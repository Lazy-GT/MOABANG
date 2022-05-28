import React, { useState, useEffect } from 'react';
import Slider from "react-slick";
import './Carousels.css'
import { Card } from 'react-bootstrap';
import axios from 'axios';
import Myloc_Card from './Myloc_Card';
import left_arrow from '../../../../image/left-arrow.png'
import right_arrow from '../../../../image/right-arrow.png'
import run from '../../../../image/run.gif';

const Carousels = () => {

    const [cafeData, setCafeData] = useState([]);   //DB에서 받아온 데이터 저장
    const [sortData, setSortData] = useState([]);
    const [cafeCount, setCafeCount] = useState(45); //cafe 총 개수
    const [lat, setLat] = useState("");
    const [lon, setLon] = useState("");


    //카페 전체 데이터 배열에 저장
    function getCafeData() {

        axios.get('/cafe/list')
            .then((res) => {

                setCafeData(res.data);
                setCafeCount(res.data.length);
            })
            .catch((error) => {
                console.error(error);
                alert("error");
            });
    }

    //한번만 실행
    useEffect(() => {

        //현재위치 좌표값 설정
        navigator.geolocation.getCurrentPosition((position) => {
            setLat(position.coords.latitude);
            setLon(position.coords.longitude);
            //km단위로 출력 
        })
        getCafeData();
    }, []);

    //카페데이터가 저장되면 실행
    useEffect(() => {
        sort_cafe();
    }, [cafeData]);

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

    //slick carousel 설정
    const settings = {
        centerMode: true,
        centerPadding: "0px",
        focusOnSelect: true,
        dots: false,                // 스크롤바 아래 점으로 페이지네이션 여부
        infinite: true, 	        //무한 반복 옵션	 
        slidesToShow: 3,		    // 한 화면에 보여질 컨텐츠 개수
        slidesToScroll: 1,		    //스크롤 한번에 움직일 컨텐츠 개수
        speed: 500,	                // 다음 버튼 누르고 다음 화면 뜨는데까지 걸리는 시간(ms)
        arrows: true,
        draggable: false, 	        //드래그 가능 여부 
        autoplay: true,
        nextArrow: <SampleNextArrow />,
        prevArrow: <SamplePrevArrow />
    };

    //좌표간 직선거리(Km) 함수
    function getDistanceFromLatLonInKm(lat1, lng1, lat2, lng2) {
        function deg2rad(deg) {
            return deg * (Math.PI / 180)
        }
        var R = 6371;                       // Radius of the earth in km 
        var dLat = deg2rad(lat2 - lat1);    // deg2rad below 
        var dLon = deg2rad(lng2 - lng1);
        var a =
            Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2);
        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        var d = R * c;                      // Distance in km 
        return d;
    }

    //거리순으로 카페데이터 정렬 함수
    function sort_cafe() {
        setSortData(cafeData.sort(function (a, b) {
            return (getDistanceFromLatLonInKm(lat, lon, a.lat, a.lon)) - (getDistanceFromLatLonInKm(lat, lon, b.lat, b.lon));
        }))
    }

    return (
        <div className="carousels-container">

            <div className='my-loc'>
                <img className='run' src={run}></img>
                <p> 내 근처 방탈출 카페</p>
            </div>

            <Slider {...settings}>
                {sortData.slice(0, 6).map((data) => {
                    return <Myloc_Card data={data} key={data.cid} cur_lat={lat} cur_lon={lon} />;
                })}
            </Slider >
        </div >
    );
};

export default Carousels;