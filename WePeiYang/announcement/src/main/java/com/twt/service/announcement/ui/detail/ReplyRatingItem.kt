package com.twt.service.announcement.ui.detail

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import cn.edu.twt.retrox.recyclerviewdsl.Item
import cn.edu.twt.retrox.recyclerviewdsl.ItemController
import com.twt.service.announcement.R
import com.twt.service.announcement.service.AnnoService
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import org.jetbrains.anko.sdk27.coroutines.onClick

class ReplyRatingItem(
        val userId: Int,
        val id: Int,
        var score: Int
) : Item {
    companion object ReplyRatingItemController : ItemController {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ReplyRatingItemViewHolder
            item as ReplyRatingItem
            holder.apply {
                if (item.score != -1) {
                    ratingBar.rating = item.score.toFloat() / 2f
                }
                button.onClick(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                    AnnoService.evaluateAnswer(3/*TODO*/, item.id, (ratingBar.rating * 2).toInt(), "test"/* 为什么事commit为什么事commit为什么事commit */).awaitAndHandle {
                        Toasty.error(itemView.context, "拉取数据失败，请稍后再试").show()
                    }?.msg?.let {
                        Toasty.success(itemView.context, "评价成功").show()
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val itemView: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.anno_reply_rating_item, parent, false)
            val ratingBar: RatingBar = itemView.findViewById(R.id.annoReplyRatingBar)
            val button: Button = itemView.findViewById(R.id.annoReplyRatingButton)
            return ReplyRatingItemViewHolder(itemView, ratingBar, button)
        }
    }

    override val controller: ItemController
        get() = ReplyRatingItemController

    private class ReplyRatingItemViewHolder(
            itemView: View,
            val ratingBar: RatingBar,
            val button: Button
    ) : RecyclerView.ViewHolder(itemView)
}

fun MutableList<Item>.addReplyRatingItem(userId: Int, id: Int, score: Int) = add(ReplyRatingItem(userId, id, score))