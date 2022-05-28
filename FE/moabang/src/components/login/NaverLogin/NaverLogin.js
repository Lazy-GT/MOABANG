import { useEffect } from 'react';

const { naver } = window;

const NaverLogin = () => {
  const initializeNaverLogin = () => {
    const naverLogin = new naver.LoginWithNaverId({
      clientId: process.env.REACT_APP_NAVER_KEY,
      callbackUrl: process.env.REACT_APP_NAVER_CLLBACK_URL,
      isPopup: false,
      loginButton: { color: 'green', type: 3, height: '47' },
    });
    naverLogin.init();

  };



  useEffect(() => {
    initializeNaverLogin();
  }, []);

  return <div id="naverIdLogin"></div>;
}



export default NaverLogin;