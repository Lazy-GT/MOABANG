package com.ssafy.moabang.data.model.repository

import com.ssafy.moabang.config.GlobalApplication
import com.ssafy.moabang.data.api.CafeApi
import com.ssafy.moabang.data.model.dto.Theme
import retrofit2.Response
import java.lang.Exception

class CafeRepository {
    val cafeApi = GlobalApplication.retrofit.create(CafeApi::class.java)

    fun getAllThemeWithLike() : Response<List<Theme>>? {
        try {
            return cafeApi.getAllThemeWithLike().execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}