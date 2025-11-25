//! user.api.ts

import { publicApi, privateApi } from "../common/axiosInstance";
import type { ResponseDto } from "@/types/common/ResponseDto";
import type {
  UserResponse,
  MeResponse,
  UserProfileUpdateRequest,
  UserProfileImageResponse,
} from "@/types/user/user.dto";

const USER_PREFIX = "/api/v1/users";

export const userApi = {

  me: async () => {
    const res = await privateApi.get<ResponseDto<MeResponse>>(
      `${USER_PREFIX}/me`
    );
    return res.data;
  },


  getById: async (userId: number) => {
    const res = await publicApi.get<ResponseDto<UserResponse>>(
      `${USER_PREFIX}/${userId}`
    );
    return res.data;
  },


  updateProfile: async (data: UserProfileUpdateRequest) => {
    const res = await privateApi.put<ResponseDto<UserResponse>>(
      `${USER_PREFIX}/profile`,
      data
    );
    return res.data;
  },


  uploadProfileImage: async (file: File) => {
    const formData = new FormData();
    formData.append("file", file);

    const res = await privateApi.post<ResponseDto<UserProfileImageResponse>>(
      `${USER_PREFIX}/profile/image`,
      formData,
      {
        headers: { "Content-Type": "multipart/form-data" },
      }
    );

    return res.data;
  },


  deleteProfileImage: async () => {
    const res = await privateApi.delete<ResponseDto<void>>(
      `${USER_PREFIX}/profile/image`
    );
    return res.data;
  },


  changePassword: async (oldPassword: string, newPassword: string) => {
    const res = await privateApi.put<ResponseDto<void>>(
      `${USER_PREFIX}/password`,
      { oldPassword, newPassword }
    );
    return res.data;
  },
};

/** 내 정보 조회 */

/** 특정 사용자 정보 조회 */
// # publicApi 인 이유?
// # 보통 게시판 글에 붙어있는 작성자의 공개 정보 조회할 때 사용.

/** 닉네임 등 사용자 정보 업데이트 */
/** 프로필 이미지 업로드 */    
/** 프로필 이미지 삭제 */  
/** 비밀번호 변경 */
