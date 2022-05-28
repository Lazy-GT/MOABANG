package com.ssafy.moabang.data.api

import com.ssafy.moabang.data.model.dto.User
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginApi {

    @POST("/user/klogin")
    fun login(
        @Header("token") token : String?,
    ) : Call<User>
}