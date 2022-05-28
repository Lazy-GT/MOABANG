import React, { useEffect, useState } from 'react';
import './Community.css'
import { ListGroup } from 'react-bootstrap';
import List_item from '../Community/List_item';
import axios from 'axios';
import chat from '../../../../image/chat.gif';

const Community = () => {

    const Token = localStorage.getItem('myToken');
    const [community, setCommunity] = useState([]);

    //카페 전체 데이터 배열에 저장
    function getCommunityData() {

        axios.get('/community/read/list', {
            headers: {
                Authorization: Token
            }
        })
            .then((res) => {

                setCommunity(res.data);


            })
            .catch((error) => {
                console.error(error);
            });
    }

    //한번만 실행
    useEffect(() => {
        getCommunityData();
    }, []);

    //테마데이터가 저장되면 실행
    useEffect(() => {
        sort_CommunityData();
    }, [community]);

    function sort_CommunityData() {
        community.sort(function (a, b) {
            return b.rid - a.rid;
        })
    }

    return (
        <div className='new_list'>
            <div className='new_community'>
                <img className='chat' src={chat}></img>
                <p> 커뮤니티 새 글 </p>
                <a href='/board'>더보기</a>
            </div>
            <ListGroup as="ol" className='list_group'>
                {community.filter((data) => data.header == "공지").slice(0, 3).map((data) => {
                    return <List_item data={data} key={data.rid} />;
                })}
                {community.filter((data) => data.header != "공지").slice(0, 8).map((data) => {
                    return <List_item data={data} key={data.rid} />;
                })}
            </ListGroup>
        </div>
    );
};

export default Community;