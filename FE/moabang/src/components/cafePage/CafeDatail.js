import { useState, useEffect } from 'react';
import axios from 'axios';
import Map from './Modal/Map';
import ModalList from './Modal/ModalList';


import "./CafeCSS/CafeDetail.css";
import "./Modal/ModalList.css"//열쇠 사진과 코난 사진 CSS만 가져온다.

const CafeDetail = (props) => {
    const cafe = props.cafe //카페 정보를 가져온다

    //카페이 있는 테마 정보를 가저온다.
    const [themeData, setThemeData] = useState([]); //DB에서 받아온 데이터 저장
    async function getThemeData() {
        axios.get(`/cafe/theme/${cafe.cid}`)
            .then(response => {
                setThemeData(response.data);
        });
    }
    const Content = () => {
        return (
            <div>
                난이도
                <img id='DiffKey' src='https://www.emojiall.com/images/240/htc/1f511.png' alt='keyimg' />
                &nbsp;&nbsp;&nbsp;인원
                <img id='Conan' src='https://us.123rf.com/450wm/iconmama/iconmama1512/iconmama151200208/49892585-%EC%9D%B8%EA%B0%84-%EC%95%84%EC%9D%B4%EC%BD%98.jpg?ver=6' alt='conanMax' />
            </div>
        )
    }
    

    //페이지 네이션
    useEffect(() => {
        getThemeData();
    }, []);

    return (
        <div className='DetailTotal'>
            <div className='DetailAndMap'>
                <div className='cafeDetailInfo'>
                    <div id='cafeDetailName'>{cafe.cname}</div>
                    <div id='cafeDetailAdd'>{cafe.location}</div>
                    <div id='cafeDetailNumber'>{cafe.cphone}</div>
                   
        
                </div>
                <div className='kakaoMap' >
                    <Map lat={cafe.lat} lon={cafe.lon}/>
                </div>
            </div>
            <br/><br/>
            <div id='ThemeWrite'>
                테마 
            </div>
                {Content()}
            <div>
                <ModalList Theme={themeData}/>
            </div>
            

        </div>
    )

}

export default CafeDetail;