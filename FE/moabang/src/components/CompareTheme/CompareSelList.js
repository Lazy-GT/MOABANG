import "./CompareCss/CompareSelList.css"


const CompareSelList = ({ theme ,selCompare,setSelCompare }) => {
    
    const deleteSelTheme = () => {
        for (let i = 0; i < selCompare.length; i++) {
            if (selCompare[i].tid === theme.tid) {
                //테마를 리스트에서 제거
                setSelCompare(e => (e.filter((value, index) => index !== i)));
                return;
            }
            
        }
    }

    return (
        <div className='selItem'>
            <img id='selThemeImg' alt='selThemeImg' src={theme.img} />
            <div id='selThemeName'>{theme.tname}</div>
            <button id='selDelBtn' onClick={deleteSelTheme}>제거</button>
        </div>
    );


}
export default CompareSelList;