import React, { useEffect, useState } from 'react';
import ThemeSearchbar from './ThemeSearchbar';
import axios from 'axios';

import "./ThemeCSS/Theme.css"
/*매장명, 테마명,  장르, 난이도, 시간,인원, 평정, 유형,  사진, 홈페이지, 예약페이지*/



const ThemeMain = () => {
    //카페 리스트를 url로 가져옴
    const [themeData, setthemeData] = useState([]); //DB에서 받아온 데이터 저장
    const [ThemeCount, setThemeCount] = useState(1682); //Theme 총 개수

    const [tListRender, setTListRender] = useState(false);

    async function getThemeData() {
        await axios.get('/cafe/theme/list',
            {
                headers: {
                    'Authorization': localStorage.getItem("myToken")
                }
            }
        ).then(response => {
            setthemeData(response.data);
            setThemeCount(response.data.length);
        });

    }

    useEffect(() => {
        getThemeData();
    }, [tListRender]);

    return (
        <div className='total'>
            <div >
                <ThemeSearchbar searchItems={themeData} totalcnt={ThemeCount} setTListRender={setTListRender} />

            </div>
        </div>
    );
}
export default ThemeMain;