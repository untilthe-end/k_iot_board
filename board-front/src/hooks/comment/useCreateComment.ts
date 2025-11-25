// src/hooks/comment/useCreateComment.ts
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { commentApi } from "@/apis/board/comment.api";
import type { CommentCreateRequest } from "@/types/board/comment.dto";

export const useCreateComment = (boardId: number) => {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (payload: CommentCreateRequest) =>
      commentApi.create(boardId, payload),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ["boards", boardId, "comments"] });
    },
  });
};
