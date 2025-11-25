import { boardApi } from "@/apis/board/board.api";

export async function downloadFile(fileId: number, filename?: string) {
  const data: Blob = await boardApi.DOWNLOAD(fileId); // blob 타입의 데이터

  const link = document.createElement('a');
  const url = window.URL.createObjectURL(data);
  link.href = url;
  link.download = filename || `file_${fileId}`;
  document.body.appendChild(link);
  link.click();
  link.remove();
  window.URL.revokeObjectURL(url);
}

//^ ||, && 연산자 (논리 연산자, boolean 논리 값 사용)
//  ?? 연산자
//  : 왼쪽 값이 null 또는 undefined 일 때만 오른쪽 값을 사용