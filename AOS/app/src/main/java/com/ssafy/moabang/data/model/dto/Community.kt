package com.ssafy.moabang.data.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.time.LocalDateTime

@Parcelize
data class Community(
    var count : Int, // 댓글 수
    var rid : Int,
    var user : User,
    var title : String?,
    var content : String?,
    var createDate : LocalDateTime,
    var updateDate : LocalDateTime,
    var header : String?,
    var reportCnt : Int,
) : Parcelable
// LocalDateTime