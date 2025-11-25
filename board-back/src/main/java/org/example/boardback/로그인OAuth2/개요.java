package org.example.boardback.로그인OAuth2;

/*
    === 0Auth2 ===
    : 사용자가 자신의 비밀번호를 '직접' 제공하지 않고도
        , 다른 서비스(클라이언트)에게 자신의 정보에 접근할 수 있도록 권한을 위임하는 표준화 된 권한 부여 프로토콜

    > 비밀번호 제공없이, (Naver, Google, Kakao) 일부 기능만 사용할 수 있는 임시 토큰을 발급해주는 방식

    ※ 현재 User 엔티티에 0Auth2 사용을 위한 필드 설정 ※
        : provider - GOOGLE, KAKAO, NAVER, LOCAL 구분
        : providerId - 각 제공자의 unique ID (google sub, kakao id 등)

    === 0Auth2 사용 흐름 ===
    1) 프론트에서 "소셜 계정 로그인" 버튼 클릭
        : 브라우저가 백엔드 /oauth2/authorization/{sns-name} 으로 이동

    2) Spring Security가 자동으로 소셜 로그인 페이지로 리다이렉트

    3) 사용자가 소셜 로그인 완료
        : 소셜 계정이 우리 서버의 /login/oauth2/code/{sns-name}으로 코드를 보냄

    4) 서버 쪽 CustomOAuth2UserService가 코드를 AccessToken 으로 교환하고
        , 사용자 정보 (이메일/닉네임 등)를 가져옴.

    5) 우리 DB에 해당 이메일의 유저가 있으면 -> 로그인
                                  없으면 -> 자동 회원가입 후 로그인

    6) 로그인에 성공하면 OAuth2AuthenticationSuccessHandler 에서
       - 프로젝트 서비스용 JWT Access/Refresh Token 생성
       - 프론트 콜백 URL로 리디렉트
            http://localhost:5173/oauth2/callback?accessToken=...&refreshToken=...
 */
public class 개요 {
}
