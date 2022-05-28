package com.ssafy.moabang.data.model.repository


import android.util.Log
import com.ssafy.moabang.config.GlobalApplication
import com.ssafy.moabang.data.api.ThemeApi
import com.ssafy.moabang.data.model.dto.Theme
import com.ssafy.moabang.data.model.response.ReviewStatResponse
import java.lang.Exception
import retrofit2.Response

class ThemeRepository {
    val themeApi = GlobalApplication.retrofit.create(ThemeApi::class.java)

    fun getAllTheme(): Response<List<Theme>>? {
        try{
            var res = themeApi.getAllTheme().execute()
            Log.d("Theme Repository Test","성공")
            return res
        } catch (e: Exception){
            e.printStackTrace()
        }
        return null
    }

    fun themeLike(tid: Int) : Response<String>? {
        try{
           var res = themeApi.themeLike(tid).execute()
            Log.d("Theme LIKE Test","성공 $res")
            return res
        } catch(e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun themeStat(tid: Int) : Response<ReviewStatResponse>? {
        try{
            var res = themeApi.themeStat(tid).execute()
            Log.d("Theme STAT Test","성공 $res")
            return res
        } catch(e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}