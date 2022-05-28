package com.ssafy.moabang.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.moabang.R
import com.ssafy.moabang.config.GlobalApplication
import com.ssafy.moabang.data.model.dto.Comment
import com.ssafy.moabang.data.model.dto.CommentUpdateRequest
import com.ssafy.moabang.data.model.repository.CommunityRepository
import com.ssafy.moabang.databinding.ListCommentBinding
import com.ssafy.moabang.src.util.ReportDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommentRVAdapter : RecyclerView.Adapter<CommentRVAdapter.CommentRVAdapterViewHolder>() {

    var communityRepository = CommunityRepository()
    lateinit var data: MutableList<Comment>

    inner class CommentRVAdapterViewHolder(var binding: ListCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(comment: Comment) {
            Glide.with(binding.root.context)
                .load(comment.userProfile)
                .placeholder(R.drawable.icon_profile)
                .centerCrop()
                .into(binding.civCommentItem)
            binding.tvCommentItemAuthor.text = comment.userName

            if(comment.reportCnt <= 3){
                binding.tvCommentDate.text = comment.regDate
                binding.tvCommentContent.text = comment.content

                if (comment.uid == GlobalApplication.sp.getInt("uid")) {
                    binding.btCommentRemove.visibility = ViewGroup.VISIBLE
                    binding.btCommentEdit.visibility = ViewGroup.VISIBLE
                    binding.btCommentReport.visibility = View.GONE

                    binding.btCommentRemove.setOnClickListener {
                        CoroutineScope(Dispatchers.IO).launch {
                            communityRepository.deleteComment(
                                comment.coid
                            )
                        }
                        data.removeAt(adapterPosition)
                        notifyItemRemoved(adapterPosition)
                        notifyItemRangeChanged(adapterPosition, data.size)
                    }

                    binding.btCommentEdit.setOnClickListener {
                        binding.tvCommentContent.visibility = ViewGroup.GONE
                        binding.etCommentContent.visibility = ViewGroup.VISIBLE
                        binding.btCommentEdit.visibility = ViewGroup.GONE
                        binding.btCommentEditDone.visibility = ViewGroup.VISIBLE
                        binding.etCommentContentInput.setText(binding.tvCommentContent.text)
                    }

                    binding.btCommentEditDone.setOnClickListener {
                        binding.tvCommentContent.visibility = ViewGroup.VISIBLE
                        binding.etCommentContent.visibility = ViewGroup.GONE
                        binding.btCommentEdit.visibility = ViewGroup.VISIBLE
                        binding.btCommentEditDone.visibility = ViewGroup.GONE


                        binding.tvCommentContent.text = binding.etCommentContentInput.text.toString()
                        CoroutineScope(Dispatchers.IO).launch {
                            val temp = CommentUpdateRequest(
                                comment.coid,
                                binding.etCommentContentInput.text.toString()
                            )
                            communityRepository.updateComment(temp)
                        }
                    }
                } else {
                    binding.btCommentRemove.visibility = ViewGroup.GONE
                    binding.btCommentEdit.visibility = ViewGroup.GONE
                    binding.btCommentReport.visibility = View.VISIBLE
                    binding.btCommentReport.setOnClickListener {
                        val dialog = ReportDialog(
                            binding.root.context,
                            data[adapterPosition].coid,
                            2,
                            data[adapterPosition].content
                        )
                        dialog.show()
                    }
                }
            }
            else{
                binding.tvCommentDate.visibility = ViewGroup.GONE
                binding.tvCommentContent.text = "※신고된 댓글입니다※"
                binding.btCommentRemove.visibility = ViewGroup.GONE
                binding.btCommentEdit.visibility = ViewGroup.GONE
                binding.btCommentReport.visibility = View.GONE
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentRVAdapter.CommentRVAdapterViewHolder {
        val binding = ListCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentRVAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CommentRVAdapter.CommentRVAdapterViewHolder,
        position: Int
    ) {
        holder.bindInfo(data[position])
    }

    override fun getItemCount(): Int = data.size
}