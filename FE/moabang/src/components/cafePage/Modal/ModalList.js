import React from "react"

import "./ModalList.css"

const ModalList = (props) => {
    const Theme = props.Theme;
    
    const DifficultyKeyImg = (diff) => {
        const result = [];
        
        for (let i = 0; i < diff; i++) {
            result.push(<img key={i}  id='DiffKey' src='https://www.emojiall.com/images/240/htc/1f511.png' alt='keyimg' />)
        }
        return result;

    }
    const ConanMin = (min) => {
        const num = parseInt(min.substr(-3,1));
        const result = [];
        for (let i = 0; i < num; i++) {
            result.push(<img key={i}  id='Conan' src='https://us.123rf.com/450wm/iconmama/iconmama1512/iconmama151200208/49892585-%EC%9D%B8%EA%B0%84-%EC%95%84%EC%9D%B4%EC%BD%98.jpg?ver=6' alt='conanMin' />)
        }
        return result;
        
    }
    const ConanMax = (max) => {
        const num = parseInt(max.substr(-1));
        const result = [];
        for (let i = 0; i < num; i++) {
            result.push(<img key={i} id='Conan' src='https://us.123rf.com/450wm/iconmama/iconmama1512/iconmama151200208/49892585-%EC%9D%B8%EA%B0%84-%EC%95%84%EC%9D%B4%EC%BD%98.jpg?ver=6' alt='conanMax' />)
        }
        return result;
    }
    const Water = () => {
        return <img id='water' src='https://upload.wikimedia.org/wikipedia/commons/thumb/4/42/Tilde.svg/1200px-Tilde.svg.png' alt="water" ></img>
    }
    const ChangeImg = (event) => {
        event.target.src="https://cdn.discordapp.com/attachments/963307192025485326/975564975617744946/unknown.png"
    }

    return (
        <div className='ModalDetailList'>
            {Theme.map((item, index) => (
                
                <div className='Modal-container' key={index} onClick={() => {
                    
                }}>
                    <img className='ThemeListImg' onError={ChangeImg} alt='profile' src={item.img}/>
                    
                    <div className='ThemeListInfo'>
                        <div id='ThemeListName'>{item.tname}</div>
                        <div id='ThemeListGenreTime'>{item.genre}&nbsp;&nbsp;{item.time}</div>
                        <div id='ThemeListType'>타입&nbsp;:&nbsp;{item.type}</div>
                        <div id='ThemeListDiff'>{DifficultyKeyImg(item.difficulty)}</div>
                        <div id='ThemeListRplayer'>{ConanMin(item.rplayer)}
                            {Water()}{ConanMax(item.rplayer)}</div>
                    </div>
                    
                </div>
            ))}
        </div>
    )

}

export default ModalList;

