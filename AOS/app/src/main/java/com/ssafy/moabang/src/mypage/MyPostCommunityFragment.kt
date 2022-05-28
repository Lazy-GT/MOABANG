package com.ssafy.moabang.src.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.moabang.R
import com.ssafy.moabang.adapter.CommunityRVAdapter
import com.ssafy.moabang.data.model.dto.Community
import com.ssafy.moabang.data.model.viewmodel.MyPageViewModel
import com.ssafy.moabang.databinding.FragmentMyPostCommunityBinding
import com.ssafy.moabang.src.main.community.CommunityDetailActivity

class MyPostCommunityFragment : Fragment() {
    private lateinit var binding: FragmentMyPostCommunityBinding
    private lateinit var communityRVAdapter: CommunityRVAdapter
    private val myPageViewModel: MyPageViewModel by viewModels()
    private var postList = mutableListOf<Community>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPostCommunityBinding.inflate(layoutInflater, container, false)
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
        communityRVAdapter = CommunityRVAdapter()

        myPageViewModel.getAllPost()
        myPageViewModel.myPostLiveData.observe(this){
            val res = it
            postList = res.communityList as MutableList<Community>
//            Log.d("POST LIST", "init: ${postList}")
            communityRVAdapter.data = postList
            communityRVAdapter.notifyDataSetChanged()
        }

        communityRVAdapter.apply {
            context = requireContext()
            mode = "mypost"
            data = postList
            Log.d("POST RVA TEST", "init: $data")
            itemClickListener = object : CommunityRVAdapter.ItemClickListener {
                override fun onClick(community: Community) {
                    startActivity(Intent(requireContext(), CommunityDetailActivity::class.java)
                        .putExtra("community", community)
                        .putExtra("mode", "read"))
                }
            }
        }

        binding.rvMyPostComm.apply{
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = communityRVAdapter
        }


    }

}