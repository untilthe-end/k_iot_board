import { publicApi } from "@/apis/common/axiosInstance";

// 공통 Response 형식
export interface ResponseDto<T> {
  success: boolean;       // 성공 여부
  message: string;        // 메시지
  data?: T | null;        // 응답 데이터 (성공 시)
  timestamp: string;      // ISO 날짜 문자열 (Instant -> string)

  status?: number | null; // HTTP 상태 코드
  code?: string | null;   // ErrorCode (C001, A003 등)
}
