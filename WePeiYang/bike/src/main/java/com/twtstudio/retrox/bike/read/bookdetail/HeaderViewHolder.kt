package com.twtstudio.retrox.bike.read.bookdetail

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.twt.wepeiyang.commons.experimental.extensions.bind
import com.twtstudio.retrox.bike.R
import com.twtstudio.retrox.bike.model.read.Detail

/**
 * Created by zhangyulong on 18-1-26.
 */
class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val detailData = MutableLiveData<Detail>()
    private val tvTitle = itemView.findViewById<TextView>(R.id.tv_detail_title)
    private val tvAuthor = itemView.findViewById<TextView>(R.id.tv_detail_author)
    private val tvPublisher = itemView.findViewById<TextView>(R.id.tv_detail_publisher)
    private val tvTime = itemView.findViewById<TextView>(R.id.tv_detail_time)
    private val tvSummary = itemView.findViewById<TextView>(R.id.tv_detail_summary)
    val mFrame = itemView.findViewById<FrameLayout>(R.id.item_book_detail_header_frame)
    val mCoverImage = itemView.findViewById<ImageView>(R.id.item_book_detail_header_cover)
    val btLove = itemView.findViewById<Button>(R.id.book_detail_btn_love)
    val btAddReview = itemView.findViewById<Button>(R.id.book_detail_btn_addreview)
    fun bind(owner: LifecycleOwner, detail: Detail) {
        detailData.value = detail
        detailData.bind(owner) {
            it?.apply {
                tvTitle.text = this.title
                tvAuthor.text = "作者: " + this.author
                tvPublisher.text = "出版社: " + this.publisher
                tvTime.text = "出版年份: " + this.time
                tvSummary.text = if (this.summary != null) this.summary else "no summary"
            }
        }
    }
}