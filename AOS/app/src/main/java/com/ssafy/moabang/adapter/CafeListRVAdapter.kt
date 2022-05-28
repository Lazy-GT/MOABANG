package com.ssafy.moabang.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.ssafy.moabang.R
import com.ssafy.moabang.data.model.dto.Cafe
import com.ssafy.moabang.databinding.ListCafeItemCafeFBinding
import com.ssafy.moabang.src.util.LocationUtil
import kotlin.math.roundToInt

class CafeListRVAdapter(var cafeList: List<Cafe>) :
    RecyclerView.Adapter<CafeListRVAdapter.CafeViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CafeViewHolder {
        val binding = ListCafeItemCafeFBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return CafeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CafeViewHolder, position: Int) {
        holder.bindInfo(cafeList[position])
    }

    override fun getItemCount(): Int = cafeList.size


    inner class CafeViewHolder(private val binding: ListCafeItemCafeFBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(cafe : Cafe) {
            val cur = LocationUtil().getCurrentLocation(binding.root.context) ?: LatLng(37.566535, 126.9779692)
            var distance = if (cafe.lat == "" || cafe.lon == "") "알수없음"
            else LocationUtil().getDistanceLatLngInKm(cur!!.latitude, cur!!.longitude, cafe.lat!!.toDouble(), cafe.lon!!.toDouble()).roundToInt().toString() + "km"

            Glide.with(binding.root.context)
                .load(cafe.img)
                .placeholder(R.drawable.web_logo)
                .centerCrop()
                .into(binding.iv1)
            binding.tv1.text = cafe.cname
            binding.tv2.text = cafe.island + " " + cafe.si
            binding.tv3.text = distance

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