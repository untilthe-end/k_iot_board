import { useAuthInitQuery } from "@/hooks/auth/useAuthInitQuery";

export default function App() {
  const { data: isLoggedIn, isLoading } = useAuthInitQuery();

  if (isLoading) return <div>Loading...</div>;

  return (
    <>
      {isLoggedIn ? (
        <>
          로그인이 된 경우
        </>
        // <MainRouter />  // 로그인이 된 경우
      ) : (
        // <AuthRouter />  // 로그인 필요
        <>
          로그인 필요
        </>
      )}
    </>
  );
}
