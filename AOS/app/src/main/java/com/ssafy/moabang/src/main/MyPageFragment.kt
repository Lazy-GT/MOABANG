package com.ssafy.moabang.src.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.kakao.sdk.user.UserApiClient
import com.ssafy.moabang.data.model.viewmodel.MyPageViewModel
import com.ssafy.moabang.databinding.FragmentMyPageBinding
import com.ssafy.moabang.src.login.LoginActivity
import com.ssafy.moabang.src.mypage.MyDoneActivity
import com.ssafy.moabang.src.mypage.MyLikeActivity
import com.ssafy.moabang.src.mypage.MyPostActivity
import com.ssafy.moabang.src.theme.ThemeCompareActivity

class MyPageFragment : Fragment() {
    private lateinit var binding: FragmentMyPageBinding
    private val mypageViewModel: MyPageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvMypageFLogout.setOnClickListener { logout() }
    }

    override fun onResume() {
        init()
        super.onResume()
    }

    private fun init(){
        setUIwithKakao()
        initClickListeners()
        var cnt = 0

        mypageViewModel.getAllDoneTheme()
        mypageViewModel.doneThemeListLiveData.observe(this){
            cnt = 0
            for(item in it){
                if(item.isSuccess == 1) cnt++
            }
            binding.tvMypageFTry.text = it.size.toString()
            binding.tvMypageFDone.text = cnt.toString()

            var srate = if(it.size == 0) 0
            else cnt.toDouble()/it.size * 100
            binding.tvMypageFSuccess.text = String.format("%.1f", srate)
        }
    }

    private fun initClickListeners(){
        binding.tvMypageFMenu2.setOnClickListener { // 찜한 테마
            startActivity(Intent(requireContext(), MyLikeActivity::class.java))
        }
        binding.tvMypageFMenu3.setOnClickListener { // 테마 비교
            startActivity(Intent(requireContext(), ThemeCompareActivity::class.java))
        }
        binding.tvMypageFMenu4.setOnClickListener { // 이용한 테마
            startActivity(Intent(requireContext(), MyDoneActivity::class.java))
        }
        binding.tvMypageFMenu5.setOnClickListener { // 나의 방탈출 통계
        }
        binding.tvMypageFMenu6.setOnClickListener { // 작성글 관리
            startActivity(Intent(requireContext(), MyPostActivity::class.java))
        }
    }

    /**
     * 로그아웃 후, Login Activity로 간다.
     */
    private fun logout(){
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e("AAAAA", "연결 끊기 실패", error)
            } else {
                Log.i("AAAAA", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
            }
        }
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    /**
     * 카카오에 있는 정보를 바탕으로 UI 변경
     */
    private fun setUIwithKakao(){
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("AAAAA", "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                Log.i("AAAAA", "사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +
                        "\n이메일: ${user.kakaoAccount?.email}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")

                binding.tvMypageFUserName.text = user.kakaoAccount?.profile?.nickname
                binding.tvMypageFUserEmail.text = user.kakaoAccount?.email
                Glide.with(this)
                    .load(user.kakaoAccount?.profile?.thumbnailImageUrl)
                    .into(binding.civMypageF)
            }
        }
    }

}