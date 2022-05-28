package com.ssafy.moabang.data.model.repository


import android.util.Log
import com.ssafy.moabang.config.GlobalApplication
import com.ssafy.moabang.data.api.ReviewApi
import com.ssafy.moabang.data.api.ThemeApi
import com.ssafy.moabang.data.model.dto.ReviewForCreate
import com.ssafy.moabang.data.model.dto.ReviewForUpdate
import com.ssafy.moabang.data.model.dto.Theme
import com.ssafy.moabang.data.model.response.ReviewResponse
import java.lang.Exception
import retrofit2.Response

class ReviewRepository {
    val reviewApi = GlobalApplication.retrofit.create(ReviewApi::class.java)

    fun addReview(review: ReviewForCreate): Response<String>? {
        try{
            var res = reviewApi.addReview(review).execute()
            Log.d("Review Repository Test","add 성공")
            return res
        } catch (e: Exception){
            e.printStackTrace()
        }
        return null
    }

    fun readReview(tid: Int): Response<List<ReviewResponse>>? {
        try{
            var res = reviewApi.readReview(tid).execute()
            Log.d("Review Repository Test","read 성공")
            return res
        } catch (e: Exception){
            e.printStackTrace()
        }
        return null
    }

    fun updateReview(rid: Int, review: ReviewForUpdate): Response<String>? {
        try {
            var res = reviewApi.updateReview(rid, review).execute()
            Log.d("Review Repository Test", "update 성공")
            return res
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun deleteReview(rid: Int): Response<String>? {
        try {
            var res = reviewApi.deleteReview(rid).execute()
            Log.d("Review Repository Test", "delete 성공")
            return res
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getReview(rid: Int): Response<ReviewResponse>? {
        try {
            var res = reviewApi.getReview(rid).execute()
            Log.d("Review Repository Test", "get 성공")
            return res
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}