//! user.password.path.ts

import { BASE } from "../common/base.path";

const USER_PREFIX = `${BASE}/users`;

export const USER_PASSWORD_PATH = {
  RESET: (userId: number) => `${USER_PREFIX}/${userId}/password`,
}