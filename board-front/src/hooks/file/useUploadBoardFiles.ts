// src/hooks/file/useUploadBoardFiles.ts
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { boardApi } from "@/apis/board/board.api";

export const useUploadBoardFiles = (boardId: number) => {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (formData: FormData) =>
      boardApi.uploadBoardFiles(boardId, formData),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ["boards", boardId, "files"] });
    },
  });
};
