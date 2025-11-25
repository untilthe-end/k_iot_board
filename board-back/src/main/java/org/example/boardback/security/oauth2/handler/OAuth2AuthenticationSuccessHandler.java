package org.example.boardback.security.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.boardback.common.enums.ErrorCode;
import org.example.boardback.entity.auth.RefreshToken;
import org.example.boardback.entity.user.User;
import org.example.boardback.exception.BusinessException;
import org.example.boardback.repository.auth.RefreshTokenRepository;
import org.example.boardback.repository.user.UserRepository;
import org.example.boardback.security.provider.JwtProvider;
import org.example.boardback.security.user.UserPrincipal;
import org.example.boardback.security.util.CookieUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * OAuth2 로그인 성공 시
 * - JWT Access / Refresh 발급
 * - 프론트엔드 콜백 URL로 리다이렉트하면서 토큰 전달
 * */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    // 정의된 리다이렉트 URI 값을 주입
    @Value("${app.oauth2.authorized-redirect-uri}")
    private String redirectUri;

    // 리프레시 토큰을 담을 쿠키 이름 상수: HttpONLY 쿠키
    private static final String REFRESH_TOKEN = "refreshToken";

    @Override
    public void onAuthenticationSuccess (
            HttpServletRequest request,     // HTTP 요청 객체
            HttpServletResponse response,   // HTTP 응답 객체
            Authentication authentication   // 인증 정보 (로그인 한 사용자 정보 포함)
    ) throws IOException, ServletException {
        // SecurityContext에 저장된 principal(UserDetails 구현체)을 꺼냄
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        String username = principal.getUsername();

        // username 기준으로 DB 에서 실제 User 엔티티 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // principal이 가진 권한(ROLE 들)을 문자열 Set으로 추출
        Set<String> roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)            // ROLE_USER 같은 문자열로 변환
                .collect(Collectors.toSet());                   // 중복이 없는 Set으로 수집

        // JWT 토큰 생성 (Access/Refresh)
        String accessToken = jwtProvider.generateAccessToken(username, roles);
        String refreshToken = jwtProvider.generateRefreshToken(username, roles);

        // 방금 생성한 refreshToken의 남은 만료 시간을 밀리초 단위로 환산
        long refreshMillis = jwtProvider.getRemainingMillis(refreshToken);

        // 현재 시각 + 남은 밀리초를 더해 만료 시각을 계산
        Instant refreshExpiry = Instant.now().plusMillis(refreshMillis);

        // RefreshToken 엔티티를 DB에 upsert(있으면 갱신, 없으면 새로 저장)
        refreshTokenRepository.findByUser(user)
                .ifPresentOrElse(
                        rt -> rt.renew(refreshToken, refreshExpiry),
                        () -> refreshTokenRepository.save(
                                RefreshToken.builder()
                                        .user(user)             // 어떤 유저의 토큰인지
                                        .token(refreshToken)    // 실제 리프레시 토큰 문자열
                                        .expiry(refreshExpiry)  // 만료 시각
                                        .build()
                        ));


        CookieUtils.addHttpOnlyCookie(
                response,
                REFRESH_TOKEN,
                refreshToken,
                (int) (refreshMillis / 1000),
                false   // HTTPS 에서만 전송하도록 Secure 옵션 설정
        );

        // 프론트엔드로 보낼 리다이렉트 URL 생성
        // +) accessToken은 쿼리 파라미터에 포함하여 전송
        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("accessToken", accessToken)
                .build()
                .toUriString();

        // 세션에 남아 있을 수 있는 인증 관련 속성들 정리
        clearAuthenticationAttributes(request);

        // 최종적으로 클라이언트를 targetUrl로 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}