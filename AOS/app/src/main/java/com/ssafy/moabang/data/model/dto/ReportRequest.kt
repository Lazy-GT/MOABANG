package com.ssafy.moabang.data.model.dto

data class ReportRequest(
    val category : Int, // 리뷰 0, 게시글 1, 댓글 2
    val reason : String,
    val target_id : Int
)
