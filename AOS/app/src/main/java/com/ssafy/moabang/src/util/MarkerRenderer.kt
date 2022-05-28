package com.ssafy.moabang.src.util

import com.google.maps.android.clustering.ClusterManager
import android.content.Context
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.graphics.drawable.toBitmap

import com.google.android.gms.maps.model.BitmapDescriptorFactory

import com.google.android.gms.maps.model.MarkerOptions

import com.google.maps.android.clustering.Cluster

import com.google.android.gms.maps.GoogleMap

import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.ssafy.moabang.R
import com.ssafy.moabang.src.main.cafe.CafeMapClusterItem


class MarkerRenderer(
    context: Context?,
    map: GoogleMap?,
    clusterManager: ClusterManager<CafeMapClusterItem>
) :
    DefaultClusterRenderer<CafeMapClusterItem>(context, map, clusterManager) {
    private val con = context
    override fun onClustersChanged(clusters: Set<Cluster<CafeMapClusterItem?>?>) {
        super.onClustersChanged(clusters)
    }

    override fun onBeforeClusterItemRendered(item: CafeMapClusterItem, markerOptions: MarkerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions)
        val icon = getDrawable(con!!, R.drawable.ic_marker)!!.toBitmap(100, 100, null)

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
    }

}