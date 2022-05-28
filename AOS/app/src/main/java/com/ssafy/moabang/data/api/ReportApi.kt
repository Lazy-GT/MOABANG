package com.ssafy.moabang.data.api

import com.ssafy.moabang.data.model.dto.ReportRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ReportApi {
    @POST("/report/create")
    fun createReport(
        @Body reportRequest: ReportRequest
    ) : Call<Boolean>
}