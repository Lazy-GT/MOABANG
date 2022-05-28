package com.ssafy.moabang.data.model.response

data class DoneThemeResponse(
    val tid: Int,
    val tname: String,
    val rating: Double,     // from review
    val isSuccess: Int,     // from theme
    val player: Int,        // from review
    var cname: String,      // from theme
    val img: String,        // from theme
    val playDate: String    // from review
)