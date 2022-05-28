
import axios from 'axios';
import { useState } from 'react'
import "./ReviewCSS/ReviewWrite.css"
import Swal from 'sweetalert2';
import { succAndfailBtn } from './ReviewData';

const ReviewWrite = ({ tid, setListRender }) => {
    const [active, setActive] = useState("");               //활동성
    const [diff, setDiff] = useState(-1);                   //체감 난이도
    const [clearTime, setClearTime] = useState("");         //클리어 시간
    const [cotent, setCotent] = useState("");               // 내용
    const [hint, setHint] = useState("");                   //사용 힌트 수
    const [isCleared, setIsCleared] = useState(-1);         //성공 여부
    const [joinNum, setJoinNum] = useState(-1);             //참가 인원
    const [star, setStar] = useState(-1);                   //평점
    const [recommendNum, setRecommendNum] = useState(-1);   //추천 인원


    const handleSetJoinNum = (event) => {
        setJoinNum(event.target.value)                      //입력된 텍스트 답아옴
    }

    const handleCleared = (event) => {
        setIsCleared(event.target.value)                    //입력된 텍스트 답아옴

        //클릭시 색 변경
        const nameId = document.getElementsByClassName('filterBtn5');
        for (var i = 0; i < nameId.length; i++) {
            nameId[i].classList.remove("clicked");
        }

        event.target.classList.add("clicked");


    }

    const handleSetStar = (event) => {

        setStar(event.target.value)             //입력된 텍스트 답아옴
    }
    const handleSetClearTime = (event) => {
        setClearTime(event.target.value)        //입력된 텍스트 답아옴
    }
    const handleSetActive = (event) => {
        setActive(event.target.value);          //입력된 텍스트 답아옴
    }
    const handleSetHint = (event) => {
        setHint(event.target.value);            //입력된 텍스트 답아옴
    }
    const handleRecommendNum = (event) => {
        setRecommendNum(event.target.value);    //입력된 텍스트 답아옴
    }
    const handleSetCotent = (event) => {
        setCotent(event.target.value);          //입력된 텍스트 답아옴
    }
    const handleDiff = (event) => {
        setDiff(event.target.value);
    }

    const handleClearContent = () => {
        //초기화
        setIsCleared("");
        setCotent("")
        setActive("");

        setClearTime("");
        setHint("");

        setJoinNum(-1);
        setStar(-1);
        setDiff(-1);
        setRecommendNum(-1);

        //제출시 버튼 초기화
        const nameId = document.getElementsByClassName('filterBtn5');
        for (var i = 0; i < nameId.length; i++) {
            nameId[i].classList.remove("clicked");
        }

    }

    const handleSubmit = () => {

        //빈칸 없는지 체크
        if (isCleared == "-1") {
            Swal.fire('성공 여부를 클릭해 주세요');
        } else if (clearTime == "") {
            Swal.fire('소요시간을 작성해 주세요.');
        }
        else if (diff == "-1") {
            Swal.fire('체감 난이도를 작성해 주세요.');
        }
        else if (hint == "") {
            Swal.fire('힌트 수를 작성해 주세요.');
        }
        else if (joinNum == "-1") {
            Swal.fire('참여 인원을 작성해 주세요.');
        }
        else if (star == "-1") {
            Swal.fire('평점을 작성해 주세요.');
        }
        else if (active == "-1") {
            Swal.fire('활동성을  작성해 주세요.');
        }
        else if (recommendNum == "-1") {
            Swal.fire('추천인원을  작성해 주세요.');
        }
        else if (cotent == "") {
            Swal.fire('내용을 작성해 주세요.');
        }
        else {
            //axios로 결과를 쏴주고
            let today = new Date();
            let year = today.getFullYear();
            let month = today.getMonth() + 1;
            let date = today.getDate(); // 일

            //API 날짜 양식에 맞춰주는 코드 (YYYY-MM-DD)
            let resM = "";
            if (Math.floor(month / 10) === 0) {
                resM = '0' + month;
            } else {
                resM = month;
            }

            let resD = "";
            if (Math.floor(date / 10) === 0) {
                resD = '0' + date;
            } else {
                resD = date;
            }
            const timeNow = year + '-' + resM + '-' + resD;

            axios.post("/theme/review/create", {
                active: active,
                chaegamDif: diff,
                clearTime: clearTime,
                content: cotent,
                hint: hint,
                isSuccess: isCleared,
                playDate: timeNow,
                player: joinNum,
                rating: star,
                recPlayer: recommendNum,
                tid: tid

            }, {
                headers: {
                    'Authorization': localStorage.getItem("myToken")
                }
            }).then(response => {
                setListRender(e => !e);
            }).catch(error => {
                console.error(error);
            });


            setIsCleared(-1);
            setCotent("")
            setActive("");
            setDiff(-1);

            setClearTime("");
            setHint("");

            setJoinNum(-1);
            setStar(-1);

            setRecommendNum(-1);

            //제출시 버튼 초기화
            const nameId = document.getElementsByClassName('filterBtn5');
            for (var i = 0; i < nameId.length; i++) {
                nameId[i].classList.remove("clicked");
            }

        }
    }

    return (
        <div >
            <details className='toggle-container'>
                <summary >
                    <img id='reviewImg' src='https://cdn-icons-png.flaticon.com/512/651/651191.png' alt='reviewImg' />
                </summary>

                <div><span id='ReviewIsCleared'>탈출 </span>
                    {succAndfailBtn &&
                        succAndfailBtn.map((type, index) => (
                            <button className='filterBtn5' key={index} value={type.value} onClick={handleCleared}>
                                {type.name}
                            </button>
                        ))
                    }
                    &nbsp; &nbsp; &nbsp; &nbsp;
                    <span id='ReviewClearTime'>소요시간<input id='input2' type={"number"} onChange={handleSetClearTime} value={clearTime} />분</span>
                    &nbsp; &nbsp; &nbsp; &nbsp;
                    <span id='ReviewHint'>사용 힌트 수<input id='input2' type={"number"} onChange={handleSetHint} value={hint} />개</span>
                    &nbsp; &nbsp;
                    <span id='ReviewJoinDiff'>
                        체감 난이도:
                        <select onChange={handleDiff} value={diff}>
                            <option value="-1">선택</option>
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                            <option value="5">5</option>
                        </select>명
                    </span>

                </div>
                <div>
                    <span id='ReviewJoinNum'>
                        참가 인원:
                        <select onChange={handleSetJoinNum} value={joinNum}>
                            <option value="-1">선택</option>
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                            <option value="5">5</option>
                            <option value="6">6</option>
                            <option value="7">7</option>
                            <option value="8">8</option>
                        </select>명
                    </span>
                    &nbsp; &nbsp; &nbsp; &nbsp;
                    <span id='ReviewStar'>
                        평점

                        <select onChange={handleSetStar} value={star}>
                            <option value="-1">선택</option>
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                            <option value="5">5</option>
                            <option value="6">6</option>
                            <option value="7">7</option>
                            <option value="8">8</option>
                            <option value="9">9</option>
                            <option value="10">10</option>
                        </select>점

                    </span>
                    &nbsp; &nbsp; &nbsp; &nbsp;
                    <span id='ReviewActive'>
                        활동성
                        <select onChange={handleSetActive} value={active}>
                            <option value="-1">선택</option>
                            <option value="적음">적음</option>
                            <option value="보통">보통</option>
                            <option value="많음">많음</option>
                        </select>

                    </span>
                    &nbsp; &nbsp; &nbsp; &nbsp;
                    <span id='ReviewRecommendNum'>
                        추천 인원
                        <select onChange={handleRecommendNum} value={recommendNum}>
                            <option value="-1">선택</option>
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                            <option value="5">5</option>
                            <option value="6">6</option>
                            <option value="7">7</option>
                            <option value="8">8</option>
                        </select>
                        명
                    </span>
                </div>
                <div>

                </div>
                <div id='ReviewCotent'>
                    <textarea id='inputContent' placeholder='내용을 입력해 주세요' onChange={handleSetCotent} value={cotent}>
                    </textarea>
                </div>

                <button className='writeSumitBtn' onClick={handleClearContent}>초기화</button>
                <button className='writeSumitBtn' onClick={handleSubmit}>제출</button>

            </details>
        </div>
    )
}

export default ReviewWrite;