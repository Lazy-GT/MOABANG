
import React, { useEffect, useState } from "react";
import axios from 'axios';
import Swal from 'sweetalert2';
import CompareThemeList from './CompareThemeList';
import CompareSelList from './CompareSelList';
import "./CompareCss/CompareMain.css";
import "./CompareCss/CompareMain.css"

import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Legend } from "recharts";

const CompareMain = () => {
	const [theme, setTheme] = useState([]); //비교 테마 리스트 전체
	const [selCompare, setSelCompare] = useState([]); //비교할 테마 3개 정도
	const [compareRerender, setCompareRerender] = useState(false);//리렌더링을 위한 state
	const getThemeList = async () => {
		//테마 리스트 가져오기
		await axios.get(`/compare/list`,
			{
				headers: {
					'Authorization': localStorage.getItem("myToken")
				}
			}
		).then(res => {
			setTheme(res.data);
		}).catch(error => {
			console.error(error);
		});
	}

	useEffect(() => {
		getThemeList();

	}, [compareRerender]);

	//================================================
	//거리 구하기
	const [lat, setLat] = useState("");
	const [lon, setLon] = useState("");
	//좌표간 직선거리(Km) 함수
	function getDistanceFromLatLonInKm(lat1, lng1, lat2, lng2) {
		function deg2rad(deg) {
			return deg * (Math.PI / 180)
		}
		var R = 6371; 						// Radius of the earth in km 
		var dLat = deg2rad(lat2 - lat1); 	// deg2rad below 
		var dLon = deg2rad(lng2 - lng1);
		var a =
			Math.sin(dLat / 2) * Math.sin(dLat / 2) +
			Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
			Math.sin(dLon / 2) * Math.sin(dLon / 2);
		var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		var d = R * c; 						// Distance in km 
		return d;
	}
	//현재위치 좌표값 설정
	navigator.geolocation.getCurrentPosition((position) => {
		setLat(position.coords.latitude);
		setLon(position.coords.longitude);
		//km단위로 출력 
	})

	return (
		<div className='CompareMain-container'>
			<div className='CompareMain-selList'>
				{selCompare.map((item, index) => (
					<CompareSelList theme={item} key={index} selCompare={selCompare} setSelCompare={setSelCompare} />
				))}
			</div>
			<div className='chartAndList'>
				<div className='compareChart'>
					<BarChart
						width={1000}
						height={600}
						data={selCompare}
						margin={{ top: 5, right: 30, left: 20, bottom: 5 }}
					>
						<CartesianGrid strokeDasharray="3 3" />
						<XAxis dataKey="tname" />
						<YAxis />
						<Legend />
						<Bar name='평점' dataKey="grade" fill="#c09ee9" />
						<Bar name='난이도' dataKey="difficulty" fill="#20c7d8" />
						<Bar name='활동성' dataKey="activity" fill="#ffa9d1" />
						<div> Some text </div>
					</BarChart>

				</div>

				<div className='CompareMainThemeList'>
					<div id='text1'>비교하기({selCompare.length}/4)</div>
					{theme.map((item, index) => (
						<CompareThemeList theme={item} key={index} selCompare={selCompare} setSelCompare={setSelCompare} setCompareRerender={setCompareRerender} />
					))}
				</div>
			</div>
		</div>
	);
}

export default CompareMain;