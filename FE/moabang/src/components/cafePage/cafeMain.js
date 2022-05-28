import React, { useEffect, useState } from 'react';
import CafeSearchbar from './CafeSearchbar';
import axios from 'axios';

import "./CafeCSS/Cafe.css"



const CafeMain = () => {
    //카페 리스트를 url로 가져옴
    const [cafeData, setCafeData] = useState([]); //DB에서 받아온 데이터 저장
    const [cafeCount, setCafeCount] = useState(45); //cafe 총 개수
    async function getCafeData() {
        await axios.get('/cafe/list')
            .then(response => {
                setCafeData(response.data);
                setCafeCount(response.data.length);
            });
    }


    //페이지 네이션
    useEffect(() => {
        getCafeData();
    }, []);


    return (
        <div className='total'>
            <div >
                <CafeSearchbar searchItems={cafeData} totalcnt={cafeCount} />

            </div>
        </div>
    );
}
export default CafeMain;