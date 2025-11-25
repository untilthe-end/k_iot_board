//! user.path.ts

import { BASE } from "../common/base.path";

const USER_PREFIX = `${BASE}/users`;

export const USER_PATH = {
  ROOT: USER_PREFIX,

  LIST: USER_PREFIX,
  CREATE: USER_PREFIX,

  // 동적 변수값은 반드시 함수로 작성!
  BY_ID: (userId: number) => `${USER_PREFIX}/${userId}`,

  ME: `${USER_PREFIX}/me`,
}