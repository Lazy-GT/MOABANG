package com.ssafy.moabang.data.model.repository


import android.util.Log
import com.ssafy.moabang.config.GlobalApplication
import com.ssafy.moabang.data.api.MyPageApi
import com.ssafy.moabang.data.model.dto.Theme
import com.ssafy.moabang.data.model.response.DoneThemeResponse
import com.ssafy.moabang.data.model.response.DoneTidResponse
import com.ssafy.moabang.data.model.response.MyPostResponse
import java.lang.Exception
import retrofit2.Response

class MyPageRepository {
    val mypageApi = GlobalApplication.retrofit.create(MyPageApi::class.java)

    fun getAllLikeTheme(): Response<List<Theme>>? {
        try{
            var res = mypageApi.getAllLikeTheme().execute()
            Log.d("Mypage Repository Test","성공")
            return res
        } catch (e: Exception){
            e.printStackTrace()
        }
        return null
    }

    fun getDoneTheme(): Response<List<DoneThemeResponse>>? {
        try{
            var res = mypageApi.getDoneTheme().execute()
            Log.d("Mypage Repository Test","성공")
            return res
        } catch (e: Exception){
            e.printStackTrace()
        }
        return null
    }

    fun getDoneTid(): Response<List<DoneTidResponse>>? {
        try{
            var res = mypageApi.getDoneTid().execute()
            Log.d("Mypage Repository Test","성공")
            return res
        } catch (e: Exception){
            e.printStackTrace()
        }
        return null
    }

    fun getMyPost(): Response<MyPostResponse>? {
        try{
            var res = mypageApi.getMyPost().execute()
            Log.d("Mypage Repository Test","getMyPost 성공")
            return res
        } catch (e: Exception){
            e.printStackTrace()
        }
        return null
    }
}