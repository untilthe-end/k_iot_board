// src/store/auth.store.ts
import { create } from "zustand";
import { persist } from "zustand/middleware";
import type { MeResponse } from "@/types/user/user.dto";
import type { LoginResponse } from "@/types/auth/auth.dto";

interface AuthState {
  accessToken: string | null;
  refreshToken: string | null;
  user: MeResponse | null;

  setTokens: (access: string, refresh: string) => void;
  setUser: (user: MeResponse | LoginResponse | null) => void;
  logout: () => void;
}

export const useAuthStore = create(
  persist<AuthState>(
    (set) => ({
      accessToken: null,
      refreshToken: null,
      user: null,

      setTokens: (access, refresh) =>
        set({ accessToken: access, refreshToken: refresh }),

      setUser: (user) => {
        if (!user) return set({ user: null });
        // LoginResponse, MeResponse 모두 userId, nickname 정도는 공통이라고 가정
        const mapped: MeResponse = {
          userId: user.userId,
          username: (user as any).username ?? "",
          nickname: user.nickname,
          profileImageUrl: (user as any).profileImageUrl ?? undefined,
        };
        set({ user: mapped });
      },

      logout: () =>
        set({
          accessToken: null,
          refreshToken: null,
          user: null,
        }),
    }),
    {
      name: "auth-storage",
    }
  )
);
