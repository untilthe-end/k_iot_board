//! board.api.ts

import { type ResponseDto } from "@/types/common/ResponseDto";
import { publicApi, privateApi } from "../common/axiosInstance";
import { BOARD_PATH } from "./board.path";
import type {
  BoardCreateRequest,
  BoardResponse,
  BoardListResponse,
  BoardUpdateRequest,
  BoardSearchRequest,
  BoardDraftRequest,
} from "@/types/board/board.dto";
import { BOARD_FILE_PATH } from "./board.file.path";

export const boardApi = {
  /** 게시글 생성 */
  create: async (data: BoardCreateRequest) => {
    const res = await privateApi.post<ResponseDto<BoardResponse>>(
      BOARD_PATH.CREATE,
      data
    );
    return res.data;
  },

  /** 게시글 단일 조회 */
  getById: async (boardId: number) => {
    const res = await publicApi.get<ResponseDto<BoardResponse>>(
      BOARD_PATH.BY_ID(boardId)
    );
    return res.data;
  },

  /** 전체 조회 */
  list: async () => {
    const res = await publicApi.get<ResponseDto<BoardListResponse>>(
      BOARD_PATH.LIST
    );
    return res.data;
  },

  /** 게시글 수정 */
  update: async (boardId: number, data: BoardUpdateRequest) => {
    const res = await privateApi.put<ResponseDto<BoardResponse>>(
      BOARD_PATH.BY_ID(boardId),
      data
    );
    return res.data;
  },

  /** 게시글 삭제 */
  delete: async (boardId: number) => {
    await privateApi.delete(BOARD_PATH.BY_ID(boardId));
  },

  /** 카테고리별 조회 */
  getByCategory: async (categoryId: number) => {
    const res = await publicApi.get<ResponseDto<BoardListResponse>>(
      BOARD_PATH.CATEGORY(categoryId)
    );
    return res.data;
  },

  /** 카테고리별 게시글 개수 */
  countByCategory: async () => {
    const res = await publicApi.get<ResponseDto<number>>(
      BOARD_PATH.COUNT_BY_CATEGORY
    );
    return res.data;
  },

  /** 검색 */
  search: async (params: BoardSearchRequest) => {
    const res = await publicApi.get<ResponseDto<BoardListResponse>>(
      BOARD_PATH.SEARCH,
      { params }
    );
    return res.data;
  },

  /** 페이징 조회 */
  paging: async (page: number, size: number, sort: string) => {
    const res = await publicApi.get<ResponseDto<BoardListResponse>>(
      BOARD_PATH.PAGE,
      { params: { page, size, sort } }
    );
    return res.data;
  },

  /** 내가 쓴 글 조회 */
  myBoards: async () => {
    const res = await privateApi.get<ResponseDto<BoardListResponse>>(
      BOARD_PATH.MY_BOARDS
    );
    return res.data;
  },

  /** 조회수 증가 */
  increaseView: async (boardId: number) => {
    const res = await publicApi.post<ResponseDto<void>>(
      BOARD_PATH.VIEW(boardId)
    );
    return res.data;
  },

  /** 좋아요 */
  like: async (boardId: number) => {
    const res = await privateApi.post<ResponseDto<void>>(
      BOARD_PATH.LIKE(boardId)
    );
    return res.data;
  },

  /** 좋아요 취소 */
  cancelLike: async (boardId: number) => {
    const res = await privateApi.post<ResponseDto<void>>(
      BOARD_PATH.LIKE_CANCEL(boardId)
    );
    return res.data;
  },

  /** 좋아요 개수 */
  likeCount: async (boardId: number) => {
    const res = await publicApi.get<ResponseDto<number>>(
      BOARD_PATH.LIKE_COUNT(boardId)
    );
    return res.data;
  },

  /** 게시글 고정(pin) */
  pin: async (boardId: number) => {
    const res = await privateApi.post<ResponseDto<void>>(
      BOARD_PATH.PIN(boardId)
    );
    return res.data;
  },

  /** 고정 해제 */
  unpin: async (boardId: number) => {
    const res = await privateApi.post<ResponseDto<void>>(
      BOARD_PATH.UNPIN(boardId)
    );
    return res.data;
  },

  /** 고정글 리스트 */
  pinnedList: async () => {
    const res = await publicApi.get<ResponseDto<BoardListResponse>>(
      BOARD_PATH.PINNED_LIST
    );
    return res.data;
  },

  /** 신고 */
  report: async (boardId: number) => {
    const res = await privateApi.post<ResponseDto<void>>(
      BOARD_PATH.REPORT(boardId)
    );
    return res.data;
  },

  /** 임시 저장 생성 */
  createDraft: async (dto: BoardDraftRequest) => {
    const res = await privateApi.post<ResponseDto<number>>(
      BOARD_PATH.DRAFT,
      dto
    );
    return res.data;
  },

  /** 임시 저장 조회 */
  getDraftById: async (draftId: number) => {
    const res = await privateApi.get<ResponseDto<BoardResponse>>(
      BOARD_PATH.DRAFT_BY_ID(draftId)
    );
    return res.data;
  },

  /** 임시 저장 수정 */
  updateDraft: async (draftId: number, dto: BoardDraftRequest) => {
    const res = await privateApi.put<ResponseDto<void>>(
      BOARD_PATH.DRAFT_BY_ID(draftId),
      dto
    );
    return res.data;
  },

  /** 통계 - daily */
  statsDaily: async () => {
    const res = await publicApi.get<ResponseDto<any>>(BOARD_PATH.STATS_DAILY);
    return res.data;
  },

  /** 통계 - monthly */
  statsMonthly: async () => {
    const res = await publicApi.get<ResponseDto<any>>(BOARD_PATH.STATS_MONTHLY);
    return res.data;
  },

  /** 통계 - gender */
  statsGender: async () => {
    const res = await publicApi.get<ResponseDto<any>>(BOARD_PATH.STATS_GENDER);
    return res.data;
  },

  /** ---------------------------
   *  1) 파일 목록 조회 GET
   *  /api/v1/board-files/{boardId}/files
   * --------------------------- */
  getFilesByBoard: async (boardId: number) => {
    const res = await publicApi.get<ResponseDto<BoardListResponse>>(
      BOARD_FILE_PATH.FILES_BY_BOARD(boardId)
    );
    return res.data;
  },

  /** ---------------------------
   *  2) 파일 업로드 POST
   *  /api/v1/board-files/{boardId}/files
   * --------------------------- */
  uploadBoardFiles: async (boardId: number, formData: FormData) => {
    const res = await privateApi.post<ResponseDto<void>>(
      BOARD_FILE_PATH.FILES_BY_BOARD(boardId),
      formData,
      {
        headers: { "Content-Type": "multipart/form-data" },
      }
    );
    return res.data;
  },

  /** ---------------------------
   *  3) 파일 수정(추가/삭제) PUT
   *  /api/v1/board-files/{boardId}/files
   * --------------------------- */
  UPDATE_BOARD_FILES: async (boardId: number, formData: FormData) => {
    const res = await privateApi.put<ResponseDto<void>>(
      BOARD_FILE_PATH.FILES_BY_BOARD(boardId),
      formData,
      {
        headers: { "Content-Type": "multipart/form-data" },
      }
    );
    return res.data;
  },

  /** ---------------------------
   * 파일 다운로드 GET (Blob)
   * /api/v1/board-files/files/{fileId}/download
   * --------------------------- */
  DOWNLOAD: async (fileId: number): Promise<Blob> => {
    const res = await publicApi.get(BOARD_FILE_PATH.DOWNLOAD(fileId), {
      responseType: "blob",
    });
    return res.data;
  },

  /** ---------------------------
   * 파일 삭제 DELETE
   * /api/v1/board-files/files/{fileId}
   * --------------------------- */
  DELETE_BOARD_FILE: async (fileId: number) => {
    await privateApi.delete(BOARD_FILE_PATH.FILE_BY_ID(fileId));
  },
};