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
import com.ssafy.moabang.databinding.ListCommunityAnnouncementItemBinding
import com.ssafy.moabang.databinding.ListCommunityItemBinding
import java.time.LocalDateTime

class CommunityAnnounceRVAdapter : RecyclerView.Adapter<CommunityAnnounceRVAdapter.CommunityAnnounceRVAdapterViewHolder>() {

    lateinit var mode : String
    lateinit var context: Context
    lateinit var data: List<Community>
    lateinit var itemClickListener: ItemClickListener
    @RequiresApi(Build.VERSION_CODES.O)
    inner class CommunityAnnounceRVAdapterViewHolder(private val binding: ListCommunityAnnouncementItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                if(this@CommunityAnnounceRVAdapter::itemClickListener.isInitialized){
                    itemClickListener.onClick(data[adapterPosition])
                }
            }
        }

        private val now = LocalDateTime.now()

        fun bindInfo(community: Community) {
            binding.tvCommuAFHeader.text = community.header
            binding.tvCommuAFTitle.text = community.title
            if(isToday(community.createDate)){
                binding.tvCommuAFTime.text = community.createDate.hour.toString() + ":" + community.createDate.minute.toString()
            }else{
                var monthStr = community.createDate.monthValue.toString()
                var dayStr = community.createDate.dayOfMonth.toString()
                if(community.createDate.monthValue < 10){
                    monthStr = "0"+community.createDate.monthValue.toString()
                }
                if(community.createDate.dayOfMonth < 10){
                    dayStr = "0"+community.createDate.dayOfMonth.toString()
                }
                binding.tvCommuAFTime.text = monthStr+"."+dayStr
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
    ): CommunityAnnounceRVAdapterViewHolder {
        val binding = ListCommunityAnnouncementItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CommunityAnnounceRVAdapterViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CommunityAnnounceRVAdapter.CommunityAnnounceRVAdapterViewHolder, position: Int) {
        holder.bindInfo(data[position])
    }

    override fun getItemCount(): Int = data.size

    interface ItemClickListener {
        fun onClick(community: Community)
    }

}