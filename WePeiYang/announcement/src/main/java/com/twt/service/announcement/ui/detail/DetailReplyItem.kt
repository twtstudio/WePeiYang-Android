package com.twt.service.announcement.ui.detail

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import cn.edu.twt.retrox.recyclerviewdsl.Item
import cn.edu.twt.retrox.recyclerviewdsl.ItemController
import com.twt.service.announcement.R
import com.twt.service.announcement.service.AnnoPreference
import com.twt.service.announcement.service.AnnoService
import com.twt.service.announcement.service.Reply
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * DetailReplyItem
 * @author TranceDream
 * 这个是显示校方或者管理员回复的Item
 * @param title 该回复所属问题的标题
 * @param reply 该条目显示的回复
 * @param likeState 该用户是否点过赞
 * @param likeCount 该问题的点赞数量
 */
class DetailReplyItem(
        val title: String,
        val reply: Reply,
        var likeState: Boolean,
        var likeCount: Int,
        var function: () -> Unit,
        var isLikable: Boolean = true
        // TODO: 呵呵，你觉得这就完了么
) : Item {
    companion object DetailReplyItemController : ItemController {
        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as DetailReplyItemViewHolder
            item as DetailReplyItem
            holder.apply {
                nameTv.text = item.reply.admin_name
                contentTv.apply {
                    text = Html.fromHtml(item.reply.contain.replace(Regex("<img.*?>"), ""))
                    onClick {
                        item.function.invoke()
                    }
                }
                timeTv.text = item.reply.created_at
                        .split("T", ".")
                        .subList(0, 2).joinToString(separator = " ") {
                            if (it.contains(":"))
                                it.split(":").subList(0, 2).joinToString(separator = ":")
                            else
                                it
                        }
                AnnoPreference.myId?.let {
                    GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                        AnnoService.getLikedState("answer", it, item.reply.id).awaitAndHandle {
//                            Toast.makeText(itemView.context, "请求点赞数据失败",Toast.LENGTH_SHORT).show()
                        }?.data?.let { likeState ->
                            item.likeState = likeState.is_liked
                            when (item.likeState) {
                                true -> likeButtonIv.setImageResource(R.drawable.good_fill)
                                false -> likeButtonIv.setImageResource(R.drawable.good)
                            }
                        }
                    }
                }
                likeCountTv.text = item.likeCount.toString()
                /**
                 * 点赞按钮逻辑
                 * 点击按钮时在本地操作点赞数量
                 * 同时发送请求
                 */
                likeButtonIv.apply {
                    setImageResource(
                            when (item.likeState) {
                                true -> R.drawable.good_fill
                                false -> R.drawable.good
                            }
                    )
                    setOnClickListener {
                        if (item.isLikable && AnnoPreference.myId != null) {
                            item.isLikable = false
                            GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                                AnnoService.postThumbUpOrDown(
                                        "answer",
                                        when (item.likeState) {
                                            true -> "dislike"
                                            false -> "like"
                                        },
                                        item.reply.id,
                                        AnnoPreference.myId!!
                                ).awaitAndHandle {
//                                    Toast.makeText(itemView.context, "点赞状态更新失败",Toast.LENGTH_SHORT).show()
                                    item.isLikable = true
                                }?.data?.let {
                                    likeCountTv.text = it.toString()
                                    item.likeCount = it
                                    item.likeState = !item.likeState
                                    likeButtonIv.setImageResource(when (item.likeState) {
                                        false -> R.drawable.good
                                        true -> R.drawable.good_fill
                                    })
                                    item.isLikable = true
                                }
                            }
                        }
                    }
                }
                if (item.reply.score == -1) {
                    ratingBar.visibility = View.GONE
                    ratingLabelTv.visibility = View.GONE
                } else {
                    ratingBar.rating = item.reply.score.toFloat() / 2f
                    ratingLabelTv.text = "${item.reply.score}分"
                }

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val itemView: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.anno_detail_reply_item, parent, false)
            val nameTv: TextView = itemView.findViewById(R.id.annoDetailReplyName)
            val contentTv: TextView = itemView.findViewById(R.id.annoDetailReplyContent)
            val timeTv: TextView = itemView.findViewById(R.id.annoDetailReplyTime)
            val likeButtonIv: ImageView = itemView.findViewById(R.id.annoDetailReplyLikeButton)
            val likeCountTv: TextView = itemView.findViewById(R.id.annoDetailReplyLikeCount)
            val ratingBar: RatingBar = itemView.findViewById(R.id.annoDetailReplyRatingBar)
            val ratingLabelTv: TextView = itemView.findViewById(R.id.annoDetailReplyRatingLabel)
            return DetailReplyItemViewHolder(itemView, nameTv, contentTv, timeTv, likeButtonIv, likeCountTv, ratingBar, ratingLabelTv)
        }
    }

    override val controller: ItemController
        get() = DetailReplyItemController

    private class DetailReplyItemViewHolder(
            itemView: View,
            val nameTv: TextView,
            val contentTv: TextView,
            val timeTv: TextView,
            val likeButtonIv: ImageView,
            val likeCountTv: TextView,
            val ratingBar: RatingBar,
            val ratingLabelTv: TextView
    ) : RecyclerView.ViewHolder(itemView)
}

/**
 * 向Item列表中添加一个[DetailReplyItem]
 */
fun MutableList<Item>.addDetailReplyItem(
        title: String,
        reply: Reply,
        likeState: Boolean,
        likeCount: Int,
        function: () -> Unit
) = add(DetailReplyItem(title, reply, likeState, likeCount, function))