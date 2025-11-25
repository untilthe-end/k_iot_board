// =======================================
// File DTO (FileInfo 기반) - Backend 기준 자동 변환
// =======================================

// 백엔드 FileInfo 엔티티 기반
export interface FileInfoDto {
  fileId: number;
  originalName: string;
  storedName: string;
  contentType: string;
  fileSize: number;
  filePath: string;
  createdAt: string;
}

// 파일 업로드 응답
export interface FileUploadResponse {
  fileId: number;
  url: string;
}

// 게시글 첨부파일 리스트
export interface BoardFileListDto {
  fileId: number;
  originalName: string;
  storedName: string;
  contentType: string;
  fileSize: number;
  downloadUrl: string;
}
