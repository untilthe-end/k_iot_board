//! board.path.ts

import { BASE } from "../common/base.path";

const BOARD_PREFIX = `${BASE}/boards`;

export const BOARD_PATH = {
  //? cf) SpringBoot의 controller는 class 파일 단위의 Mapping값 설정 가능
  //      VS React HTTP의 경로는 완전한 경로값을 전달
  ROOT: BOARD_PREFIX,

  LIST: BOARD_PREFIX,
  CREATE: BOARD_PREFIX,

  BY_ID: (boardId: number) => `${BOARD_PREFIX}/${boardId}`,

  // 카테고리
  CATEGORY: (categoryId: number) => `${BOARD_PREFIX}/category/${categoryId}`,
  COUNT_BY_CATEGORY: `${BOARD_PREFIX}/category-count`,

  // 검색
  SEARCH: `${BOARD_PREFIX}/search`,

  // 페이징 조회
  PAGE: `${BOARD_PREFIX}/page`,

  // 내가 쓴 글
  MY_BOARDS: `${BOARD_PREFIX}/me`,

  // 조회수 증가
  VIEW: (boardId: number) => `${BOARD_PREFIX}/${boardId}/view`,

  // 좋아요
  LIKE: (boardId: number) => `${BOARD_PREFIX}/${boardId}/like`,
  LIKE_CANCEL: (boardId: number) => `${BOARD_PREFIX}/${boardId}/like/cancel`,
  LIKE_COUNT: (boardId: number) => `${BOARD_PREFIX}/${boardId}/like/count`,

  // 게시글 고정 (pin)
  PIN: (boardId: number) => `${BOARD_PREFIX}/${boardId}/pin`,
  UNPIN: (boardId: number) => `${BOARD_PREFIX}/${boardId}/unpin`,
  PINNED_LIST: `${BOARD_PREFIX}/pinned`,

  // 신고
  REPORT: (boardId: number) => `${BOARD_PREFIX}/${boardId}/report`,

  // 임시 저장
  DRAFT: `${BOARD_PREFIX}/draft`,
  DRAFT_BY_ID: (draftId: number) => `${BOARD_PREFIX}/draft/${draftId}`,

  // 통계
  STATS: `${BOARD_PREFIX}/stats`,
  STATS_DAILY: `${BOARD_PREFIX}/stats/daily`,
  STATS_MONTHLY: `${BOARD_PREFIX}/stats/monthly`,
  STATS_GENDER: `${BOARD_PREFIX}/stats/gender`,
}