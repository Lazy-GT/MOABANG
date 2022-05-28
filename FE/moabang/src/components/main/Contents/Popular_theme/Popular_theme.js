import React, { useState, useEffect } from 'react';
import Slider from "react-slick";
import { Card } from 'react-bootstrap';
import './Popular_theme.css'
import axios from 'axios';
import Theme_Card from './Theme_Card';
import left_arrow from '../../../../image/left-arrow.png'
import right_arrow from '../../../../image/right-arrow.png'
import fire from '../../../../image/fire.gif';

const Popular_theme = () => {

    const [themeData, setThemeData] = useState([]);   //DB에서 받아온 데이터 저장
    const [sortData, setSortData] = useState([]);
    const [themeCount, setThemeCount] = useState(45); //Theme 총 개수


    //카페 전체 데이터 배열에 저장
    function getThemeData() {

        axios.get('/cafe/theme/list')
            .then((res) => {

                setThemeData(res.data);
                setThemeCount(res.data.length);



            })
            .catch((error) => {
                console.error(error);
                alert("error");
            });
    }

    //한번만 실행
    useEffect(() => {
        getThemeData();
    }, []);

    //테마데이터가 저장되면 실행
    useEffect(() => {
        sort_theme();
    }, [themeData]);

    //좋아요순으로 테마데이터 정렬 함수
    function sort_theme() {
        themeData.sort(function (a, b) {
            return b.count - a.count;
        })
    }

    function SampleNextArrow(props) {
        const { className, style, onClick } = props;
        return (
            <img
                className={className}
                src={right_arrow}
                onClick={onClick}
            />
        );
    }

    function SamplePrevArrow(props) {
        const { className, style, onClick } = props;
        return (
            <img
                className={className}
                // style={{ ...style, display: "block", background: "green" }}
                src={left_arrow}
                onClick={onClick}
            />
        );
    }

    const settings = {
        dots: false,                // 스크롤바 아래 점으로 페이지네이션 여부
        infinite: true, 	        //무한 반복 옵션	 
        slidesToShow: 1,		    // 한 화면에 보여질 컨텐츠 개수
        slidesToScroll: 1,		    //스크롤 한번에 움직일 컨텐츠 개수
        speed: 500,	                // 다음 버튼 누르고 다음 화면 뜨는데까지 걸리는 시간(ms)
        arrows: true,
        draggable: false, 	//드래그 가능 여부 
        autoplay: true,
        nextArrow: <SampleNextArrow />,
        prevArrow: <SamplePrevArrow />
    };
    return (
        <div className="pop">
            <div className='pop-theme'>
                <img className='fire' src={fire}></img>
                <p> 인기 테마 </p>
            </div>
            <Slider {...settings}>
                {themeData.slice(0, 6).map((data) => {
                    return <Theme_Card data={data} key={data.cid} />;
                })}
            </Slider >
        </div>
    );
};

export default Popular_theme;