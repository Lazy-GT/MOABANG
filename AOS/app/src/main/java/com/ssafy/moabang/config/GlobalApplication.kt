package com.ssafy.moabang.config

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.kakao.sdk.common.KakaoSdk
import com.ssafy.moabang.BuildConfig
import com.ssafy.moabang.data.model.repository.Repository
import com.ssafy.moabang.src.util.SharedPreferencesUtil
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit


class GlobalApplication : Application() {
    val TIME_OUT = 10000L

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        instance = this

        KakaoSdk.init(this, BuildConfig.native_app_key) // 카카오 세팅

        gson = GsonBuilder()
            .setLenient()
            .create()

        sp = SharedPreferencesUtil(applicationContext)

        retrofitInit()

        Repository.initialize(this)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun retrofitInit(){
        // 승관홈 : http://114.129.238.28/
        // 서-버 : http://k6d205.p.ssafy.io:8080/
        // 모아방 : http://모아방.kr:8080/
        val serverURL="https://모아방.kr:8080/"

        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
            .connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
            // 로그캣에 okhttp.OkHttpClient로 검색하면 http 통신 내용을 보여줍니다.
//            .addInterceptor(
//                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS).setLevel(
//                    HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(AuthInterceptor()) // JWT 자동 헤더 전송
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(serverURL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(gsonConverterFactory())
            .build()
    }

    // String을 LocalDate, LocalTime, LocalDateTime 으로 변형하는걸 등록한다.
    @RequiresApi(Build.VERSION_CODES.O)
    private fun gsonConverterFactory(): GsonConverterFactory? {
        val gson = GsonBuilder()
            .registerTypeAdapter(
                LocalDateTime::class.java,
                JsonDeserializer<Any?> { json, typeOfT, context ->
                    LocalDateTime.parse(
                        json.asString,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                    )
                })
            .registerTypeAdapter(
                LocalDate::class.java,
                JsonDeserializer<Any?> { json, typeOfT, context ->
                    LocalDate.parse(
                        json.asString,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    )
                })
            .registerTypeAdapter(
                LocalTime::class.java,
                JsonDeserializer<Any?> { json, typeOfT, context ->
                    LocalTime.parse(
                        json.asString,
                        DateTimeFormatter.ofPattern("HH:mm:ss")
                    )
                })
            .create()
        return GsonConverterFactory.create(gson)
    }

    companion object {
        lateinit var instance: GlobalApplication
            private set

        lateinit var retrofit: Retrofit
            private set

        lateinit var gson: Gson
            private set

        lateinit var sp: SharedPreferencesUtil
    }
}