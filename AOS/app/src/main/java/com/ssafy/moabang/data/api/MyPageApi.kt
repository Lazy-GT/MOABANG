package com.ssafy.moabang.data.api

import com.ssafy.moabang.data.model.dto.Theme
import com.ssafy.moabang.data.model.response.DoneThemeResponse
import com.ssafy.moabang.data.model.response.DoneTidResponse
import com.ssafy.moabang.data.model.response.MyPostResponse
import org.androidannotations.annotations.rest.Get
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MyPageApi {
    @GET("/mypage/theme/list")
    fun getAllLikeTheme() : Call<List<Theme>>

    @GET("/mypage/theme/review")
    fun getDoneTheme() : Call<List<DoneThemeResponse>>

    @GET("/mypage/theme/tlist")
    fun getDoneTid() : Call<List<DoneTidResponse>>

    @GET("/mypage/reviewcommunity")
    fun getMyPost() : Call<MyPostResponse>
}