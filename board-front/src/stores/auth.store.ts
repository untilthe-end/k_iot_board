// src/store/auth.store.ts
import { create } from "zustand";
import { persist } from "zustand/middleware";
import type { MeResponse } from "@/types/user/user.dto";

//& 상태 관리 데이터
type AuthState = {
  accessToken: string | null;         // 로그인 후 받은 JWT 토큰
  user: MeResponse | null;            // 로그인 된 사용자 정보
  isInitialized: boolean;             // 초기 로딩 여부
}

//& 상태 변경 함수
type AuthActions = {

  setAccessToken: (token: string | null) => void;        // accessToken 상태 저장
  setUser: (user: MeResponse | null) => void;            // 로그인된 사용자 정보 저장
  clearAuth: () => void;                                 // 토큰 + 유저 정보 모두 초기화 (logout)

  // 로컬 스토리지에 있는 토큰을 읽어와 상태에 반영하는 함수
  hydrateStorage: () => void;                   // persist가 로컬스토리지 값 불러와 "초기화 완료" 표시
}

// 로컬 스토리지에 사용할 액세스 토큰 키 이름 상수
const ACCESS_STORAGE = "board_access_token"; 

//& 상태 + 함수 묶어놓은 전역 저장소
export const useAuthStore = create(
  persist<AuthState & AuthActions >(
    (set, get) => ({
      accessToken: null,
      user: null,
      isInitialized: false,

      setAccessToken: (token) => set({ accessToken: token}),
      setUser: (user) => set({user}),
      clearAuth: () => set ({ accessToken: null, user: null}),

      // persist 초기화 완료 여부 플래그 설정
      hydrateStorage: () => {
        // persist로부터 복원된 상태를 그대로 사용 -> 추가 작업 필요 없음
        set({ isInitialized: true })
      },
    }),
    {

      // onRehydrateStorage - persist가 localStorage 값 복원 끝난 후 실행되는 함수      
      name: ACCESS_STORAGE, // 로컬 스토리지 키
      onRehydrateStorage: () => (state) => {
        if (state) {
          state.isInitialized = true; // persist가 localStorage 값 로그인 정보 복원 끝났다는 표시해줌
        }
      }
    }
  )
);

// & useAuthStore
// # accessToken + user 정보를 전역으로 관리하고
// # 로컬 스토리지에 자동 저장 (persist)
// # 새로고침해도 로그인 유지되도록 만든 저장소

// (cf. persist: 로컬 스토리지 저장 여부 결정)
//! persist 옵션
// : 모든 localStorage 작업을 자동 처리 (get/set 필요 없음)
// * 새로고침해도 accessToken 유지 / 앱 종료후 다시 들어와도 user 정보 유지
// - 키 이름: AUTH_STORAGE (auth-storage)

// persist       = 냉장고
// rehydrate     = 냉장고에서 음식을 꺼내 테이블에 올리는 과정
// isInitialized = "이제 다 꺼냈습니다!" 라고 말해주는 신호