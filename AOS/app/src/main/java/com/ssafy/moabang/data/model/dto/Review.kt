package com.ssafy.moabang.data.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Review(
    val reviewId: Int,
    val userId: Int,
    val themeId: Int,
    val userName: String,
    val rating: Double,
    val isSuccess: Int,
    val timeLeft: String,
    val player: Int,
    val reqPlayer: Int,
    val active: Int,
    val diff: Int,
    val hint: Int,
    val playDate: String,
    var regDate: String,
    val desc: String,
): Parcelable