package com.ssafy.moabang.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.moabang.R
import com.ssafy.moabang.data.model.dto.Theme

class HorizontalThemeListRVAdapter: RecyclerView.Adapter<HorizontalThemeListRVAdapter.ViewHolder>() {
    var data: List<Theme> = emptyList()
    lateinit var itemClickListener: ItemClickListener

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                if(this@HorizontalThemeListRVAdapter::itemClickListener.isInitialized){
                    itemClickListener.onClick(data[adapterPosition])
                }
            }
        }
        fun bind(item: Theme){
            val img = itemView.findViewById<ImageView>(R.id.iv_vtheme_img)
            Glide.with(img).load(item.img).centerCrop().into(img)

            itemView.findViewById<TextView>(R.id.tv_vtheme_theme_name).text = item.tname
            itemView.findViewById<TextView>(R.id.tv_vtheme_genre).text = item.genre
            itemView.findViewById<TextView>(R.id.tv_vtheme_time).text = item.time
            itemView.findViewById<TextView>(R.id.tv_vtheme_rating).text = item.grade.toString()
            itemView.findViewById<TextView>(R.id.tv_vtheme_diff).text = item.difficulty.toString()
            itemView.findViewById<TextView>(R.id.tv_vtheme_player).text = item.rplayer
            itemView.findViewById<TextView>(R.id.tv_vtheme_type).text = item.type
            itemView.findViewById<TextView>(R.id.tv_vtheme_active).text = if(item.activity == "") "-" else item.activity
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_horizontal_theme_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    interface ItemClickListener {
        fun onClick(item: Theme)
    }
}