import React from 'react';
import Carousels from './Contents/Myloc_cafe/Carousels';
import PopularTheme from './Contents/Popular_theme/Popular_theme';
import Community from './Contents/Community/Community';
import { Row, Col } from 'react-bootstrap';
import './mainpage.css'

const mainpage = () => {

    return (
        <div className='mainpage'>
            <Row className='list_chart'>
                <Col sm={15}>

                    <Carousels />
                </Col>
            </Row>
            {/* <PopularTheme /> */}
            <Row className='list_chart'>
                <Col sm={7}>

                    <Community className='community' />
                </Col>
                <Col sm={4}>

                    <PopularTheme className='populartheme' />
                </Col>
            </Row>

        </div>
    );
};

export default mainpage;