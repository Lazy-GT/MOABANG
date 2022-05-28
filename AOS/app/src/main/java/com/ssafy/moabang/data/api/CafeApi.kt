package com.ssafy.moabang.data.api

import com.ssafy.moabang.data.model.dto.Cafe
import com.ssafy.moabang.data.model.dto.Theme
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface CafeApi {
    @GET("/cafe/list")
    fun getAllCafe(
        @Header("token") jwtToken : String?,
    ) : Call<List<Cafe>>

    @GET("/cafe/theme/{cid}")
    fun getThemeByCid(
        @Header("token") jwtToken : String?,
        @Path("cid") cid : Int
    ) : Call<List<Theme>>

    @GET("/cafe/theme/list")
    fun getAllThemeWithLike() : Call<List<Theme>>
}