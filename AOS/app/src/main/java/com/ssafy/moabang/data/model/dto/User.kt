package com.ssafy.moabang.data.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var uid : Int?,
    var nickname : String?,
    var email : String?,
    var pimg : String?,
):Parcelable
