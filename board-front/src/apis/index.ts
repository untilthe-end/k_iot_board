//! apis 폴더
// : API - Application Programming Interface
// > 두 개의 소프트웨어 프로그램이 서로 통신하고 상호작용할 수 있도록 하는 정의, 규칙, 프로토콜의 집합

//? apis 정의 규칙
// : 도메인/리소스 단위 폴더 구성
// - common 폴더
//   > axiosInstance.ts
//   > ApiResponse.ts   (<-> 백엔드 ResponseDto)
// - auth 폴더
//   > auth.api.ts
// - user 폴더
//   > user.api.ts

//? API는 도메인 단위 "객체형 export" 권장

//! Path는 도메인 별 분리
// : prefix는 분리하고, endpoint만 객체 구조로 명시