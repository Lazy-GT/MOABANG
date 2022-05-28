import React, { useEffect, useState } from "react"
import ThemeList from './ThemeList';
import Paging from './Paging';
import { gerneBtn, memberCntBtn, sortBtn, typeBtn, diffBtn } from './FilterData';

import "./ThemeCSS/Theme.css"
import "./ThemeCSS/Paging.css"
import "./ThemeCSS/SearchBar.css";

const ThemeSearchbar = (props) => {
    const products = props.searchItems;
    const [searchValue, setSearchValue] = useState("");
    const [gerne, setGerne] = useState("");
    const [memberCnt, setMemberCnt] = useState(0);
    const [sortList, setSortList] = useState("이름순");
    const [themetype, setThemetype] = useState("");
    const [diff, setDiff] = useState(0);

    const handleInputChange = (event) => {
        setSearchValue(event.target.value)//입력된 텍스트 답아옴
    }

    const shouldDisplayButton = searchValue.length > 0;

    const handleInputClear = () => {//텍스트 초기화
        setSearchValue("")
    }



    //검색창 필터
    const filteredProducts = products.filter((res) => {
        return res.tname.includes(searchValue);//검색창에 입력된 내용과 비교하여 출력
    })
    //장르별 필터
    const resFilter = filteredProducts.filter((res) => {

        return res.genre.includes(gerne);
    });
    //인원별 필터
    const memberCntFilter = resFilter.filter((res) => {
        const min = parseInt(res.rplayer.substr(-3, 1));
        const max = parseInt(res.rplayer.substr(-1));
        if (memberCnt == 0) {
            return memberCnt >= 0;
        } else {
            const resTF = (min <= memberCnt) && (memberCnt <= max);
            return resTF
        }

    });
    //타입
    const typeFilter = memberCntFilter.filter((res) => {

        return res.type.includes(themetype);
    });

    const diffFilter = typeFilter.filter((res) => {
        if (diff == 0) {
            return true;
        } else {
            return res.difficulty == diff;
        }


    });
    //정렬
    const sortFilter = diffFilter.sort((a, b) => {

        if (sortList === "이름순") {
            let x = a.tname.toLowerCase();
            let y = b.tname.toLowerCase();
            if (x < y) {
                return -1;
            }
            if (x > y) {
                return 1;
            }
            return 0;
        } else if (sortList === "평점순") {

            return b.grade - a.grade;
        } else if (sortList === "인기순") {
            return b.count - a.count;
        }

    })


    //필터 체크 박스 영역 ---------------------------------
    //장르
    const handleGerne = (event) => {
        setPage(1);
        if (event.target.value === "전체")
            setGerne("");
        else
            setGerne(event.target.value);

        //클릭 시 색 변하게 하기 위한 코드
        const nameId = document.getElementsByClassName('filterBtn1');

        for (var i = 0; i < nameId.length; i++) {
            nameId[i].classList.remove("clicked");
        }

        event.target.classList.add("clicked");

    }

    //타입
    const handleType = (event) => {
        setPage(1);
        if (event.target.value === "전체")
            setThemetype("");
        else
            setThemetype(event.target.value);

        //클릭 시 색 변하게 하기 위한 코드
        const nameId = document.getElementsByClassName('filterBtn2');
        for (var i = 0; i < nameId.length; i++) {
            nameId[i].classList.remove("clicked");
        }

        event.target.classList.add("clicked");
    }
    //난이도
    const handleDiff = (event) => {
        setPage(1);
        setDiff(event.target.value);
        //클릭 시 색 변하게 하기 위한 코드
        const nameId = document.getElementsByClassName('filterBtn3');
        for (var i = 0; i < nameId.length; i++) {
            nameId[i].classList.remove("clicked");
        }

        event.target.classList.add("clicked");
    }
    //인원
    const handleMemberCnt = (event) => {
        setPage(1);
        setMemberCnt(event.target.value);
        //클릭 시 색 변하게 하기 위한 코드
        const nameId = document.getElementsByClassName('filterBtn4');
        for (var i = 0; i < nameId.length; i++) {
            nameId[i].classList.remove("clicked");
        }

        event.target.classList.add("clicked");
    }


    //정렬
    const handleSort = (event) => {
        setPage(1);
        setSortList(event.target.value);

        //클릭 시 색 변하게 하기 위한 코드
        const nameId = document.getElementsByClassName('filterBtn5');
        for (var i = 0; i < nameId.length; i++) {
            nameId[i].classList.remove("clicked");
        }

        event.target.classList.add("clicked");
    }


    //위로 체크 박스 영역-----------------------------
    //페이징 처리
    const [page, setPage] = useState(1);
    const [pageCnt, setPageCnt] = useState(45);

    const handlePageChange = (page) => {
        setPage(page);
    };



    const indexOfLast = page * 9;
    const indexOfFirst = indexOfLast - 9;
    function currentPosts(tmp) {
        let currentPosts = 0;
        currentPosts = tmp.slice(indexOfFirst, indexOfLast);
        return currentPosts;
    }

    useEffect(() => {
        setPageCnt((cnt) => cnt = sortFilter.length);

    }, [sortFilter]);



    return (
        <div className="searchBar">
            <div className='filter'>
                <div>
                    {shouldDisplayButton && <button className='button4' onClick={handleInputClear}>검색 초기화</button>}
                    <input type="text" className="search__input" value={searchValue} placeholder='검색어 입력' onChange={handleInputChange} />
                </div>
                <div className='locationGerne'><span id='filterText'>장르&nbsp;&nbsp;</span>
                {gerneBtn &&
                    gerneBtn.map((type, index) => (
                        <button className='filterBtn1' key={index} value={type.value} onClick={handleGerne}>
                            {type.name}
                        </button>
                    ))
                }
                </div>
                <div className='locationType'>
                    <span id='filterText'>타입&nbsp;&nbsp;</span>
                    {typeBtn &&
                        typeBtn.map((type, index) => (
                            <button className='filterBtn2' key={index} value={type.value} onClick={handleType}>
                                {type.name}
                            </button>
                        ))
                    }
                </div>
                <div className='locationDiff'>
                    <span id='filterText'>난이도&nbsp;&nbsp;</span>
                    {diffBtn &&
                        diffBtn.map((type, index) => (
                            <button className='filterBtn3' key={index} value={type.value} onClick={handleDiff}>
                                {type.name}
                            </button>
                        ))
                    }
                </div>
                <div className='locationNum'><span id='filterText'>인원&nbsp;&nbsp;</span>
                {memberCntBtn &&
                    memberCntBtn.map((type, index) => (
                        <button className='filterBtn4' key={index} value={type.value} onClick={handleMemberCnt}>
                            {type.name}
                        </button>
                    ))
                }
                </div>
                
                
                <div className='locationSort'><span id='filterText'>순서&nbsp;&nbsp;</span>
                {sortBtn &&
                    sortBtn.map((type, index) => (
                        <button className='filterBtn5' key={index} value={type.value} onClick={handleSort}>
                            {type.name}
                        </button>
                    ))
                }
                </div>

            </div>
            <br></br>
            <div>
                난이도
                <img id='DiffKey' src='https://www.emojiall.com/images/240/htc/1f511.png' alt='keyimg' />
                &nbsp;&nbsp;&nbsp;인원
                <img id='Conan' src='https://us.123rf.com/450wm/iconmama/iconmama1512/iconmama151200208/49892585-%EC%9D%B8%EA%B0%84-%EC%95%84%EC%9D%B4%EC%BD%98.jpg?ver=6' alt='conanMax' />
            </div>

            <div >
                <ThemeList Theme={currentPosts(sortFilter)} setTListRender={props.setTListRender} />

            </div>
            <br></br>
            <div>
                <Paging page={page} count={pageCnt} setPage={handlePageChange} />
            </div>
            <br></br>
        </div>

    )



}



export default ThemeSearchbar;