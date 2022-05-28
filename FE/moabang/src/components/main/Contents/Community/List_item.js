import React, { useEffect, useState } from 'react';
import { ListGroup, Badge } from 'react-bootstrap';
import './List_item.css'

const List_item = (props) => {

    const date = new Date();
    const years = date.getFullYear();
    const months = date.getMonth() + 1;
    const days = date.getDate();
    const hours = date.getHours();
    const minutes = date.getMinutes();
    const seconds = date.getSeconds();
    const doc_date = props.data.createDate;
    const doc_y = parseInt(doc_date.substring(0, 4));
    const doc_month = parseInt(doc_date.substring(5, 7));
    const doc_d = parseInt(doc_date.substring(8, 10));
    const doc_h = parseInt(doc_date.substring(11, 13));
    const doc_minute = parseInt(doc_date.substring(14, 16));
    const doc_s = parseInt(doc_date.substring(17, 19));



    function during_date() {
        //년 월 일이 동일하며
        if (years - doc_y == 0 && months - doc_month == 0 && days - doc_d == 0) {
            // 작성시간이 현재로부터 3시간 미만이면 new뱃지 생성
            if (hours - doc_h < 5) {
                return true;
            }
            return false;
        }
    }

    return (

        <ListGroup.Item
            as="li"
            className="list_group"
        >
            <div className="ms-2 me-auto">
                <div className="fw-bold">
                    <p className='header'>{props.data.header}</p>
                    <p className=' title'>{props.data.title}</p>
                    {
                        during_date() ?
                            <Badge className='new_badge' bg="primary" pill> new </Badge> :
                            <Badge className='new_badge' hidden="true"></Badge>
                    }
                </div>
            </div>
        </ListGroup.Item>
    );
};

export default List_item;