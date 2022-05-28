package com.ssafy.moabang.data.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewList(
    var content: String,
    var rid: Int,
    var cname: String,
    var img: String,
    var tid: Int,
    var tname: String,
    var playDate: String,
    var isSuccess: Int,
    var player: Int,
    var chaegamDif: Int,
    var hint: Int,
    var rating: Float,
    var clearTime: Int,
    var recPlayer: Int,
    var active: String,
    var regDate: String,
    var uid: Int
):Parcelable
