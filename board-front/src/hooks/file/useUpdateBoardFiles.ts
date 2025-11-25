// src/hooks/file/useUpdateBoardFiles.ts
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { boardApi } from "@/apis/board/board.api";

export const useUpdateBoardFiles = (boardId: number) => {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (formData: FormData) =>
      boardApi.UPDATE_BOARD_FILES(boardId, formData),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ["boards", boardId, "files"] });
    },
  });
};
