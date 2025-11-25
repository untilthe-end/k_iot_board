// =======================================
// User DTO - Backend 기준 자동 변환
// =======================================

// 유저 기본 정보
export interface UserResponse {
  userId: number;
  username: string;
  nickname: string;
  profileImageUrl?: string;
  createdAt: string;
}

// 유저 프로필 업데이트 요청
export interface UserProfileUpdateRequest {
  nickname: string;
}

// 유저 프로필 이미지(FileInfo DTO 사용)
export interface UserProfileImageResponse {
  fileId: number;
  url: string;
}

// 로그인된 사용자 정보
export interface MeResponse {
  userId: number;
  username: string;
  nickname: string;
  profileImageUrl?: string;
}
