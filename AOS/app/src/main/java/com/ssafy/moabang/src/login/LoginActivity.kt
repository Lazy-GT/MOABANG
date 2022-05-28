package com.ssafy.moabang.src.login

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.ssafy.moabang.databinding.ActivityLoginBinding
import com.ssafy.moabang.src.main.MainActivity
import com.ssafy.moabang.config.GlobalApplication
import com.ssafy.moabang.config.GlobalApplication.Companion.sp
import com.ssafy.moabang.data.model.dto.Cafe
import com.ssafy.moabang.data.model.dto.User
import com.ssafy.moabang.data.model.repository.Repository
import com.ssafy.moabang.data.model.viewmodel.ThemeViewModel
import com.ssafy.moabang.data.api.CafeApi
import com.ssafy.moabang.data.api.LoginApi
import com.ssafy.moabang.src.util.SharedPreferencesUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    val themeViewModel: ThemeViewModel by viewModels()

    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("AAAAA", "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i("AAAAA", "카카오계정으로 로그인 성공 ${token.accessToken}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.activity = this

        // 로그인 구현 전이라 우선 요 버튼 누르면 메인 화면으로 넘어가게 구현
        binding.tvLoginATmp.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        doIhaveKakaoToken()
    }

    private fun doIhaveKakaoToken(){
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e("AAAAA", "로그아웃 했음", error)
            }
            else if (tokenInfo != null) {
                Log.i("AAAAA", "자동 로그인")
                kakaoLogin(null)
            }
        }
    }

    fun kakaoLogin(view : View?) {
        val context = this
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e("AAAAA", "카카오톡으로 로그인 실패", error)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    sendTokenToBackend(token)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    /**
     * 카카오톡 로그인 성공 후 토큰을 서버에 전송한다.
     */
    private fun sendTokenToBackend(token: OAuthToken) {
        val loginService = GlobalApplication.retrofit.create(LoginApi::class.java)
        loginService.login(token.accessToken).enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){ // 로그인에 성공했다면
                    val jwtToken = response.headers()["Authorization"]
                    if (jwtToken != null) {
                        sp.putString("moabangToken", jwtToken.toString())
                        sp.putInt("uid", response.body()!!.uid!!)
                    }
                    getCafesFromServer(jwtToken.toString())// 2. 서버에서 전체 카페 데이터를 가져오고, 이를 로컬 DB에 저장한다.
                    themeViewModel.getAllTheme(jwtToken.toString())
                    val intent = Intent(this@LoginActivity, MainActivity::class.java) // 3. MainActivity로 전환한다.
                    startActivity(intent)
                    finish()
                }else{
                    Log.e("AAAAA","네트워킹 성공1, 하지만 원하는 결과가 아님. ${response.errorBody()}")
                    Toast.makeText(this@LoginActivity, "네트워크 성공했지만 에러발생 : ${response.errorBody()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("AAAAA","네트워크 오류1 : ${t.message}")
                Toast.makeText(this@LoginActivity, "네트워크 오류1 : ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getCafesFromServer(jwtToken: String) {
        Log.d("AAAAA","JWT토큰: $jwtToken")
        val cafeService = GlobalApplication.retrofit.create(CafeApi::class.java)
        cafeService.getAllCafe(jwtToken).enqueue(object : Callback<List<Cafe>>{
            override fun onResponse(call: Call<List<Cafe>>, response: Response<List<Cafe>>) {
                if(response.isSuccessful){
                    val data : List<Cafe> = response.body()!!
                    CoroutineScope(Dispatchers.IO).launch {
                        Repository.get().insertCafes(data)
                    }
                }else{
                    Log.e("AAAAA","실패!")
                }
            }

            override fun onFailure(call: Call<List<Cafe>>, t: Throwable) {
                Log.d("AAAAA","네트워크 오류2 : ${t.message}")
                Toast.makeText(this@LoginActivity, "네트워크 오류2 : ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}