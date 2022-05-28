import axios from 'axios';

const { Kakao } = window;
/* https://developers.kakao.com/docs/latest/ko/kakaologin/js*/
export const API_HOST = process.env.REACT_APP_API_HOST;

const LoginWithKakao = () => {
  const scope = "profile_nickname,profile_image, account_email";


  Kakao.Auth.login({
    scope,
    // success는 인증 정보를 응답(response)으로 받는다. 
    success: function (response) {
      //카카오 SDK에 사용자 토큰을 설정한다.

      window.Kakao.Auth.setAccessToken(response.access_token);

      var ACCESS_TOKEN = window.Kakao.Auth.getAccessToken();


      axios.post('/user/klogin', {

      }, {
        headers: {
          token: ACCESS_TOKEN,
        }
      })
        .then((res) => {
          localStorage.setItem('myToken', res.headers.authorization)
          localStorage.setItem('username', res.data.nickname)
          localStorage.setItem('email', res.data.email)
          localStorage.setItem('p_img', res.data.p_img)
          document.location.href = '/'
        })
        .catch((error) => {
          alert("Error : 로그인을 다시 시도해주세요");
        });
    },
    fail: function (error) {
    },
  });
};

export default LoginWithKakao;