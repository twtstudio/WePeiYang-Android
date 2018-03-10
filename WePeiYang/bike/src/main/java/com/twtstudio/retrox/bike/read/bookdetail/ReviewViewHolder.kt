package com.twtstudio.retrox.bike.read.bookdetail

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.twt.wepeiyang.commons.experimental.extensions.bind
import com.twtstudio.retrox.bike.R
import com.twtstudio.retrox.bike.model.read.Detail
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by zhangyulong on 18-1-26.
 */
class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val ivReviewAvatar = itemView.findViewById<CircleImageView>(R.id.item_book_detail_review_avatar)
    private val tvUserName = itemView.findViewById<TextView>(R.id.item_book_detail_review_username)
    private val rbScore = itemView.findViewById<RatingBar>(R.id.item_book_detail_review_score)
    private val tvContent = itemView.findViewById<TextView>(R.id.item_book_detail_review_content)
    private val tvDate = itemView.findViewById<TextView>(R.id.tv_date)
    val tvLike = itemView.findViewById<TextView>(R.id.tv_like)
    val ivLike = itemView.findViewById<ImageView>(R.id.item_book_detail_iv_like)
    val vDivider = itemView.findViewById<View>(R.id.divider)
    private val reviewData = MutableLiveData<Detail.ReviewBean.DataBean>()
    fun bind(owner: LifecycleOwner, review: Detail.ReviewBean.DataBean) {
        reviewData.value = review
        reviewData.bind(owner) {
            it?.apply {
                ivReviewAvatar.tag = null
                Glide
                        .with(ivReviewAvatar.context)
                        .load(this.avatar).placeholder(R.drawable.default_cover)
                        .error(R.drawable.default_cover)
                        .into(ivReviewAvatar)
                ivReviewAvatar.tag = this.avatar
                tvUserName.text = this.user_name
                rbScore.rating = this.score.toFloat()
                tvContent.text = this.content
                tvDate.text = this.updated_at
                tvLike.text = this.like_count

            }
        }
    }
}