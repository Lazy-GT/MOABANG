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
import com.ssafy.moabang.data.model.repository.Repository
import com.ssafy.moabang.data.model.response.DoneThemeResponse
import com.ssafy.moabang.data.model.viewmodel.ThemeViewModel
import com.ssafy.moabang.databinding.ListThemeItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DoneThemeListRVAdapter: RecyclerView.Adapter<DoneThemeListRVAdapter.ViewHolder>() {
    var data: List<DoneThemeResponse> = mutableListOf()
    lateinit var itemClickListener: ItemClickListener

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                if(this@DoneThemeListRVAdapter::itemClickListener.isInitialized){
                    itemClickListener.onClick(data[adapterPosition])
                }
            }
        }
        fun bind(item: DoneThemeResponse){
            val themeImg = itemView.findViewById<ImageView>(R.id.iv_doneL_img)
            val tvThemeName = itemView.findViewById<TextView>(R.id.tv_doneL_tname)
            val tvCafeName = itemView.findViewById<TextView>(R.id.tv_doneL_cname)
            val tvRating = itemView.findViewById<TextView>(R.id.tv_doneL_rate)
            val tvIsSuccess = itemView.findViewById<TextView>(R.id.tv_doneL_isSucess)
            val tvContent = itemView.findViewById<TextView>(R.id.tv_doneL_content)

            Glide.with(themeImg).load(item.img).centerCrop().into(themeImg)
            tvThemeName.text = item.tname
            tvCafeName.text = item.cname
            tvRating.text = item.rating.toString()
            tvIsSuccess.text = if(item.isSuccess == 1) "탈출 성공" else "탈출 실패"
            tvContent.text = item.player.toString() + "명, " + item.playDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_done_theme_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun filterList(list: List<DoneThemeResponse>){
        data = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    interface ItemClickListener {
        fun onClick(item: DoneThemeResponse)
    }
}