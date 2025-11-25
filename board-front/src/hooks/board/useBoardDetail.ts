// src/hooks/board/useBoardDetail.ts
import { useQuery } from "@tanstack/react-query";
import { boardApi } from "@/apis/board/board.api";

export const useBoardDetail = (boardId: number) => {
  return useQuery({
    queryKey: ["boards", boardId],
    queryFn: () => boardApi.getById(boardId),
    enabled: !!boardId,
  });
};
