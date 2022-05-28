package com.ssafy.moabang.data.model.repository

import android.util.Log
import com.ssafy.moabang.config.GlobalApplication
import com.ssafy.moabang.data.api.ReportApi
import com.ssafy.moabang.data.model.dto.ReportRequest
import retrofit2.Response
import java.lang.Exception

class ReportRepository {
    val reportApi = GlobalApplication.retrofit.create(ReportApi::class.java)

    fun createReport(reportRequest : ReportRequest) : Response<Boolean>?{
        try{
            val result  = reportApi.createReport(reportRequest).execute()
            return result
        }catch (e : Exception){
            e.printStackTrace()
        }
        return null
    }
}