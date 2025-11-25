// =======================================
// Comment 관련 프론트 DTO 전체 자동 생성
// Backend Java DTO 기준 변환
// =======================================

// 댓글 작성 요청 DTO
export interface CommentCreateRequest {
  content: string;
}

// 댓글 단일 응답 DTO
export interface CommentResponse {
  commentId: number;
  boardId: number;
  userId: number;
  nickname: string;
  content: string;
  createdAt: string;  // Instant → ISO 문자열
  updatedAt: string;  // Instant → ISO 문자열
}

// 댓글 리스트 응답 DTO
export type CommentListResponse = CommentResponse[];
