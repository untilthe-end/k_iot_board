// src/hooks/board/useBoardList.ts
import { useQuery } from "@tanstack/react-query";
import { boardApi } from "@/apis/board/board.api";

export const useBoardList = () => {
  return useQuery({
    queryKey: ["boards", "list"],
    queryFn: () => boardApi.list(), // ResponseDto<BoardListResponse>
  });
};
