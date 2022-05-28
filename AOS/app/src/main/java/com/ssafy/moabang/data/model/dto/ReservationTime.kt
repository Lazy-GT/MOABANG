package com.ssafy.moabang.data.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReservationTime(
    val themeId: Int,
    val date: String,
    val time: String,
    var isAvailable: Boolean
): Parcelable