import React from 'react';
import { Card, Button } from 'react-bootstrap';
import empty_img from '../../../../image/empty_img.png';
import './Myloc_Card.css';

const Myloc_Card = (props) => {

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

    const onErrorImg = (e) => {
        e.target.src = empty_img;
        e.target.className = "loc-card-img-default";
    }
    return (
        <Card className='loc-card'>
            <Card.Img className='loc-card-img' variant="top" src={props.data.img} onError={onErrorImg} />
            <hr class="foo" />
            <Card.Body className='card-body'>
                <Card.Title className='card-title'>{props.data.cname}</Card.Title>
                <Card.Text className='card-text'>
                    주소 : {props.data.location}<br />
                    전화번호 : {props.data.cphone ? props.data.cphone : "-"} <br />
                </Card.Text>
                <Button variant="primary" onClick={() => window.open(props.data.url, '_blank')}>바로가기</Button>
            </Card.Body>
            <Card.Footer className='card-footer'>
                <small className="text-muted">현재 위치에서 약 {parseInt(getDistanceFromLatLonInKm(props.cur_lat, props.cur_lon, props.data.lat, props.data.lon))}Km</small>
            </Card.Footer>
        </Card>
    );
};

export default Myloc_Card;