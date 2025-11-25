// src/hooks/auth/useLogin.ts
import { useMutation } from "@tanstack/react-query";
import { authApi } from "@/apis/auth/auth.api";
import { queryClient } from "@/query/client";
import type { LoginRequest } from "@/types/auth/auth.dto";
import { useAuthStore } from "@/stores/auth.store";

export const useLogin = () => {
  const { setTokens, setUser } = useAuthStore();

  return useMutation({
    mutationFn: (payload: LoginRequest) => authApi.login(payload),
    onSuccess: (data) => {
      setTokens(data.accessToken, data.refreshToken);
      setUser(data);
      // 미리 me 캐시 세팅
      queryClient.setQueryData(["me"], data);
    },
  });
};
