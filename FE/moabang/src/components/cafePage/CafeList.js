import React, { useState } from "react"
import ModalMain from './Modal/ModalMain';
import CafeDetail from './CafeDatail';
import empty_img from '../../image/empty_img.png';
import "./CafeCSS/Cafe.css"

const CafeList = (props) => {
    const cafe = props.cafe;

    //Modal창 띄우기
    const [modalOpen, setModalOpen] = useState(false);
    const [modalData, setModalData] = useState(null);

    const openModal = () => {
        document.body.style.overflow = "hidden";
        setModalOpen(true);
    };
    const closeModal = () => {
        document.body.style.overflow = "unset";
        setModalOpen(false);
    };

    const onErrorImg = (e) => {
        e.target.src = empty_img;
    }

    return (
        <div className='cafedetailList' >
            {cafe.map((item, index) => (

                <div className='cafe-detail-container' key={index} onClick={() => {
                    openModal();
                    setModalData(item);
                }}>
                    <img className='cafeImg' alt='profile' src={item.img} onError={onErrorImg} />

                    <div className='cafeInfo'>
                        <div id='cafeName' >{item.cname}</div>
                        <div id='cafeAdd'>{item.location}</div>
                        <div id='cafeNumber'>{item.cphone}</div>
                    </div>

                </div>
            ))}

            <ModalMain open={modalOpen} close={closeModal} header="Modal heading">
                <CafeDetail cafe={modalData} />
            </ModalMain>




        </div>
    )

}

export default CafeList;

