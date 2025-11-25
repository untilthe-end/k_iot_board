// =======================================
// Category DTO - Backend 기준 자동 변환
// =======================================

// 카테고리 정보
export interface CategoryDto {
  categoryId: number;
  name: string;
  description?: string;
  createdAt: string;
}

// 게시판에서 카테고리별 조회 시 사용
export interface CategoryCountResponse {
  categoryId: number;
  categoryName: string;
  count: number;
}
