package com.ssafy.moabang.src.main.community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.kakao.sdk.user.UserApiClient
import com.ssafy.moabang.R
import com.ssafy.moabang.adapter.CommentRVAdapter
import com.ssafy.moabang.config.GlobalApplication.Companion.sp
import com.ssafy.moabang.data.model.dto.*
import com.ssafy.moabang.data.model.repository.CommunityRepository
import com.ssafy.moabang.databinding.ActivityCommunityDetailBinding
import com.ssafy.moabang.src.util.KeyboardVisibilityUtils
import com.ssafy.moabang.src.util.ReportDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class CommunityDetailActivity : AppCompatActivity() {

    private lateinit var mode: String // "write" or "read"
    private lateinit var community: Community
    private lateinit var binding: ActivityCommunityDetailBinding
    private var communityRepository = CommunityRepository()

    private lateinit var commentAdapter: CommentRVAdapter
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        keyboardVisibilityUtils = KeyboardVisibilityUtils(window,
            onShowKeyboard = { keyboardHeight ->
                binding.svCommRoot.run {
                    smoothScrollTo(scrollX, scrollY + keyboardHeight)
                }
            })

        setMode()
    }

    override fun onStart() {
        super.onStart()
        initView()
    }

    private fun setAdapter() {
        CoroutineScope(Dispatchers.Main).launch {
            var commentList : List<Comment> = emptyList()
            CoroutineScope(Dispatchers.IO).async {
                commentList = communityRepository.getCommentList(community_id = community.rid)?.body()!!
            }.await()
            commentAdapter = CommentRVAdapter()
            commentAdapter.data = commentList as MutableList<Comment>
            binding.rRcvComment.apply {
                adapter = commentAdapter
                layoutManager =
                    androidx.recyclerview.widget.LinearLayoutManager(this@CommunityDetailActivity)
            }
        }
    }

    private fun setMode() {
        mode = intent.getStringExtra("mode")!!
        if (mode == "read") {
            community = intent.getParcelableExtra("community")!!
        }
    }

    private fun initView() {
        if (mode == "read") {
            if(community.reportCnt <= 3){
                // 카테고리
                binding.tvCommuItemFHeader.visibility = View.VISIBLE
                binding.spCommuItemFHeader.visibility = View.INVISIBLE

                // 제목
                binding.tvCommuItemFTitle.visibility = View.VISIBLE
                binding.etCommuItemFTitle.visibility = View.INVISIBLE
                binding.textInputLayout.visibility = View.INVISIBLE

                // 본문
                binding.tvCommuItemFContent.isEnabled = false

                // 하단 버튼
                binding.btCommuWriteCancel.visibility = View.GONE
                binding.btCommuWriteWrite.visibility = View.GONE

                // 댓글
                binding.rComment.visibility = View.VISIBLE



                binding.tvCommuItemFAuthor.text = community.user.nickname
                binding.tvCommuItemFContent.setText(community.content)
                binding.tvCommuItemFContent.isEnabled = false
                binding.tvCommuItemFHeader.text = community.header
                binding.tvCommuItemFTitle.text = community.title
                Glide.with(this)
                    .load(community.user.pimg)
                    .placeholder(R.drawable.icon_profile)
                    .into(binding.civCommuItemF)

                binding.btCommuItemFWriteComment.setOnClickListener { commentWrite() }

                if (isMine()) {
                    binding.btCommuItemFReport.visibility = View.GONE
                    binding.btCommuItemFEdit.visibility = View.VISIBLE
                    binding.btCommuItemFRemove.visibility = View.VISIBLE

                    binding.btCommuItemFRemove.setOnClickListener { removeCommunity() }
                    binding.btCommuItemFEdit.setOnClickListener { modeChangeToEdit() }
                } else {
                    binding.btCommuItemFReport.visibility = View.VISIBLE
                    binding.btCommuItemFEdit.visibility = View.GONE
                    binding.btCommuItemFRemove.visibility = View.GONE

                    binding.btCommuItemFReport.setOnClickListener { report() }
                }

                setAdapter()
            }else{
                Toast.makeText(this, "신고된 게시글을 열람할 권한이 없습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }

        } else if (mode == "write" || mode == "edit") {
            // 카테고리
            binding.tvCommuItemFHeader.visibility = View.INVISIBLE
            binding.spCommuItemFHeader.visibility = View.VISIBLE

            // 제목
            binding.tvCommuItemFTitle.visibility = View.INVISIBLE
            binding.etCommuItemFTitle.visibility = View.VISIBLE
            binding.textInputLayout.visibility = View.VISIBLE

            // 본문
            binding.tvCommuItemFContent.isEnabled = true

            // 하단 버튼
            binding.btCommuWriteWrite.visibility = View.VISIBLE
            binding.btCommuWriteCancel.visibility = View.VISIBLE
            binding.btCommuWriteCancel.setOnClickListener {
                finish()
            }

            // 댓글
            binding.rComment.visibility = View.GONE

            // 상단 버튼
            binding.btCommuItemFReport.visibility = View.GONE
            binding.btCommuItemFEdit.visibility = View.GONE
            binding.btCommuItemFRemove.visibility = View.GONE


            val headerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.community_header,
                R.layout.spinner_text
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spCommuItemFHeader.adapter = adapter
            }

            if (mode == "write") {
                binding.btCommuWriteWrite.setOnClickListener { write() }
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e("AAAAA", "사용자 정보 요청 실패", error)
                    } else if (user != null) {
                        binding.tvCommuItemFAuthor.text = user.kakaoAccount?.profile?.nickname
                        Glide.with(this)
                            .load(user.kakaoAccount?.profile?.thumbnailImageUrl)
                            .into(binding.civCommuItemF)
                    }
                }
            } else if (mode == "edit") {
                binding.spCommuItemFHeader.setSelection(
                    headerAdapter.getPosition(community.header)
                )
                binding.etCommuItemFTitle.setText(binding.tvCommuItemFTitle.text)
                binding.tvCommuItemFContent.setText(binding.tvCommuItemFContent.text)
                binding.btCommuWriteWrite.setOnClickListener { edit() }
            }


        }


    }

    private fun modeChangeToEdit() {
        mode = "edit"
        initView()
    }

    private fun removeCommunity() {
        CoroutineScope(Dispatchers.IO).launch {
            communityRepository.deleteCommunity(community.rid)
            finish()
        }
    }

    private fun write() {
        CoroutineScope(Dispatchers.IO).launch {
            val header = binding.spCommuItemFHeader.selectedItem.toString()
            val title = binding.etCommuItemFTitle.text.toString()
            val content = binding.tvCommuItemFContent.text.toString()
            val recruitCreateRequest = RecruitCreateRequest(content, header, title)
            val result = communityRepository.insertCommunity(recruitCreateRequest)
            finish()
        }
    }

    private fun commentWrite(){
        val tempContent = binding.etCommuItefFCommentInput.text.toString()
        val tempCR = CommentRequest(community.rid, tempContent)
        binding.etCommuItefFCommentInput.setText("")
        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO).async {
                communityRepository.insertComment(tempCR)
            }.await()
            initView()
        }
    }


    private fun edit() {
        Log.d("BBBBB", "edit: ")
        CoroutineScope(Dispatchers.IO).launch {
            val header = binding.spCommuItemFHeader.selectedItem.toString()
            val title = binding.etCommuItemFTitle.text.toString()
            val content = binding.tvCommuItemFContent.text.toString()
            val rid = community.rid
            val recruitCreateRequest = RecruitCreateRequest(content, header, title)
            val result = communityRepository.updateCommunity(rid, recruitCreateRequest)
            finish()
        }
    }

    private fun isMine(): Boolean {
        return community.user.uid == sp.getInt("uid")
    }


    private fun report(){
        val dialog = ReportDialog(this, community.rid, 1, community.content!!)
        dialog.show()
    }

    override fun onDestroy() {
        keyboardVisibilityUtils.detachKeyboardListeners()
        super.onDestroy()
    }
}