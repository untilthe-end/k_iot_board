// src/hooks/auth/useLogout.ts
import { useMutation } from "@tanstack/react-query";
import { authApi } from "@/apis/auth/auth.api";
import { queryClient } from "@/query/client";
import { useAuthStore } from "@/stores/auth.store";

export const useLogout = () => {
  const { logout } = useAuthStore();

  return useMutation({
    mutationFn: () => authApi.logout(), // Todo backend에 logout API 구현
    onSettled: () => {
      logout();
      queryClient.clear();
    },
  });
};
