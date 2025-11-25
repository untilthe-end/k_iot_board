// src/hooks/comment/useCommentList.ts
import { useQuery } from "@tanstack/react-query";
import { commentApi } from "@/apis/board/comment.api";

export const useCommentList = (boardId: number) => {
  return useQuery({
    queryKey: ["boards", boardId, "comments"],
    queryFn: () => commentApi.list(boardId),
    enabled: !!boardId,
  });
};
