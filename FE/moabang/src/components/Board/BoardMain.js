import BoardWrite from './BoardWrite';
import BoardList from './BoardList';
import { useState, useEffect } from 'react';
import axios from 'axios';
import "./BoardCSS/BoardMain.css"

const BoardMain = () => {
    const [showList, setShowList] = useState(true);
    const [boardList, setBoardList] = useState([]);
    const [category, setCategory] = useState("전체");


    const getBoardList = () => {
        axios.get('/community/read/list',
            {
                headers: {
                    'Authorization': localStorage.getItem("myToken")
                }
            }
        ).then(res => {
			setBoardList(res.data);
		}).catch(error => {
			console.error(error);
		});
    }
    useEffect(() => {
        getBoardList();
    },[showList]);

    const filteredProducts = boardList.filter((res) => {
        if (category === "전체") {
            return true;
        }
        else {
            return res.header===category;
        }
    })
    
    const WriteBtnHandler = () => {
        setShowList(e => !e); //작성 버튼을 누르면 작성지 보이게

    }
    const Categoryhandler = (e) => {
        setCategory(e.target.value);
    }

    return (
        <div className='board-container'>
            
            {showList ?
                <div>
                    <div className='boardName'>커뮤니티</div>
                    <div className='btnLocaion'>
                        <span>
                                <select id='boardSelect' onChange={Categoryhandler} value={category}>
                                    <option value="전체">전체</option>
                                    <option value="구인">구인</option>
                                    <option value="자유">자유</option>
                                </select>
                        </span>
                        <button className='mainWriteBtn' onClick={WriteBtnHandler}>
                            <img id='pencilImg' alt='pencil' src='https://w7.pngwing.com/pngs/759/500/png-transparent-drawing-pencil-sketch-pencil-pencil-illustrator-material-thumbnail.png'></img>
                            <span id='pencilText'>글쓰기</span>
                        </button>
                    </div>
                    <BoardList boardList={filteredProducts}/>
                </div>
                :
                <BoardWrite setShowList={setShowList} />
            }
        </div>  
    );
}

export default BoardMain;