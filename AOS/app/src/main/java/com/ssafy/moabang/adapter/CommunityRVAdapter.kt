package com.ssafy.moabang.adapter

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.moabang.R
import com.ssafy.moabang.data.model.dto.Cafe
import com.ssafy.moabang.data.model.dto.Community
import com.ssafy.moabang.databinding.ListCommunityItemBinding
import java.time.LocalDateTime

class CommunityRVAdapter : RecyclerView.Adapter<CommunityRVAdapter.CommunityRVAdapterViewHolder>() {

    lateinit var mode : String
    lateinit var context: Context
    lateinit var data: List<Community>
    lateinit var itemClickListener: ItemClickListener
    @RequiresApi(Build.VERSION_CODES.O)
    inner class CommunityRVAdapterViewHolder(private val binding: ListCommunityItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                if(this@CommunityRVAdapter::itemClickListener.isInitialized){
                    itemClickListener.onClick(data[adapterPosition])
                }
            }
        }

        private val now = LocalDateTime.now()

        fun bindInfo(community: Community) {
            if(community.reportCnt <= 3){
                Glide.with(binding.civCommuF).load(community.user.pimg)
                    .placeholder(R.drawable.icon_profile).into(binding.civCommuF)
                binding.tvCommuFComment.text = community.count.toString()
                binding.tvCommuFHeader.text = community.header
                binding.tvCommuFAuthor.text = community.user.nickname
                binding.tvCommuFTitle.text = community.title
                if(isToday(community.createDate)){
                    binding.tvCommuFTime.text = community.createDate.hour.toString() + ":" + community.createDate.minute.toString()
                }else{
                    var monthStr = community.createDate.monthValue.toString()
                    var dayStr = community.createDate.dayOfMonth.toString()
                    if(community.createDate.monthValue < 10){
                        monthStr = "0"+community.createDate.monthValue.toString()
                    }
                    if(community.createDate.dayOfMonth < 10){
                        dayStr = "0"+community.createDate.dayOfMonth.toString()
                    }
                    binding.tvCommuFTime.text = monthStr+"."+dayStr
                }
                if(mode == "latest3Announcement"){
                    binding.tvCommuFHeader.setTextColor(context.resources.getColor(R.color.moabang_gray, null))
                    binding.tvCommuFTime.typeface = Typeface.DEFAULT_BOLD
                    binding.tvCommuFAuthor.typeface = Typeface.DEFAULT_BOLD
                    binding.tvCommuFTitle.typeface = Typeface.DEFAULT_BOLD
                    binding.tvCommuFHeader.typeface= Typeface.DEFAULT_BOLD
                }
                if(mode == "mypost"){
                    binding.tvCommuFAuthor.visibility = View.GONE
                }
            }else{
                Glide.with(binding.civCommuF).load(community.user.pimg)
                    .placeholder(R.drawable.icon_profile).into(binding.civCommuF)
                binding.tvCommuFAuthor.text = community.user.nickname
                binding.tvCommuFTitle.text = "※ 신고된 게시물입니다 ※"
                binding.tvCommuFTime.visibility = View.GONE
                binding.commuFComment.visibility = View.GONE
                binding.commuFComment2.visibility = View.GONE
                binding.tvCommuFComment.visibility = View.GONE
                binding.tvCommuFHeader.text = "신고"
            }
        }

        private fun isToday(time : LocalDateTime) : Boolean{
            return now.toLocalDate() == time.toLocalDate()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommunityRVAdapterViewHolder {
        val binding = ListCommunityItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CommunityRVAdapterViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CommunityRVAdapterViewHolder, position: Int) {
        holder.bindInfo(data[position])
    }

    override fun getItemCount(): Int = data.size

    interface ItemClickListener {
        fun onClick(community: Community)
    }

}