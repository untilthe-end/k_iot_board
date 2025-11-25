import { Link, Route, Routes } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import { useAuthStore } from "./stores/auth.store";
import RegisterPage from "./pages/RegisterPage";
import OAuth2CallbackPage from "./pages/OAuth2CallbackPage";

export default function App() {
  const { isInitialized, accessToken, user } = useAuthStore();

  if (!isInitialized) return <div>Loading...</div>;

  const isLoggedIn = Boolean(accessToken && user);

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
          <Link to="/login">로그인</Link>
          <Routes>
            <Route path="/login" element={<LoginPage />}/>
            <Route path="/login" element={<LoginPage />}/>

            {/* OAuth2 소셜 로그인 콜백 */}
            <Route path="/oauth2/callback" element={<OAuth2CallbackPage />}/>
          </Routes>
          
        </>
      )}
    </>
  );
}
