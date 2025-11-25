import { userApi } from '@/apis/user/user.api';
import { useAuthStore } from '@/stores/auth.store';
import React, { useEffect } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'

/**
 * 소셜 로그인 Redirect 후 AccessToken (파람으로 오는거) 저장
 * + 사용자 정보 조회
 * + 메인 페이지 (개인 설정) 이동
 */


// http://localhost:5173/oauth2/callback 경로로 리다이렉트되는 페이지
function OAuth2CallbackPage() {
  // 리액트 라우터 돔에서
  // URL의 쿼리 파라미터(accessToken 등)를 읽기 위한 훅 
  // => useSearchParams
  const [searchParams] = useSearchParams();

  // 페이지 이동용 훅
  const navigate = useNavigate();

  // Zustand store에서 액션 가져오기
  const setAccessToken = useAuthStore(state => state.setAccessToken);
  const setUser = useAuthStore(s => s.setUser);

  // 해당 컴포넌트 페이지 랜더링 시? 
  // : accessToken과 user 데이터 저장
  useEffect(() => {
    const handleOAuthCallback = async () => {
      // 1) 백엔드에서 redirect 시 함께 전달한 accessToken 읽어오기
      const accessToken = searchParams.get("accessToken");

      // 2) accessToken이 없으면? -> 로그인이 실패하게 됨
      if (!accessToken) {
        navigate("/login?error=oauth2");
        return;
      }

      try {
        // 3) AccessToken 저장
        setAccessToken(accessToken);

        // 4) 사용자 정보(/me)에 요청해 상태 동기화
        const {data} = await userApi.me();

        if(!data) {
          navigate("/login?error=oauth2_me");
          return;
        }
        setUser(data);

        navigate("/", {replace: true})

      } catch (e) {
        // me 조회 실패 시 
        navigate("/login?error=oauth2_me")
      }
    }

    handleOAuthCallback();
  }, [searchParams, navigate, setAccessToken, setUser]);

  return (

    <div style={{padding: 40}}>
      소셜 로그인 처리 중입니다.
    </div>
  )
}

export default OAuth2CallbackPage