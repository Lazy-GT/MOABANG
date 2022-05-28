package com.ssafy.moabang.data.model.response

import android.os.Parcelable
import com.ssafy.moabang.data.model.dto.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewStatResponse(
    val r_rating: Float,
    val r_chaegamDif: Int,
    val r_isSuccess: Float,
    val r_activity: String,
    val r_clearTime: Int,
    val r_recPlayer: Int,
    val r_hint: Float,
): Parcelable
