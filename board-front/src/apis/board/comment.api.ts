//! comment.api.ts

import { publicApi, privateApi } from "../common/axiosInstance";
import type { ResponseDto } from "@/types/common/ResponseDto";
import { BOARD_COMMENT_PATH } from "./board.comment.path";
import type {
  CommentCreateRequest,
  CommentResponse,
} from "@/types/board/comment.dto";

export const commentApi = {
  /** 댓글 전체 조회 */
  list: async (boardId: number) => {
    const res = await publicApi.get<ResponseDto<CommentResponse[]>>(
      BOARD_COMMENT_PATH.COMMENTS(boardId)
    );
    return res.data;
  },

  /** 댓글 작성 */
  create: async (boardId: number, dto: CommentCreateRequest) => {
    const res = await privateApi.post<ResponseDto<CommentResponse>>(
      BOARD_COMMENT_PATH.COMMENTS(boardId),
      dto
    );
    return res.data;
  },

  /** 댓글 수정 */
  update: async (
    boardId: number,
    commentId: number,
    dto: CommentCreateRequest
  ) => {
    const res = await privateApi.put<ResponseDto<CommentResponse>>(
      BOARD_COMMENT_PATH.COMMENT_BY_ID(boardId, commentId),
      dto
    );
    return res.data;
  },

  /** 댓글 삭제 */
  delete: async (boardId: number, commentId: number) => {
    await privateApi.delete(
      BOARD_COMMENT_PATH.COMMENT_BY_ID(boardId, commentId)
    );
  },
};
