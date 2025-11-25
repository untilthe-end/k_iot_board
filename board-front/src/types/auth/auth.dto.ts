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
  success: any;
  data: any;
  accessToken: string;
  accessTokenExpiresInMillis: number;
}

// 회원가입 요청
export interface SignupRequest {
  username: string;
  password: string;
  nickname: string;
  email: string;
}
