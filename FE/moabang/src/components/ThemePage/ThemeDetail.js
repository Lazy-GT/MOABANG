
import "./ThemeCSS/ThemeDetail.css";

import ReviewList from '../Review/ReviewList';
import ReviewWrite from '../Review/ReviewWrite';
import { useState, useEffect } from 'react';
import axios from 'axios';
import Swal from 'sweetalert2';

const ThemeDetail = (props) => {
    //리뷰 더미 데이터
    const [listRender, setListRender] = useState(true);//리뷰의 상태가 변하면 리랜더링을 하기 위한 state
    const [Theme, setTheme] = useState(props.Theme);
    const [likeCheck, setLikeCheck] = useState(false);//좋아요 true false 인지 저장하기 윈한 state
    const [isLikes, setIsLike] = useState(false);//좋아요 변화를 감지해서 리랜더링을 하기 위한 state
    const [reviewRating, setReviewRating] = useState([]);


    //비교하기 리스트에 테마 추가
    const addCompareList = () => {
        axios.get(`/compare/${props.Theme.tid}`,
            {
                headers: {
                    'Authorization': localStorage.getItem("myToken")
                }
            }
        ).then(res => {
            Swal.fire({
                icon: 'success',
                title: res.data
            })
        }).catch(error => {
            console.error(error);
        });

    }

    //테마 리스트를 가져온다.
    const getThemeList = () => {
        axios.get(`/cafe/theme/detail/${props.Theme.tid}`,
            {
                headers: {
                    'Authorization': localStorage.getItem("myToken")
                }
            }
        ).then(res => {
            setTheme(res.data.themeDetailDTO);
            setLikeCheck(res.data.islike);
        }).catch(error => {
            console.error(error);
        });
    }

    useEffect(() => {
        getThemeList();
    }, [isLikes]);

    //리뷰 평균을 가져온다.
    const getThemeRating = () => {
        axios.get(`/theme/review/rate/${props.Theme.tid}`
        ).then(res => {
            setReviewRating(res.data);
        }).catch(error => {
            console.error(error);
        });
    }
    useEffect(() => {
        getThemeRating();
    }, []);

    //난이도별 열쇠 이미지 개수 맞추기
    const DifficultyKeyImg = (diff) => {
        const result = [];
        for (let i = 0; i < diff; i++) {
            result.push(<img key={i} id='DiffKey' src='https://www.emojiall.com/images/240/htc/1f511.png' alt='keyimg' />)
        }
        return result;

    }
    //사람 개수 최소
    const ConanMin = (min) => {
        const num = parseInt(min.substr(-3, 1));
        const result = [];
        for (let i = 0; i < num; i++) {
            result.push(<img key={i} id='Conan' src='https://us.123rf.com/450wm/iconmama/iconmama1512/iconmama151200208/49892585-%EC%9D%B8%EA%B0%84-%EC%95%84%EC%9D%B4%EC%BD%98.jpg?ver=6' alt='conanMin' />)
        }
        return result;

    }
    //사람 개수 최대
    const ConanMax = (max) => {
        const num = parseInt(max.substr(-1));
        const result = [];
        for (let i = 0; i < num; i++) {
            result.push(<img key={i} id='Conan' src='https://us.123rf.com/450wm/iconmama/iconmama1512/iconmama151200208/49892585-%EC%9D%B8%EA%B0%84-%EC%95%84%EC%9D%B4%EC%BD%98.jpg?ver=6' alt='conanMax' />)
        }
        return result;
    }
    //물결표
    const Water = () => {
        return <img id='water' src='https://upload.wikimedia.org/wikipedia/commons/thumb/4/42/Tilde.svg/1200px-Tilde.svg.png' alt="water" ></img>
    }
    //별점 이미지
    const starScore = () => {
        return <img id='theme-detail-starscore' src='https://emojigraph.org/media/facebook/star_2b50.png' alt='starscore'></img>
    }


    const heartChange = (event) => {
        //좋아요 버튼
        axios.get(`/theme/${event.target.alt}/like/`,
            {
                headers: {
                    'Authorization': localStorage.getItem("myToken")
                }
            }
        ).then(response => {
            props.setTListRender(e => !e);
            setIsLike(e => e = !e);
            if (response.data === "좋아요 success") {
                Swal.fire({
                    icon: 'success',
                    title: '찜 성공'
                })
            } else {
                Swal.fire({
                    icon: 'success',
                    title: '찜 취소'
                })
            }
            
        }).catch(error => {
            console.error(error);
        });




    }
    function roundToTwo(num) {
        return +(Math.round(num + "e+1") + "e-1");
    }



    return (
        <div className='ThemeDetailTotal'>

            <div className='ThemeDetailImgAndInfo'>
                <div className="heartDetailImg">
                    {likeCheck ?
                        <img className="Detailheart " onClick={heartChange} alt={Theme.tid} src='https://mblogthumb-phinf.pstatic.net/20140709_176/wsm0030_1404859139443xgQQv_PNG/PicsArt_1404831829726.png?type=w800' /> :
                        <img className="Detailheart " onClick={heartChange} alt={Theme.tid} src='https://mblogthumb-phinf.pstatic.net/20140709_15/wsm0030_1404859141585ixxmQ_PNG/1404859141390_PicsArt_1404833054881.png?type=w2' />

                    }
                    
                </div>

                <img className='TDetailImg' alt='profile' src={Theme.img} />



                <div className='ThemeDetailInfo'>
                    <div id='ThemeDetailName' >{Theme.tname}</div>
                    <div id='ThemeDetailType'>{Theme.type}</div>
                    <div id='ThemeDetailGenre'>{Theme.genre}</div>
                    <div id='ThemeDetailRplayer'>{ConanMin(Theme.rplayer)}
                        {Water()}{ConanMax(Theme.rplayer)}</div>
                    <div id='ThemeDetailTime'>{Theme.time}</div>
                    <div id='ThemeDetailListDiff'>{DifficultyKeyImg(Theme.difficulty)}</div>
                    <div id='ThemeDetailListGrade'>{starScore()}&nbsp;{Theme.grade}</div>
                </div>
                <div className='ThemeTotalReview'>
                    <div id='ThemeRatingTitle'>리뷰 통계</div>
                    <img id='ThemeRatingImg' src='https://emojigraph.org/media/facebook/star_2b50.png' alt='starscore'></img>
                    <div id='ThemeRatingStar'>x {roundToTwo(reviewRating.r_rating)}</div>
                    <div id='ThemeRatingDiff'><span id='ThemeRatingText'>체감 난이도:</span>&nbsp;{reviewRating.r_chaegamDif}</div>
                    <div id='ThemeRatingActive'><span id='ThemeRatingText'>체감 활동성:</span>&nbsp;{reviewRating.r_activity}</div>
                    <div id='ThemeRatingRecNum'><span id='ThemeRatingText'>추천 인원:</span>&nbsp;{reviewRating.r_recPlayer}명</div>
                    <div id='ThemeRatingClear'><span id='ThemeRatingText'>탈출 성공률: </span>{roundToTwo((reviewRating.r_isSuccess * 100))}%</div>
                    <div id='ThemeRatingTime'><span id='ThemeRatingText'>클리어 타임: </span>{reviewRating.r_clearTime}분</div>
                    <div id='ThemeRatingHint'><span id='ThemeRatingText'>사용 힌트수:</span> {roundToTwo(reviewRating.r_hint)}개</div>
                </div>

            </div>
            <div className='ThemeDetailNav'>
                <a id='homepageMove' href={Theme.url} target='_blank' >홈페이지 이동</a>
                <span id='bar1'>|</span>
                <a id='compareMove' href='/compare'>비교하기 페이지 이동</a>
                <span id='bar2'>|</span>
                <span id='compareBtn' onClick={addCompareList}> 비교 리스트 추가</span>
            </div>
            <ReviewWrite tid={Theme.tid} setListRender={setListRender} />
            <div>
                <ReviewList tid={Theme.tid} listRender={listRender} setListRender={setListRender} />
            </div>


        </div>
    )

}

export default ThemeDetail;