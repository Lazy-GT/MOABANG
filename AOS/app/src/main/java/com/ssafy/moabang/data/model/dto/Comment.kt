package com.ssafy.moabang.data.model.dto

import java.time.LocalDateTime

data class Comment(
    val coid: Int,
    val content: String,
    val regDate: String,
    val uid: Int,
    val userProfile: String,
    val userName: String,
    val reportCnt : Int
)
