package org.example.boardback.service.auth.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.boardback.common.enums.ErrorCode;
import org.example.boardback.dto.ResponseDto;
import org.example.boardback.dto.auth.request.*;
import org.example.boardback.dto.auth.response.LoginResponseDto;
import org.example.boardback.dto.auth.response.PasswordVerifyResponseDto;
import org.example.boardback.dto.auth.response.SignupResponseDto;
import org.example.boardback.entity.auth.RefreshToken;
import org.example.boardback.entity.user.User;
import org.example.boardback.exception.BusinessException;
import org.example.boardback.repository.auth.RefreshTokenRepository;
import org.example.boardback.repository.user.UserRepository;
import org.example.boardback.security.provider.JwtProvider;
import org.example.boardback.security.user.UserPrincipalMapper;
import org.example.boardback.security.util.CookieUtils;
import org.example.boardback.service.auth.AuthService;
import org.example.boardback.service.auth.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final UserPrincipalMapper userPrincipalMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${app.oauth2.authorized-redirect-uri}")
    private String redirectUri;

    private static final String REFRESH_TOKEN = "refreshToken";

    // ============================================
    // 로그인
    // ============================================
    @Override
    @Transactional
    public ResponseDto<LoginResponseDto> login(LoginRequestDto request, HttpServletResponse response) {
        try {
            // 스프링 시큐리티 AuthenticationManager로 인증 시도
            // : ID가 존재하는지
            // : 해당 ID의 비밀번호가 맞는지
            // >> 전부 내부에서 검사함! (틀리면 BadCredentialsException 발생)
            var authToken = new UsernamePasswordAuthenticationToken(
                    request.username(), request.password()
            );

            var authentication = authenticationManager.authenticate(authToken);

            // 인증 성공: username 꺼내기
            String username = authentication.getName();

            // UserPrincipal 권한 조회
            var principal = userPrincipalMapper.toPrincipal(username);
            Set<String> roles = principal.getAuthorities()
                    .stream()
                    .map(a -> a.getAuthority())
                    .collect(java.util.stream.Collectors.toSet());

            // Access/Refresh Token 생성
            String accessToken = jwtProvider.generateAccessToken(username, roles);
            String refreshToken = jwtProvider.generateRefreshToken(username, roles);

            long accessExpiresIn = jwtProvider.getRemainingMillis(accessToken);
            long refreshRemaining = jwtProvider.getRemainingMillis(refreshToken);

            Instant refreshExpiry = Instant.now().plusMillis(refreshRemaining);

            // DB에 RefreshToken 저장(또는 갱신)
            // : '즉, 이 계정은 현재 해당 RefreshToken을 가진 상태다' 라는 것을 DB에 기록
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            refreshTokenRepository.findByUser(user)
                    .ifPresentOrElse(
                            r -> {
                                r.renew(refreshToken, refreshExpiry);
                                refreshTokenRepository.save(r);
                            },
                            () -> {
                                RefreshToken r = RefreshToken.builder()
                                        .user(user)
                                        .token(refreshToken)
                                        .expiry(refreshExpiry)
                                        .build();
                                refreshTokenRepository.save(r);
                            }
                    );

            CookieUtils.addHttpOnlyCookie(
                    response,
                    REFRESH_TOKEN,
                    refreshToken,
                    (int) (refreshRemaining / 1000),
                    true
            );

            return ResponseDto.success(
                    "로그인 성공",
                    LoginResponseDto.of(accessToken, accessExpiresIn)
            );

        } catch (BadCredentialsException ex) {
            throw new BusinessException(ErrorCode.AUTHENTICATION_FAILED);
        } catch (Exception ex) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, ex.getMessage());
        }
    }

    // ============================================
    // Refresh Token 재발급
    // ============================================
    @Override
    @Transactional
    public ResponseDto<LoginResponseDto> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        // HttpOnly 쿠키에서 refreshToken 읽기
        String refreshToken = CookieUtils.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElseThrow(() -> new BusinessException(ErrorCode.TOKEN_EXPIRED)); // ErrorCode.REFRESH_TOKEN_NOT_FOUND가 맞음

        if (!jwtProvider.isValidToken(refreshToken)) {
            // 유효하지 않은 토큰
            throw new BusinessException(ErrorCode.TOKEN_INVALID);
        }

        // 토큰에서 username 꺼내기
        String username = jwtProvider.getUsernameFromJwt(refreshToken);

        // DB에서 해당 유저의 RefreshToken 레코드 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        RefreshToken stored = refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new BusinessException(ErrorCode.TOKEN_INVALID)); // ErrorCode.REFRESH_TOKEN_NOT_FOUND가 맞음

        // DB에 저장된 토큰과 현재 요청 토큰이 같은지 확인
        if (!stored.getToken().equals(refreshToken)) {
            throw new BusinessException(ErrorCode.TOKEN_INVALID, "Refresh token mismatch");
        }

        // 토큰 만료 여부 체크
        if (stored.isExpired()) {
            throw new BusinessException(ErrorCode.TOKEN_EXPIRED);
        }

        // 새 Access/Refresh Token 생성 + RefreshToken 엔티티 갱신
        var principal = userPrincipalMapper.map(user);
        Set<String> roles = principal.getAuthorities()
                .stream().map(a -> a.getAuthority()).collect(java.util.stream.Collectors.toSet());

        String newAccess = jwtProvider.generateAccessToken(username, roles);
        String newRefresh = jwtProvider.generateRefreshToken(username, roles);

        long accessExpiresIn = jwtProvider.getRemainingMillis(newAccess);
        long refreshRemaining = jwtProvider.getRemainingMillis(newRefresh);

        stored.renew(newRefresh, Instant.now().plusMillis(refreshRemaining));
        refreshTokenRepository.save(stored);

        CookieUtils.addHttpOnlyCookie(
                response,
                REFRESH_TOKEN,
                newRefresh,
                (int) (refreshRemaining) / 1000,
                false
        );

        return ResponseDto.success(
                "토큰 재발급 완료",
                LoginResponseDto.of(newAccess, accessExpiresIn)
        );
    }

    // ============================================
    // 로그아웃 → Refresh Token 제거
    // ============================================
    @Override
    @Transactional
    public ResponseDto<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        // 1) 쿠키에서 refreshToken 가져오기
        CookieUtils.getCookie(request, REFRESH_TOKEN).ifPresent(cookie -> {
            String refreshToken = cookie.getValue();

            if (jwtProvider.isValidToken(refreshToken)) {
                String username = jwtProvider.getUsernameFromJwt(refreshToken);
                userRepository.findByUsername(username).ifPresent(user -> refreshTokenRepository.deleteByUser(user));
            }
        });

        CookieUtils.deleteCookie(response, REFRESH_TOKEN);

        return ResponseDto.success("로그아웃 완료");
    }

    // ============================================
    // 회원가입 + 이메일 인증
    // ============================================
    @Override
    @Transactional
    public ResponseDto<SignupResponseDto> signup(SignupRequestDto request) {

        // isPresent(): Optional 타입 내부에 데이터가 존재하면 true, 존재하지 않으면 false
        // 아이디 중복 체크
        if (userRepository.findByUsername(request.username()).isPresent()) {
            // 기존에 해당 아이디가 있으면 예외 발생
            throw new BusinessException(ErrorCode.DUPLICATE_USER);
        }

        // 이메일 중복 체크
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATE_USER);
        }

        // User 엔티티 생성 + 저장
        User newUser = User.builder()
                .username(request.username())
                // 비밀번호 암호화
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .nickname(request.nickname())
                .build();

        userRepository.save(newUser);

        // (선택) 이메일 인증용 토큰 발급 + 메일 전송
        String emailToken = jwtProvider.generateEmailJwtToken(request.email(), "VERIFY_EMAIL");

        String verifyUrl = "https://myservice.com/email/verify?token=" + emailToken;

        emailService.sendHtmlEmail(
                request.email(),
                "회원가입 이메일 인증",
                """
                <div style="padding:20px; font-size:16px;">
                    <p>회원가입을 환영합니다!</p>
                    <p>아래 버튼을 눌러 이메일 인증을 완료해주세요.</p>
                    <a href="%s"
                        style="display:inline-block; padding:10px 20px; background:#2a5dff;
                               color:white; text-decoration:none; border-radius:8px; margin-top:10px;">
                        이메일 인증하기
                    </a>
                </div>
                """.formatted(verifyUrl)
        );

        return ResponseDto.success(
                "회원가입 완료",
                SignupResponseDto.from(newUser)
        );
    }

    // ============================================
    // 비밀번호 재설정 토큰 확인
    // ============================================
    @Override
    public ResponseDto<PasswordVerifyResponseDto> verifyPasswordToken(String token) {

        if (!jwtProvider.isValidToken(token)) {
            return ResponseDto.success(PasswordVerifyResponseDto.failure());
        }

        String email = jwtProvider.getEmailFromEmailToken(token);
        return ResponseDto.success(PasswordVerifyResponseDto.success(email));
    }

    // ============================================
    // 비밀번호 재설정
    // ============================================
    @Override
    @Transactional
    public ResponseDto<Void> resetPassword(PasswordResetRequestDto request) {

        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_CONFIRM_MISMATCH);
        }

        String token = request.token();

        if (!jwtProvider.isValidToken(token)) {
            throw new BusinessException(ErrorCode.TOKEN_INVALID);
        }

        String email = jwtProvider.getEmailFromEmailToken(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        user.changePassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        refreshTokenRepository.findByUser(user)
                .ifPresent(refreshTokenRepository::delete);

        return ResponseDto.success("비밀번호 재설정 완료");
    }

    // ============================================
    // 비밀번호 재설정 메일 발송
    // ============================================
    @Override
    public ResponseDto<Void> sendPasswordResetEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String token = jwtProvider.generateEmailJwtToken(email, "RESET_PASSWORD");

        String url = "https://myservice.com/reset-password?token=" + token;

        emailService.sendPasswordReset(email, url);

        return ResponseDto.success("비밀번호 재설정 이메일 전송 완료");
    }

    @Override
    public ResponseDto<Void> sendVerifyCode(String email) {
        return null;
    }

    @Override
    public ResponseDto<Void> verifyEmailCode(String email, String code) {
        return null;
    }
}