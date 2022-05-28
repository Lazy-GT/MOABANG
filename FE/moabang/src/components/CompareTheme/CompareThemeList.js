import "./CompareCss/CompareThemeList.css";
import axios from 'axios';
import Swal from 'sweetalert2';
import { useEffect, useState } from 'react';

const CompareThemeList = ({ theme, selCompare, setSelCompare, setCompareRerender }) => {

    const [showBtn, setShowBtn] = useState(true);
    const checkShowBtn = () => {
        for (let i = 0; i < selCompare.length; i++) {
            if (selCompare[i].tid === theme.tid) {//포함되어 있으면
                setShowBtn(e => e = false);
                break;
            }
            setShowBtn(e => e = true);
        }
    }

    useEffect(() => (
        checkShowBtn()
    ), [selCompare]);

    const addCompare = () => {
        //부모한태 추가된 테마를 넘겨주면 된다.
        if (selCompare.length == 4) return;
        for (let i = 0; i < selCompare.length; i++) {
            if (selCompare[i].tid === theme.tid) {
                return;
            }
        }

        setSelCompare(e => (
            [...e, theme]
        ));


    }
    const deleteCompare = () => {
        //비교하기 리스트에서 삭제
        axios.get(`/compare/${theme.tid}`,
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
            setCompareRerender(e => e = !e);
        }).catch(error => {
            console.error(error);
        });
    }

    return (
        <div className='compareList-container'>
            <span id='listTname'>{theme.tname}</span>
            <button id='delListBtn' onClick={deleteCompare}>삭제</button>
            {
                showBtn ? <button id='addListBtn' onClick={addCompare}>추가</button> :
                    <span></span>


            }

        </div>
    );
}

export default CompareThemeList;