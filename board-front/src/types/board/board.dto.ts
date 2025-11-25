//! board.dto.ts

// =============================
// Board 관련 프론트 DTO 전체 생성
// Backend Java DTO 기준 자동 변환
// =============================

// 게시글 생성 요청
export interface BoardCreateRequest {
  title: string;
  content?: string;
  categoryId?: number;
  fileIds?: number[]; // 기존 업로드된 FILE ID들
}

// 게시글 수정 요청
export interface BoardUpdateRequest {
  title: string;
  content?: string;
  categoryId?: number;
  deleteFileIds?: number[];
  addFileIds?: number[];
}

// 게시글 요약 DTO
export interface BoardSummaryDto {
  boardId: number;
  title: string;
  writerId: number;
  writerNickname: string;
  likeCount: number;
  commentCount: number;
  pinned: boolean;
  createdAt: string; // Instant
}

// 게시글 단일 조회 DTO
export interface BoardResponse {
  boardId: number;
  title: string;
  content: string;
  categoryId?: number;
  writerId: number;
  writerNickname: string;
  likeCount: number;
  commentCount: number;
  pinned: boolean;
  createdAt: string;
  updatedAt: string;
  files: BoardFileListDto[];
}

// 검색 요청
export interface BoardSearchRequest {
  keyword: string;
  searchType: "title" | "content" | "author" | "all";
  categoryId?: number;
}

// 페이징 결과
export interface BoardPageDto {
  items: BoardSummaryDto[];
  totalElements: number;
  totalPages: number;
}

// 게시글 리스트 응답
export type BoardListResponse = BoardSummaryDto[];

// 핀(Pinned) 게시글 리스트
export interface BoardPinnedListResponse {
  pinned: BoardSummaryDto[];
}

// 좋아요 개수 응답
export interface BoardLikeCountResponse {
  boardId: number;
  count: number;
  likedByMe: boolean;
}

// 임시 저장(DRAFT) 생성/수정 요청
export interface BoardDraftRequest {
  title: string;
  content?: string;
  categoryId?: number;
  fileIds?: number[];
}

// 임시 저장(DRAFT) 조회 응답
export interface BoardDraftResponse extends BoardResponse {}

// =============================
// 통계 DTO
// =============================

// 일별 통계
export interface BoardStatsDailyResponse {
  dateToCount: Record<string, number>; // LocalDate → count
}

// 월별 통계
export interface BoardStatsMonthlyResponse {
  monthToCount: Record<string, number>; // "2024-01" → count
}

// 성별 통계
export interface BoardStatsGenderResponse {
  genderToCount: Record<string, number>;
}

// =============================
// 게시글 파일 DTO
// backend: BoardFileListDto.java
// =============================
export interface BoardFileListDto {
  fileId: number;
  originalName: string;
  storedName: string;
  contentType: string;
  fileSize: number;
  downloadUrl: string;
}
