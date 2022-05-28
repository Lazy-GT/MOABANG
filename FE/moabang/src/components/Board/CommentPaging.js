
import React from "react";
import Pagination from "react-js-pagination";


import '../ThemePage/ThemeCSS/Paging.css';


const Paging = ({page, count, setPage}) => {
    
    return (
        <Pagination
                    activePage={page}
                    itemsCountPerPage={5}
                    totalItemsCount={count}
                    pageRangeDisplayed={5}
                    prevPageText={"‹"}
                    nextPageText={"›"}
                    onChange={setPage}
                />
    )
}

export default Paging;