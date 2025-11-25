import { publicApi } from "@/apis/common/axiosInstance";
import { useAuthStore } from "@/stores/auth.store";
import type { LoginResponse } from "@/types/auth/auth.dto";
import type { ResponseDto } from "@/types/common/ResponseDto";
import type { MeResponse } from "@/types/user/user.dto";
import { useQuery } from "@tanstack/react-query";

export function useAuthInitQuery() {
  const { accessToken, refreshToken, setTokens, setUser, logout } =
    useAuthStore();

  return useQuery({
    queryKey: ["auth", "init"],
    queryFn: async () => {
      // 1) accessToken 있으면 최소한 try me
      if (accessToken) {
        try {
          const res = await publicApi.get<ResponseDto<MeResponse>>(
            "/api/v1/auth/me"
          );
          if (res.data.data) setUser(res.data.data);
          return true;
        } catch {
          // access 토큰이 죽어있으면 아래 refresh 로직으로
        }
      }

      // 2) refreshToken 있으면 /auth/refresh 로 재발급 시도
      if (refreshToken) {
        try {
          const res = await publicApi.post<ResponseDto<LoginResponse>>(
            "/api/v1/auth/refresh",
            { refreshToken }
          );
          const data = res.data.data;
          if (data) {
            setTokens(data.accessToken, data.refreshToken);
            // 필요하면 여기서도 me 조회 or data로 user 구성
            return true;
          }
        } catch {
          logout();
          return false;
        }
      }

      return false;
    },
    retry: 0,
  });
};