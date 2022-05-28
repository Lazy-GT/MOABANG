
import React from "react";
import Pagination from "react-js-pagination";


import '../my_contents/Contents_1_Paging.css';


const Contents_1_Paging = ({ page, count, setPage }) => {

    return (
        <Pagination
            activePage={page}
            itemsCountPerPage={6}
            totalItemsCount={count}
            pageRangeDisplayed={5}
            prevPageText={"‹"}
            nextPageText={"›"}
            onChange={setPage}
        />
    )
}

export default Contents_1_Paging;