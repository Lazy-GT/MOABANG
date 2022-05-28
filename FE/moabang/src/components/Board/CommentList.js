
import "./BoardCSS/CommentList.css";
import Paging from './CommentPaging';
import { useState, useEffect } from 'react';
import axios from 'axios';
import Swal from 'sweetalert2';

const CommentList = ({ commentList, setCheckComment }) => {
    const [page, setPage] = useState(1);
    const [pageCnt, setPageCnt] = useState(45);

    const handlePageChange = (page) => {
        setPage(page);

    };
    const indexOfLast = page * 5;
    const indexOfFirst = indexOfLast - 5;
    function currentPosts(tmp) {
        let currentPosts = 0;
        currentPosts = tmp.slice(indexOfFirst, indexOfLast);
        return currentPosts;
    }

    useEffect(() => {
        setPageCnt((cnt) => cnt = commentList.length);

    }, [commentList]);

    //댓글 삭제
    const CommentDeleteBtn = (e) => {
        Swal.fire({
            title: '댓글을 삭제 하시겠습니까?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '예',
            cancelButtonText: '아니요',
        }).then((result) => {
            if (result.isConfirmed) {
                axios.delete(`/community/comment/delete/${e.target.value}`, {
                    headers: {
                        'Authorization': localStorage.getItem("myToken")
                    }
                }).then(res => {

                    setCheckComment(e => e = !e);

                })

            }

        })

    }
    //댓글 신고하기
    const ReportComment = (cid) => {
        const Report = Swal.fire({
            input: 'textarea',
            inputLabel: '신고',
            inputPlaceholder: '신고 사유를 입력해 주세요',
            inputAttributes: {
                'aria-label': 'Type your message here'
            },
            showCancelButton: true,
            confirmButtonText: '확인',
            cancelButtonText: '취소',
        }).then((res) => {
            if (res.isConfirmed) {
                axios.post('/report/create',
                    {
                        category: 2,
                        reason: res.value,
                        target_id: cid.target.value,

                    },
                    {
                        headers: {
                            'Authorization': localStorage.getItem("myToken")
                        }
                    }
                ).then((response) => {
                    if (response.data) {
                        Swal.fire({
                            icon: 'success',
                            title: "신고 되었습니다."
                        })
                    } else {
                        Swal.fire({
                            icon: 'success',
                            title: "이미 신고 하였습니다.."
                        })
                    }

                })
            }

        })


    }
    //프로필 유무에 따른 이미지
    const ShowImg = ({ profile }) => {

        if (profile === null) {
            return <img id='CommentImg1' src="https://cdn.discordapp.com/attachments/963307192025485326/975564975617744946/unknown.png" alt='profile'  ></img>
        } else {
            return <img id='CommentImg2' src={profile} alt='profile'  ></img>
        }
    }


    return (
        <div className='Comment-Container'>
            {
                currentPosts(commentList).map((item, index) => {
                    if (item.reportCnt >= 1) {

                        return (
                            <div className='Comment' key={index}>
                                <div>이 댓글은 블라인드 처리 되었습니다</div>
                                <hr id='hrSize'></hr>
                            </div>
                        )
                    } else {
                        return (
                            <div className='Comment' key={index}>
                                <ShowImg profile={item.userProfile} />
                                <span id='CommentUserName'>{item.userName}</span>
                                <span id='CommentDate'>{item.regDate}</span>
                                {
                                    localStorage.getItem('email') === item.email ?
                                        <button id='CommentDeleteBtn' value={item.coid} onClick={CommentDeleteBtn}>삭제</button>
                                        :
                                        <span></span>
                                }
                                {
                                    localStorage.getItem('email') === item.email ?
                                        <span></span>
                                        :
                                        <button id='CommentReport' value={item.coid} onClick={ReportComment}>신고하기</button>
                                }

                                <div id='CommentContent'>{item.content}</div>
                                <hr id='hrSize'></hr>
                            </div>
                        )
                    }



                })
            }
            <div>
                <Paging page={page} count={pageCnt} setPage={handlePageChange} />
            </div>
        </div>
    )

}

export default CommentList;