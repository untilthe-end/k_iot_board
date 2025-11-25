// axiosInstance.ts

import { useAuthStore } from "@/stores/auth.store";
import axios, { AxiosError } from "axios";

const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8080';

// export const publicApi = axios.create({
//   baseURL: API_BASE,
//   timeout: 10000,
//   headers: {
//     "Content-Type": "application/json",
//     Accept: "application/json"
//   },
//   withCredentials: true
// });

export const publicApi = axios.create({
  baseURL: API_BASE,
  withCredentials: true,  // refreshToken 쿠키 전달
});

export const privateApi = axios.create({
  baseURL: API_BASE,
  withCredentials: true,
});

// ============ Request ============
// : REQUEST Interceptor: Access Token 자동 삽입
privateApi.interceptors.request.use((config) => {
  const {accessToken} = useAuthStore.getState();
  if (accessToken && config.headers) {
    config.headers["Authorization"] = `Bearer ${accessToken}`;
  }
  return config;
});

//? Refresh 401(만료) 처리
let isRefreshing = false;
let failQueue: Array<{
  resolve: (token: string | null ) => void;
  reject: (err: unknown) => void;
}> = [];

// refresh 이후 큐에 쌓인 요청들을 모두 처리하는 함수
const processQueue = (error: unknown, token: string | null) => {
  failQueue.forEach(process => {
    if (error) process.reject(error);
    else process.resolve(token);
  });
  failQueue = [];
}

// ============ Response(자동 갱신/재발급) ============
privateApi.interceptors.response.use(
  (res) => res,
  async (error: AxiosError) => {
    const original = error.config as any;

    // 401(만료) + 재시도 안 된 요청만 처리
    if (error.response?.status === 401 && !original._retry) {
      const { clearAuth, setAccessToken } = useAuthStore.getState();

      // refresh 중이면 Que에 넣기
      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          failQueue.push({
            resolve: (newToken) => {
              if (newToken) original.headers.Authorization = `Bearer ${newToken}`;
              resolve(privateApi(original));
            },
            reject,
          });
        });
      }

      //! refresh 요청을 처음 시작하는 경우
      original._retry = true;
      isRefreshing = true;

      try {
        const {data} = await publicApi.post("/api/v1/auth/refresh");

        const newAccessToken = (data as any).data.accessToken;

        if (!data) return Promise.reject(error);

        // Zustand에 갱신 저장
        setAccessToken(newAccessToken);

        // 원래 요청 재시도
        original.headers["Authorization"] = `Bearer ${data.newAccessToken}`;
        return privateApi(original);

      } catch (refreshError) {
        processQueue(refreshError, null);
        clearAuth();
        return Promise.reject(refreshError);
        
      } finally {
        isRefreshing = false;
      }
    }
    return Promise.reject(error);
  }
);