import React, { useEffect, useState } from 'react';
import './Mypage.css'
import { Card, Tab, Row, Nav, Col, Tabs } from 'react-bootstrap';
import Content_1 from '../mypage/my_contents/Content_1'
import Content_2 from '../mypage/my_contents/Content_2'
import Content_3 from '../mypage/my_contents/Content_3'
import axios from 'axios';

import Paging from '../mypage/my_contents/Contents_1_Paging'


const Mypage = () => {

    const Token = localStorage.getItem('myToken');
    const [myReview, setMyReview] = useState([]);
    const [reviewList, setReviewList] = useState([]);
    const [myLike, setMyLike] = useState([]);
    const [myLikeCnt, setMyLikeCnt] = useState(0);
    const [myCommunity, setMyCommunity] = useState([]);

    //페이징 처리
    const [page_1, setPage_1] = useState(1);
    const [page_2, setPage_2] = useState(1);
    const [page_3, setPage_3] = useState(1);
    const [pageCnt_1, setPageCnt_1] = useState(45);
    const [pageCnt_2, setPageCnt_2] = useState(45);
    const [pageCnt_3, setPageCnt_3] = useState(45);

    const handlePageChange_1 = (page_1) => {
        setPage_1(page_1);
    };
    const handlePageChange_2 = (page_2) => {
        setPage_2(page_2);
    };
    const handlePageChange_3 = (page_3) => {
        setPage_3(page_3);
    };



    //유저가 이용한 테마 데이터 배열에 저장
    function getReviewData() {

        axios.get('/mypage/theme/review', {
            headers: {
                Authorization: Token
            }
        })
            .then((res) => {

                setMyReview(res.data);

            })
            .catch((error) => {
                console.error(error);
            });
    }
    //유저가 찜한 테마 데이터 배열에 저장
    function getlikeData() {

        axios.get('/mypage/theme/list', {
            headers: {
                Authorization: Token
            }
        })
            .then((res) => {

                setMyLike(res.data);
                setMyLikeCnt(res.data.length);

            })
            .catch((error) => {
                console.error(error);
            });
    }

    //유저가 작성한 글 데이터 배열에 저장
    function getCommunityData() {

        axios.get('/mypage/reviewcommunity', {
            headers: {
                Authorization: Token
            }
        })
            .then((res) => {

                setMyCommunity(res.data.communityList);
                setReviewList(res.data.reviewList);

            })
            .catch((error) => {
            });
    }

    //한번만 실행
    useEffect(() => {
        getReviewData();
        getlikeData();
        getCommunityData();
    }, []);

    useEffect(() => {
        setPageCnt_1((cnt) => cnt = myLike.length);

    }, [myLike]);
    useEffect(() => {
        setPageCnt_2((cnt) => cnt = myReview.length);

    }, [myReview]);
    useEffect(() => {
        setPageCnt_3((cnt) => cnt = myCommunity.length);

    }, [myCommunity]);

    const indexOfLast_1 = page_1 * 6;
    const indexOfLast_2 = page_2 * 6;
    const indexOfLast_3 = page_3 * 6;
    const indexOfFirst_1 = indexOfLast_1 - 6;
    const indexOfFirst_2 = indexOfLast_2 - 6;
    const indexOfFirst_3 = indexOfLast_3 - 6;
    function currentPosts_1(tmp) {
        let currentPosts_1 = 0;
        currentPosts_1 = tmp.slice(indexOfFirst_1, indexOfLast_1);
        return currentPosts_1;
    }
    function currentPosts_2(tmp) {
        let currentPosts_2 = 0;
        currentPosts_2 = tmp.slice(indexOfFirst_2, indexOfLast_2);
        return currentPosts_2;
    }
    function currentPosts_3(tmp) {
        let currentPosts_3 = 0;
        currentPosts_3 = tmp.slice(indexOfFirst_3, indexOfLast_3);
        return currentPosts_3;
    }


    return (
        <div className='my_page'>

            <Tab.Container id="left-tabs-example" defaultActiveKey="first">
                <Row>
                    <Col sm={4}>
                        <Card.Body className='profile-card-body'>
                            <img src={localStorage.getItem('p_img')} alt="Avatar" className='profile-card-img' />
                            <Card.Title className='profile-card-username'>{localStorage.getItem('username')}</Card.Title>
                            <Card.Text className='profile-card-email'>
                                {localStorage.getItem('email')}
                            </Card.Text>
                        </Card.Body>
                        <Nav variant="tabs" className="flex-column">
                            <Nav.Item className='profile-tab-item-1'>
                                <Nav.Link className='profile-tab-item-1-text' eventKey="first">
                                    찜한 테마
                                </Nav.Link>
                            </Nav.Item>
                            <Nav.Item className='profile-tab-item-2'>
                                <Nav.Link className='profile-tab-item-2-text' eventKey="second">
                                    이용한 테마
                                </Nav.Link>
                            </Nav.Item>
                            <Nav.Item className='profile-tab-item-3'>
                                <Nav.Link className='profile-tab-item-3-text' eventKey="third">
                                    작성글 관리
                                </Nav.Link>
                            </Nav.Item>
                        </Nav>
                    </Col>
                    <Col sm={7}>
                        <Tab.Content>
                            <Tab.Pane eventKey="first">
                                <Card className='content-card' >
                                    <Card.Body className='content-card-1-body'>

                                        <Card.Title className='content-card-1-title'>찜한 테마</Card.Title>

                                        <Row xs={1} md={3}>

                                            {currentPosts_1(myLike).map((data) => {
                                                return <Content_1 data={data} key={data.cid} />
                                            })}
                                        </Row>
                                    </Card.Body>

                                    <Paging page={page_1} count={pageCnt_1} setPage={handlePageChange_1} />
                                </Card>
                            </Tab.Pane>
                            <Tab.Pane eventKey="second">
                                <Card className='content-card' >
                                    <Card.Body className='content-card-2-body'>

                                        <Card.Title className='content-card-2-title'>이용한 테마</Card.Title>
                                        <Card>
                                            <Card.Body>
                                                <Row xs={1} md={3}>

                                                    {currentPosts_2(myReview).map((data) => {
                                                        return <Content_2 data={data} key={data.cid} />
                                                    })}
                                                </Row>
                                            </Card.Body>
                                            <Paging page={page_2} count={pageCnt_2} setPage={handlePageChange_2} />
                                        </Card>
                                    </Card.Body>
                                </Card>
                            </Tab.Pane>
                            <Tab.Pane eventKey="third">
                                <Card className='content-card' >
                                    <Tabs
                                        defaultActiveKey="community"
                                        transition={false}
                                        id="noanim-tab-example"
                                        className="mb-3"
                                    >
                                        <Tab eventKey="community" title="작성 글">
                                            {currentPosts_3(myCommunity).map((data) => {
                                                return <Content_3 data={data} key={data.rid} />
                                            })}
                                            <Paging page={page_3} count={pageCnt_3} setPage={handlePageChange_3} />

                                        </Tab>
                                        <Tab eventKey="review" title="작성 리뷰">
                                            {currentPosts_3(reviewList).map((data) => {
                                                return <Content_3 data={data} key={data.rid} />
                                            })}
                                            <Paging page={page_3} count={pageCnt_3} setPage={handlePageChange_3} />

                                        </Tab>
                                    </Tabs>

                                </Card>
                            </Tab.Pane>
                        </Tab.Content>
                    </Col>
                </Row>
            </Tab.Container >

        </div >
    );
};

export default Mypage;