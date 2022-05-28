package com.ssafy.moabang.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.iarcuschin.simpleratingbar.SimpleRatingBar
import com.ssafy.moabang.R
import com.ssafy.moabang.data.model.dto.ReviewList
import com.ssafy.moabang.data.model.viewmodel.MyPageViewModel
import com.ssafy.moabang.data.model.viewmodel.ReviewViewModel
import com.ssafy.moabang.src.theme.ReviewActivity
import com.ssafy.moabang.src.util.CustomDialog

class PostReviewListRVAdapter: RecyclerView.Adapter<PostReviewListRVAdapter.ViewHolder>() {
    var data: MutableList<ReviewList> = mutableListOf()
    lateinit var itemClickListener: ItemClickListener

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                if(this@PostReviewListRVAdapter::itemClickListener.isInitialized){
                    itemClickListener.onClick(data[adapterPosition])
                }
            }
        }

        fun bind(item: ReviewList){
            itemView.findViewById<TextView>(R.id.tv_pritem_tname).text = item.tname
            itemView.findViewById<TextView>(R.id.tv_pritem_regDate).text = item.regDate.split("T")[0]

            val img = itemView.findViewById<ImageView>(R.id.iv_pritem_img)
            Glide.with(img).load(item.img).centerCrop().into(img)

            itemView.findViewById<SimpleRatingBar>(R.id.ratingBar_pritem).rating = item.rating/2
            itemView.findViewById<TextView>(R.id.tv_pritem_rating).text = item.rating.toString()

            val detail = itemView.findViewById<TextView>(R.id.tv_pritem_detail)
            var txt = item.playDate + ", "
            txt += item.player.toString() + "인, "
            txt += if(item.isSuccess == 1) "탈출 성공"
            else "탈출 실패"
            detail.text = txt

            itemView.findViewById<TextView>(R.id.tv_pritem_active).text = item.active
            itemView.findViewById<TextView>(R.id.tv_pritem_diff).text = item.chaegamDif.toString()
            itemView.findViewById<TextView>(R.id.tv_pritem_time).text = item.clearTime.toString()
            itemView.findViewById<TextView>(R.id.tv_pritem_hint).text = item.hint.toString() + "개"
            itemView.findViewById<TextView>(R.id.tv_pritem_player).text = item.recPlayer.toString() + "명"
            itemView.findViewById<TextView>(R.id.tv_pritem_desc).text = item.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_post_review_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        holder.bind(data[position])

        holder.itemView.findViewById<TextView>(R.id.tv_pritem_revise).setOnClickListener {
            val intent = Intent(holder.itemView.context, ReviewActivity::class.java)
                .putExtra("type", "수정")
                .putExtra("review", data[position].rid)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }

        holder.itemView.findViewById<TextView>(R.id.tv_pritem_delete).setOnClickListener {
            CustomDialog(holder.itemView.context)
                .setContent("리뷰를 삭제하시겠습니까?")
                .setPositiveButtonText("삭제")
                .setOnPositiveClickListener{
                    ReviewViewModel().reviewDelete(data[position].rid)
                    MyPageViewModel().getAllDoneTheme()
                    data.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, data.size)
                }.build().show()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface ItemClickListener {
        fun onClick(item: ReviewList)
    }
}