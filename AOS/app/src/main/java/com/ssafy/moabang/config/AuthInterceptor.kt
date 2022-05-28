package com.ssafy.moabang.config

import com.ssafy.moabang.config.GlobalApplication.Companion.sp
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val token: String? = sp.getString("moabangToken")
        token?.let {
            builder.addHeader("Authorization", token)
        }

        return chain.proceed(builder.build())
    }
}