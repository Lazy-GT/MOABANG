package com.ssafy.moabang.data.model.response

import android.os.Parcelable
import com.ssafy.moabang.data.model.dto.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewResponse(
    val rid: Int,
    var userInfo: User,
    val tid: Int,
    var rating: Float,
    val isSuccess: Int,
    val hint: Int,
    val clearTime: Int,
    val player: Int,
    val recPlayer: Int,
    val active: String,
    val chaegamDif: Int,
    val playDate: String,
    val regDate: String,
    val content: String,
    val reportCnt : Int, // 3개 넘어가면 안 보이게 할것임
): Parcelable
