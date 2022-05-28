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
import com.ssafy.moabang.data.model.viewmodel.ThemeViewModel
import com.ssafy.moabang.databinding.ListThemeItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ThemeListRVAdapter: RecyclerView.Adapter<ThemeListRVAdapter.ViewHolder>() {
    var from : String = ""
    var data: List<Theme> = emptyList()
    lateinit var binding: ListThemeItemBinding
    lateinit var itemClickListener: ItemClickListener

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                if(this@ThemeListRVAdapter::itemClickListener.isInitialized){
                    itemClickListener.onClick(data[adapterPosition])
                }
            }
        }
        fun bind(item: Theme){
            val themeImg = itemView.findViewById<ImageView>(R.id.iv_themeL_img)
            val tvThemeName = itemView.findViewById<TextView>(R.id.tv_themeL_theme_name)
            val tvCafeName = itemView.findViewById<TextView>(R.id.tv_themeL_cafe_name)
            val tvGenre = itemView.findViewById<TextView>(R.id.tv_themeL_genre)
            val tvTime = itemView.findViewById<TextView>(R.id.tv_themeL_time)
            val tvRating = itemView.findViewById<TextView>(R.id.tv_themeL_rating)
            val tvDiff = itemView.findViewById<TextView>(R.id.tv_themeL_diff)
            val tvType = itemView.findViewById<TextView>(R.id.tv_themeL_type)
            val tvActive = itemView.findViewById<TextView>(R.id.tv_themeL_active)
            val tvPlayer = itemView.findViewById<TextView>(R.id.tv_themeL_player)
            val like = itemView.findViewById<ImageView>(R.id.iv_themeL_like)

            if(item.islike){
                like.setImageResource(R.drawable.icon_like_after)
            } else {
                like.setImageResource(R.drawable.icon_like_before)
            }

            Glide.with(themeImg).load(item.img).into(themeImg)
            tvThemeName.text = item.tname
            tvCafeName.text = item.cname
            tvGenre.text = item.genre
            tvTime.text = item.time
            tvRating.text = item.grade.toString()
            tvDiff.text = item.difficulty.toString()
            tvType.text = item.type
            tvActive.text = if(item.activity == "") "-" else item.activity
            tvPlayer.text = item.rplayer + "명"

            if(from == "HomeFragment"){
                val tvThemeLikeCount = itemView.findViewById<TextView>(R.id.tv_themeL_like_count)
                tvThemeLikeCount.apply {
                    visibility = View.VISIBLE
                    text = "+" + item.count.toString()
                }
                tvRating.visibility = View.GONE
                itemView.findViewById<TextView>(R.id.tv_themeL_star).visibility = View.GONE
            }else if(from == "CafeDetailActivity"){
                itemView.findViewById<TextView>(R.id.tv_themeL_like_count).apply {
                    visibility = View.GONE
                }
                itemView.findViewById<ImageView>(R.id.iv_themeL_like).apply {
                    visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_theme_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])

        holder.itemView.findViewById<ImageView>(R.id.iv_themeL_like).setOnClickListener {
            data[position].islike = !data[position].islike
            if(data[position].islike){
                holder.itemView.findViewById<ImageView>(R.id.iv_themeL_like).setImageResource(R.drawable.icon_like_after)
                data[position].count += 1
            } else {
                holder.itemView.findViewById<ImageView>(R.id.iv_themeL_like).setImageResource(R.drawable.icon_like_before)
                data[position].count -= 1
            }
            CoroutineScope(Dispatchers.Main).launch{

                holder.itemView.findViewById<TextView>(R.id.tv_themeL_like_count).text = "+" + (data[position].count).toString()
                Repository.get().setThemeLike(data[position].tid, data[position].islike)
                ThemeViewModel().themeLike(data[position].tid)
            }
        }
    }

    fun filterList(list: List<Theme>){
        data = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    interface ItemClickListener {
        fun onClick(item: Theme)
    }
}