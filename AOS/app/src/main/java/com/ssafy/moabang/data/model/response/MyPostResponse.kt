package com.ssafy.moabang.data.model.response

import com.ssafy.moabang.data.model.dto.Community
import com.ssafy.moabang.data.model.dto.ReviewList

data class MyPostResponse(
    val reviewList: List<ReviewList>,
    val communityList: List<Community>
)