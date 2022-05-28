package com.ssafy.moabang.data.model.dto

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.maps.android.clustering.ClusterItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Cafe (
    @PrimaryKey var cid : Int,
    var cname : String?,
    var cphone : String?,
    var url : String?,
    var time : String?,
    var img : String?,
    var location : String?,
    var lat : String?,
    var lon : String?,
    var island : String?, // 도(섬 도 島)
    var si : String?, // 시
): Parcelable{
    @IgnoredOnParcel
    var distance : Double = 0.0
}
