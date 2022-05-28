package com.ssafy.moabang.src.main.cafe

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class CafeMapClusterItem(
    lat: Double,
    lng: Double,
    title: String,
    snippet: String,
    img: String
) : ClusterItem {

    private val position: LatLng
    private val title: String
    private val snippet: String
    private val img: String

    override fun getPosition(): LatLng {
        return position
    }

    override fun getTitle(): String {
        return title
    }

    override fun getSnippet(): String {
        return snippet
    }

    init {
        position = LatLng(lat, lng)
        this.title = title
        this.snippet = snippet
        this.img = img
        Log.d(
            "CafeMapClusterItem",
            "CafeMapClusterItem init ${this.position}, ${this.title}, ${this.snippet}"
        )
    }
}