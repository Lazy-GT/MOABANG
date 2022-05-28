import React from 'react';
import { Card, Row, Col } from 'react-bootstrap';
import './Content_1.css'
import like_before from '../../../image/icon_like_before.png'

const Content_2 = (props, index) => {
    return (
        <Col>
            {props.data ? <Card >
                <Card.Img className='theme_img' variant="top" src={props.data.img} />
                <Card.Body>
                    <Card.Title>{props.data.tname}</Card.Title>
                    <Card.Subtitle>{props.data.cname}</Card.Subtitle>
                    <br />
                    <Card.Text>인원 : {props.data.player} | 평점 : {props.data.rating}</Card.Text>
                    <Card.Footer className='card-footer'>
                        <small className="text-muted">{props.data.playDate}</small>
                    </Card.Footer>
                </Card.Body>
            </Card>
                :
                <Card >

                    <Card.Body>
                        <Card.Title>이용한 테마가 없어요 ㅠㅠ</Card.Title>

                    </Card.Body>
                </Card>
            }
        </Col >

    );
};

export default Content_2;