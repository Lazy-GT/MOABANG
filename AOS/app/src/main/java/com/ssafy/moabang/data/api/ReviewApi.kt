package com.ssafy.moabang.data.api

import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import com.ssafy.moabang.data.model.dto.Review
import com.ssafy.moabang.data.model.dto.ReviewForCreate
import com.ssafy.moabang.data.model.dto.ReviewForUpdate
import com.ssafy.moabang.data.model.dto.Theme
import com.ssafy.moabang.data.model.response.ReviewResponse
import retrofit2.Call
import retrofit2.http.*

interface ReviewApi {
    @POST("/theme/review/create")
    fun addReview(
        @Body review: ReviewForCreate
    ) : Call<String>

    @GET("/theme/review/list/{tid}")
    fun readReview(
        @Path("tid") tid: Int
    ) : Call<List<ReviewResponse>>

    @PUT("theme/review/update/{rid}")
    fun updateReview(
        @Path("rid") rid: Int,
        @Body review: ReviewForUpdate
    ) : Call<String>

    @DELETE("theme/review/delete/{rid}")
    fun deleteReview(
        @Path("rid") rid: Int
    ) : Call<String>


    @GET("theme/review/{rid}")
    fun getReview(
        @Path("rid") rid: Int
    ) : Call<ReviewResponse>
}