import React from 'react';
import { Card, Badge } from 'react-bootstrap';
import './Content_3.css'

const Content_3 = (props) => {

    const goCommunityDetail = (e) => {
        window.location.href = `/board/detail/?rid=${props.data.rid}`;
    }
    return (
        <div>

            {props.data ? <Card >
                {props.data.playDate ?
                    <Card.Body>
                        <Badge bg="dark" className='review-badge'  >
                            {props.data.active}
                        </Badge>
                        <Card.Title className='review-title'>
                            {props.data.tname}
                        </Card.Title>

                        <Card.Subtitle className='review-subtitle'>{props.data.cname}</Card.Subtitle>
                        <Card.Text className='review-content'>{props.data.content}</Card.Text>
                        <Card.Footer className='card-footer'>
                            <small className="text-muted">{props.data.playDate}</small>
                        </Card.Footer>
                    </Card.Body>
                    :
                    <Card.Body>
                        <Badge className='community-badge'  >{props.data.header}</Badge>
                        <Card.Title className='community-title' onClick={goCommunityDetail}>
                            {props.data.title}
                        </Card.Title>

                        <Card.Text className='community-content'>{props.data.content}</Card.Text>
                        <Card.Footer className='card-footer'>
                            <small className="text-muted">
                                {"작성일 : "}
                                {props.data.createDate.substring(0, 10) + " "}
                                {props.data.createDate.substring(11, 19) + " "}
                            </small>
                        </Card.Footer>
                    </Card.Body>
                }
            </Card> :
                <Card.Body>
                    <Card.Title>찜한 테마가 없어요 ㅠㅠ</Card.Title>

                </Card.Body>
            }
        </div >
    );
};

export default Content_3;