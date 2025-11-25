//! board.file.path.ts
// : 게시판 파일 관련 프론트엔드 호출용 Path 구조

import { BASE } from "../common/base.path";

// 백엔드 BoardFileApi.ROOT = /api/v1/board-files
const BOARD_FILE_PREFIX = `${BASE}/board-files`;

export const BOARD_FILE_PATH = {
  //# 게시글 기준 파일 업로드/조회/수정
  // /api/v1/board-files/{boardId}/files
  FILES_BY_BOARD: (boardId: number) =>
    `${BOARD_FILE_PREFIX}/${boardId}/files`,

  //# 개별 파일 (boardId 필요 X)
  // /api/v1/board-files/files/{fileId}
  FILE_BY_ID: (fileId: number) =>
    `${BOARD_FILE_PREFIX}/files/${fileId}`,

  //# 다운로드
  // /api/v1/board-files/files/{fileId}/download
  DOWNLOAD: (fileId: number) =>
    `${BOARD_FILE_PREFIX}/files/${fileId}/download`,
};
