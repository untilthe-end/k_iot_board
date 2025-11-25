// src/hooks/board/useBoardFiles.ts
import { useQuery } from "@tanstack/react-query";
import { boardApi } from "@/apis/board/board.api";

export const useBoardFiles = (boardId: number) => {
  return useQuery({
    queryKey: ["boards", boardId, "files"],
    queryFn: () => boardApi.getFilesByBoard(boardId),
    enabled: !!boardId,
  });
};
