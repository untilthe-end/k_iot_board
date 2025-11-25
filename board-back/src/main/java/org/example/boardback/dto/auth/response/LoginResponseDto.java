package org.example.boardback.dto.auth.response;

public record LoginResponseDto (
        String accessToken,
        long accessTokenExpiresInMillis
) {
    // of) 일반적인 정적 팩토리 생성, 값 그대로 매핑 (여러 값을 조합해 생성)
    public static LoginResponseDto of(String accessToken, long accessTokenExpiresInMillis) {
        return new LoginResponseDto(accessToken, accessTokenExpiresInMillis);
    }
}