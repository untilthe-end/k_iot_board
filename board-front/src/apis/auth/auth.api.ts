// auth.api.ts

import type { LoginRequest, LoginResponse } from "@/types/auth/auth.dto";
import { publicApi } from "../common/axiosInstance";
import type { ResponseDto } from "@/types/common/ResponseDto";
import { AUTH_PATH } from "./auth.path";

export const authApi = {
  // async 함수의 반환타입: Promise
  login: async (req: LoginRequest): Promise<LoginResponse> => {
    // axios.메서드<메서드반환타입>();
    const res = await publicApi.post<ResponseDto<LoginResponse>>(
      AUTH_PATH.LOGIN,
      req
    );

    if (res.data.data) {
      return res.data.data;
    } else {
      throw new Error('로그인 응답 데이터가 존재하지 않습니다.');
    }
  },

  /** refresh 토큰 재발급 */
  refresh: async (refreshToken: string) => {
    const res = await publicApi.post<ResponseDto<LoginResponse>>(
      "/api/v1/auth/refresh",
      { refreshToken }
    );
    return res.data.data;
  },

  /** 로그아웃 (서버 API 없어도 프론트에서 동작 가능) */
  logout: async () => {
    // 서버에 요청할 게 없다면 빈 Promise 반환
    return Promise.resolve(true);
  },
};
