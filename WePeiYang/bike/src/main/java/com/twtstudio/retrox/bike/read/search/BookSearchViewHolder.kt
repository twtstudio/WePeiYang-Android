package com.twtstudio.retrox.bike.read.search

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.twt.wepeiyang.commons.experimental.extensions.bind
import com.twtstudio.retrox.bike.R
import com.twtstudio.retrox.bike.model.read.SearchBook

/**
 * Created by zhangyulong on 18-1-26.
 */
class BookSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val ivCover = itemView.findViewById<ImageView>(R.id.tv_book_search_cover)
    private val tvTitle = itemView.findViewById<TextView>(R.id.book_search_title)
    private val tvAuthor = itemView.findViewById<TextView>(R.id.tv_book_search_author)
    private val tvPublisher = itemView.findViewById<TextView>(R.id.tv_book_search_publisher)
    private val tvYear = itemView.findViewById<TextView>(R.id.tv_book_search_year)
    private val tvRate = itemView.findViewById<TextView>(R.id.tv_book_search_rate)
    val llSearchParent = itemView.findViewById<LinearLayout>(R.id.item_book_search_parent)
    private val bookData = MutableLiveData<SearchBook>()
    fun bind(owner: LifecycleOwner, book: SearchBook) {
        bookData.value = book
        bookData.bind(owner) {
            it?.apply {
                ivCover.tag = null
                Glide
                        .with(ivCover.context)
                        .load(this.cover).placeholder(R.drawable.default_cover)
                        .error(R.drawable.default_cover)
                        .into(ivCover)
                ivCover.tag = this.cover

                tvTitle.text = this.title
                tvAuthor.text = "作者: " + this.author
                tvPublisher.text = "出版社: " + this.publisher
                tvYear.text = "出版日期: " + this.year
                tvRate.text = "评分: " + if (this.rate != null) this.rate else "暂无"
            }
        }
    }
}