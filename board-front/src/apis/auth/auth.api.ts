// auth.api.ts

import type { LoginRequest, LoginResponse } from "@/types/auth/auth.dto";
import { publicApi } from "../common/axiosInstance";
import type { ResponseDto } from "@/types/common/ResponseDto";
import { AUTH_PATH } from "./auth.path";

//% 로그인/로그아웃/토큰 재발급
export const authApi = {
  loginApi: async (req: LoginRequest): Promise<LoginResponse> => {
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
  
  refresh: async () => {
    const res = await publicApi.post<ResponseDto<LoginResponse>>(
      "/api/v1/auth/refresh"
    );
    return res.data.data;
  },
  
  /** 로그아웃 (서버 API 없어도 프론트에서 동작 가능) */
  logout: async () => {
    // 서버에 요청할 게 없다면 빈 Promise 반환
    return Promise.resolve(true);
  },
};

// # login  : 서버에 ID/PW 보내서 토큰 받기
// # refresh: 만료된 토큰 -> 새 토큰으로 교체
// # logout : 서버 호출 없이 FE에서 로그아웃 처리


// async 함수의 반환타입: Promise
// axios.메서드<메서드반환타입>();