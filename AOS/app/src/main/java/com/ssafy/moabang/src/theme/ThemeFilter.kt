package com.ssafy.moabang.src.theme

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ThemeFilter (
    var island: String,
    var si: ArrayList<String>,
    var genre: ArrayList<String>,
    var type: ArrayList<String>,
    var player: ArrayList<Int>,
    var diff: ArrayList<Int>,
    var active: ArrayList<String>
): Parcelable