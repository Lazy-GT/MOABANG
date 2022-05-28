import { useState } from 'react';
import axios from 'axios';
import Swal from 'sweetalert2';
import "./BoardCSS/BoardWrite.css";

const BoardWrite = ({ setShowList }) => {
    const [header, setHeader] = useState("-1");
    const [content, setContent] = useState("");
    const [title, setTitle] = useState("");
    const [titleCnt, setTitleCnt] = useState(0);
    const [contentCnt, setContentCnt] = useState(0);
    
    const HeaderSelectHandler = (event) => {
        setHeader(event.target.value);
    }
    const ContentHandler = (event) => {
        setContent(event.target.value);
        setContentCnt(event.target.value.length);
    }
    const TitleHandler = (event) => {
        setTitleCnt(event.target.value.length)
        setTitle(event.target.value)
    }
    const WriteCancelBtn = () => {
        Swal.fire({
            title: '게시글 등록을 취소 하시겠습니까?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '예',
            cancelButtonText:'아니요',
        }).then((result) => {
            if (result.isConfirmed) {
                setShowList(e => !e);
                
            }
                
        })
    }

    const WriteSummitBtn = () => {
        
        //서버로 작성 내용 보내준다
        if (title == "") {
            Swal.fire('제목이 없습니다');
        } else if (header == "-1") {
            Swal.fire('카테고리 선택');
        }
        else if (content == "") {
            Swal.fire('내용을 입력해 주세요');
        } else {
            axios.post("/community/write", {
                content: content,
                header: header,
                title: title,
            
            }, {
                headers: {
                    'Authorization': localStorage.getItem("myToken")
                }
            }).then(res => {
                Swal.fire({
                    icon: 'success',
                    title: "작성 성공."
                })
                setShowList(e => !e);
            }).catch(error => {
                Swal.fire({
                    icon: 'fail',
                    title: "작성 실패."
                })
                console.error(error);
            });
            
        }
        //작성 클릭 시 리스트로 넘어감
        

    }
    const goCommunity = () => {
        window.location.href = `/board`;
    }
    return (
        <div className='Board-Write-Main'>
            <div id='Board-Write-Main-Title' onClick={goCommunity}>커뮤니티</div>
            <div className='boardWrite-container'>
                <div className='boardTitle'>
                    <span id='boardWriteText'>제목<span id='TextStar'>*</span></span> 
                    <input className='inputTitle' maxLength='50' placeholder='제목을 입력해주세요' type="text" onChange={TitleHandler} value={title}></input>
                    <span>{titleCnt}/50</span>
                </div>
                <div className='boardHeader'>
                    <span id='boardWriteText'>게시판<span id='TextStar'>*</span></span>
                    <select className='BoardWriteinputSelect' onChange={HeaderSelectHandler} value={header}>
                        <option value="-1">선택</option>
                        <option value="자유">자유</option>
                        <option value="구인">구인</option>
                    </select>
                </div>
                <div className='boardContent'>
                    <span id='boardWriteText'>내용<span id='TextStar'>*</span></span> 
                    <textarea className='inputContent' maxLength='200' type="text" placeholder='내용을 입력해 주세요'  value={content} onChange={ContentHandler}></textarea>
                    <span>{contentCnt}/200</span>
                </div>
                <button className='boardWriteBtn '  onClick={WriteSummitBtn}>확인</button>
                <button className='boardCancelBtn' onClick={WriteCancelBtn}>취소</button>
            </div>
        </div>
    );


}

export default BoardWrite;