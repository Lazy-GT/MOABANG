package com.ssafy.moabang.data.model.dto

import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class ThemeForCompare(
    val tid: Int,
    val tname: String,
    var cname: String,
    var lat : String?,
    var lon : String?,
    val img: String,
    val genre: String,
    val grade: Double,
    val difficulty: Int,
    val time: String,
    val type: String,
    val activity: String,
    val rplayer: String,
): Parcelable