package com.ssafy.moabang.src.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.ssafy.moabang.R
import com.ssafy.moabang.databinding.ActivityMyPostBinding

class MyPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init(){
        binding.toolbarMyPost.ivToolbarIcon.setOnClickListener { finish() }
        binding.toolbarMyPost.tvToolbarTitle.text = "작성글 관리"

        initTabLayout()
    }

    private fun initTabLayout(){
        supportFragmentManager.beginTransaction().replace(R.id.frame_myPost, MyPostCommunityFragment()).commit()

        binding.myPostTabLaout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                when(tab.position){
                    0 -> supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_myPost, MyPostCommunityFragment())
                        .commit()

                    1 -> supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_myPost, MyPostReviewFragment())
                        .commit()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }
            override fun onTabReselected(tab: TabLayout.Tab?) { }
        })
    }
}