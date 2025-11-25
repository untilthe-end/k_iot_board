// =======================================
// Auth DTO - Backend 기준 자동 변환
// =======================================

// 로그인 요청
export interface LoginRequest {
  username: string;
  password: string;
}

// 로그인 응답
export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
  accessTokenExpiresInMillis: number;
  userId: number;
  nickname: string;
}

// 회원가입 요청
export interface SignupRequest {
  username: string;
  password: string;
  nickname: string;
}

// Refresh 요청
export interface RefreshRequest {
  refreshToken: string;
}
