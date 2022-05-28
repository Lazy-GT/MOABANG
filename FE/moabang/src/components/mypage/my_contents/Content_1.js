import React, { useState } from 'react';
import { Card, Row, Col } from 'react-bootstrap';
import './Content_1.css'
import like_before from '../../../image/icon_like_before.png'
import like_after from '../../../image/icon_like_after.png'
import axios from 'axios';
import Swal from 'sweetalert2'

const Content_1 = (props) => {
    const [likeBtn, setLikeBtn] = useState(true);

    const Token = localStorage.getItem('myToken');
    const like_handler = () => {

        axios.get('/theme/' + props.data.tid + '/like', {
            headers: {
                Authorization: Token
            }
        })
            .then((res) => {
                //좋아요 취소 success
                //좋아요 success
                if (likeBtn) {
                    Swal.fire({
                        icon: 'error',
                        title: '찜을 취소했습니다',
                        showConfirmButton: false,
                        timer: 1000
                    })
                    setLikeBtn(false);
                } else {
                    Swal.fire({
                        icon: 'success',
                        title: '찜 했습니다',
                        showConfirmButton: false,
                        timer: 1000
                    })
                    setLikeBtn(true);
                }
            })
            .catch((error) => {
                console.error(error);
            });
    }
    return (
        <Col>
            {props.data ? <Card className='like_theme_card' >
                <Card.Img className='theme_img' variant="top" src={props.data.img} />
                <Card.Img
                    className='like_img'
                    variant="top"
                    src={likeBtn ? like_after : like_before}
                    onClick={like_handler} />
                <Card.Body>
                    <Card.Title>{props.data.tname}</Card.Title>
                    <Card.Subtitle>{props.data.cname}</Card.Subtitle>
                </Card.Body>

            </Card> :
                <Card.Body>
                    <Card.Title>찜한 테마가 없어요 ㅠㅠ</Card.Title>

                </Card.Body>
            }
        </Col >

    );
};

export default Content_1;