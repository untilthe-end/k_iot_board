// src/hooks/auth/useMe.ts
import { useQuery } from "@tanstack/react-query";
import { userApi } from "@/apis/user/user.api";
import { useAuthStore } from "@/stores/auth.store";

export const useMe = () => {
  const { setUser } = useAuthStore();

  return useQuery({
    queryKey: ["me"],
    queryFn: async () => {
      const res = await userApi.me();
      if (res.data) setUser(res.data);
      return res.data;
    },
  });
};
