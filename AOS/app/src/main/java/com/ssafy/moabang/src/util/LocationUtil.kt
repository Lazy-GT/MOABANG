package com.ssafy.moabang.src.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import com.google.android.gms.maps.model.LatLng
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class LocationUtil {

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(context: Context): LatLng? {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        return if (lastKnownLocation != null) {
            LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
        } else {
            null
        }
    }

    fun getDistanceLatLngInKm(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val R = 6371 // radius of earth in km
        val dLat = deg2rad(lat2 - lat1)
        val dLng = deg2rad(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(deg2rad(lat1)) * cos(deg2rad(lat2)) * sin(dLng / 2) * sin(
            dLng / 2
        )
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c
    }

    private fun deg2rad(deg: Double): Double {
        return deg * (Math.PI / 180)
    }
}