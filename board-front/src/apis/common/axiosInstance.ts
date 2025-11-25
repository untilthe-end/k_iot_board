// axiosInstance.ts

import { useAuthStore } from "@/stores/auth.store";
import axios from "axios";

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
  withCredentials: true,
});

export const privateApi = axios.create({
  baseURL: API_BASE,
  withCredentials: true,
});

// ============ Request ============
privateApi.interceptors.request.use((config) => {
  const access = useAuthStore.getState().accessToken;
  if (access && config.headers) {
    config.headers["Authorization"] = `Bearer ${access}`;
  }
  return config;
});

// ============ Response(자동 갱신) ============
privateApi.interceptors.response.use(
  (res) => res,
  async (error) => {
    const original = error.config;

    if (error.response?.status === 401 && !original._retry) {
      original._retry = true;

      const refresh = useAuthStore.getState().refreshToken;
      if (!refresh) return Promise.reject(error);

      try {
        const res = await publicApi.post("/auth/refresh", {
          refreshToken: refresh,
        });

        const data = res.data.data;
        if (!data) return Promise.reject(error);

        // Zustand에 갱신 저장
        useAuthStore.getState().setTokens(data.accessToken, data.refreshToken);

        // 원래 요청 재시도
        original.headers["Authorization"] = `Bearer ${data.accessToken}`;
        return privateApi(original);
      } catch (e) {
        useAuthStore.getState().logout();
        return Promise.reject(error);
      }
    }

    return Promise.reject(error);
  }
);