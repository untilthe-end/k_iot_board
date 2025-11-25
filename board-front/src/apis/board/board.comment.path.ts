//! board.comment.path.ts

//! board.comment.path.ts

import { BASE } from "../common/base.path";

const BOARD_PREFIX = `${BASE}/boards`;

export const BOARD_COMMENT_PATH = {
  COMMENTS: (boardId: number) =>
    `${BOARD_PREFIX}/${boardId}/comments`,

  COMMENT_BY_ID: (boardId: number, commentId: number) =>
    `${BOARD_PREFIX}/${boardId}/comments/${commentId}`,
};
