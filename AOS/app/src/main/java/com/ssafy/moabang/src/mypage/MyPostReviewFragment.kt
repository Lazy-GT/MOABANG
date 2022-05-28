package com.ssafy.moabang.src.mypage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.moabang.R
import com.ssafy.moabang.adapter.PostReviewListRVAdapter
import com.ssafy.moabang.adapter.ReviewListRVAdapter
import com.ssafy.moabang.adapter.ThemeListRVAdapter
import com.ssafy.moabang.data.model.dto.Community
import com.ssafy.moabang.data.model.dto.ReviewList
import com.ssafy.moabang.data.model.dto.Theme
import com.ssafy.moabang.data.model.viewmodel.MyPageViewModel
import com.ssafy.moabang.databinding.FragmentMyPostReviewBinding
import com.ssafy.moabang.src.theme.ThemeDetailActivity

class MyPostReviewFragment : Fragment() {
    private lateinit var binding: FragmentMyPostReviewBinding
    private lateinit var reviewListRVAdapter: PostReviewListRVAdapter
    private val myPageViewModel: MyPageViewModel by viewModels()
    private var postList = mutableListOf<ReviewList>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPostReviewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onResume() {
        myPageViewModel.getAllPost()
        super.onResume()
    }

    private fun init(){
        reviewListRVAdapter = PostReviewListRVAdapter()

        myPageViewModel.getAllPost()
        myPageViewModel.myPostLiveData.observe(this){
            val res = it
            postList = res.reviewList as MutableList<ReviewList>
            reviewListRVAdapter.data = postList
            reviewListRVAdapter.notifyDataSetChanged()
        }

        binding.rvMyPostReview.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = reviewListRVAdapter
        }

        reviewListRVAdapter.itemClickListener = object : PostReviewListRVAdapter.ItemClickListener {
            override fun onClick(item: ReviewList) {
                if(item != null){
                    startActivity(Intent(requireActivity(), ThemeDetailActivity::class.java).putExtra("theme", item.tid))
                }
            }
        }
    }

}