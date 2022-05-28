package com.ssafy.moabang.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.moabang.R
import com.ssafy.moabang.data.model.dto.Cafe
import com.ssafy.moabang.databinding.ListCafeItemHomeBinding
import kotlin.math.roundToInt

class NearCafeListRVAdapter (var cafeList: List<Cafe>) :
    RecyclerView.Adapter<NearCafeListRVAdapter.NearCafeViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): NearCafeViewHolder {
        val binding = ListCafeItemHomeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return NearCafeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NearCafeViewHolder, position: Int) {
        holder.bindInfo(cafeList[position])
    }

    override fun getItemCount(): Int = cafeList.size


    inner class NearCafeViewHolder(private val binding: ListCafeItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(cafe : Cafe) {
            Glide.with(binding.root.context)
                .load(cafe.img)
                .placeholder(R.drawable.door)
                .centerCrop()
                .into(binding.ivHomeFImg)
            binding.tvHomeFCname.text = cafe.cname
            binding.tvHomeFLocation.text = cafe.location
            binding.tvHomeFDistance.text = if(cafe.distance == 0.0) "알수없음"
            else cafe.distance.roundToInt().toString() + "km"

            itemView.setOnClickListener {
                listener.onClick(cafe)
            }
        }
    }

    interface CafeItemClickListener {
        fun onClick(cafe: Cafe)
    }

    lateinit var listener: CafeItemClickListener
    fun setItemClickListener(listener: CafeItemClickListener) {
        this.listener = listener
    }

}