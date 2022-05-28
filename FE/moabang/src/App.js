import {
  Routes,
  Route
} from 'react-router-dom';
import './App.css';
import { useEffect, useState } from 'react';
import Login from './components/login/loginpage';
import Home from './components/main/mainpage';
import Mypage from './components/mypage/Mypage';
// import Footer from './components/utils/footer/Footer';
import Navbar from './components/utils/nav/Navibar';
// import Error from './components/error/error';
import CafeMain from './components/cafePage/cafeMain';
import ThemeMain from './components/ThemePage/ThemeMain';
import CompareMain from './components/CompareTheme/CompareMain';
import BoardMain from './components/Board/BoardMain';
import BoardDetail from './components/Board/BoardDetail';
import Footer from "react-footer-comp";

import "./App.css";
function App() {

  const [ScrollY, setScrollY] = useState(0);
  const [BtnStatus, setBtnStatus] = useState(true); // 버튼 상태

  const handleFollow = () => {
    setScrollY(window.pageYOffset);
    if (ScrollY > 200) {
      // 200 이상이면 버튼이 보이게
      setBtnStatus(false);
    } else {
      // 200 이하면 버튼이 사라지게
      setBtnStatus(true);
    }
  }

  const moveToTop = () => (document.documentElement.scrollTop = 0);

  useEffect(() => {
    const watch = () => {
      window.addEventListener('scroll', handleFollow)
    }
    watch();
    return () => {
      window.removeEventListener('scroll', handleFollow)
    }
  })

  return (
    <div className="App">
      <Navbar />
      <Routes>
        <Route path="/home" element={<Home />} />
        <Route path="/mypage" element={<Mypage />} />
        <Route path="/" element={localStorage.getItem('myToken') ? <Home /> : <Login />} />
        <Route path="/" element={<Login />} />
        <Route path="/cafe" element={<CafeMain />} />
        <Route path="/theme" element={<ThemeMain />} />
        <Route path="/compare" element={<CompareMain />} />
        <Route path="/board" element={<BoardMain />} />
        <Route path="/board/detail" element={<BoardDetail />} />
        {/* <Route path="/*" element={<Error />} /> */}
      </Routes>
      <svg
        xmlns="http://www.w3.org/2000/svg"
        hidden={BtnStatus}
        className='top-button'
        onClick={moveToTop}
        width="40"
        height="40"
        fill="currentColor"
        viewBox="0 0 16 16">
        <path d="M2 16a2 2 0 0 1-2-2V2a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H2zm6.5-4.5V5.707l2.146 2.147a.5.5 0 0 0 .708-.708l-3-3a.5.5 0 0 0-.708 0l-3 3a.5.5 0 1 0 .708.708L7.5 5.707V11.5a.5.5 0 0 0 1 0z" />
      </svg>
      {/* <Footer
        copyrightIcon
        years={[2022]}
        height={150}
        bgColor={"#FFC7C7"}
        copyrightText
        copyrightIconStyle={{ color: "#8785A2", fontSize: 20, marginRight: 10 }}
        copyrightTextStyle={{ color: "#8785A2", fontSize: 20, marginRight: 10 }}
        textStyle={{ color: "#8785A2", fontSize: 16, marginRight: 10 }}
        text={"All rights reserved. SSAFY GUMI D205"}
      /> */}
    </div>
  );
}

export default App;
