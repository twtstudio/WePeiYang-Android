package com.twt.service.announcement.ui.detail

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cn.edu.twt.retrox.recyclerviewdsl.Item
import cn.edu.twt.retrox.recyclerviewdsl.ItemController
import com.twt.service.announcement.R
import com.twt.service.announcement.service.AnnoPreference
import com.twt.service.announcement.service.AnnoService
import com.twt.service.announcement.service.Reply
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.zzhoujay.richtext.ImageHolder
import com.zzhoujay.richtext.RichText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * ReplyItem
 * @author TranceDream
 * 这个是[ReplyActivity]里面的第一个用来显示回复详情的Item
 * (混乱起名
 * @param reply 把要显示的回复传进来
 * @param likeState 从上级页面传来的点赞情况
 * @param likeCount 从[reply]中提取点赞数
 */
class ReplyItem(
        val title: String,
        val reply: Reply,
        var likeState: Boolean,
        var likeCount: Int = reply.likes,
        val onRefresh: () -> Unit,
        var isLikable: Boolean = true
) : Item {
    companion object ReplyItemController : ItemController {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ReplyItemViewHolder
            item as ReplyItem
            holder.apply {
                titleTv.text = item.title
                nameTv.text = item.reply.admin_name
                timeTv.text = item.reply.created_at
                        .split("T", ".")
                        .subList(0, 2).joinToString(separator = " ") {
                            if (it.contains(":"))
                                it.split(":").subList(0, 2).joinToString(separator = ":")
                            else
                                it
                        }
                RichText.initCacheDir(itemView.context)
                RichText.fromHtml(item.reply.contain).scaleType(ImageHolder.ScaleType.fit_auto).into(contentTv)
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
//                                    Toasty.error(itemView.context, "点赞状态更新失败").show()
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
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val itemView: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.anno_reply_item, parent, false)
            val titleTv: TextView = itemView.findViewById(R.id.annoReplyTitle)
            val nameTv: TextView = itemView.findViewById(R.id.annoReplyName)
            val timeTv: TextView = itemView.findViewById(R.id.annoReplyTime)
            val contentTv: TextView = itemView.findViewById(R.id.annoReplyContent)
            val likeButtonIv: ImageView = itemView.findViewById(R.id.annoReplyLikeButton)
            val likeCountTv: TextView = itemView.findViewById(R.id.annoReplyLikeCount)
            return ReplyItemViewHolder(itemView, titleTv, nameTv, timeTv, contentTv, likeButtonIv, likeCountTv)
        }
    }

    override val controller: ItemController
        get() = ReplyItemController

    private class ReplyItemViewHolder(
            itemView: View,
            val titleTv: TextView,
            val nameTv: TextView,
            val timeTv: TextView,
            val contentTv: TextView,
            val likeButtonIv: ImageView,
            val likeCountTv: TextView
    ) : RecyclerView.ViewHolder(itemView)
}

fun MutableList<Item>.addReplyItem(
        title: String,
        reply: Reply,
        likeState: Boolean,
        likeCount: Int,
        onRefresh: () -> Unit
) = add(ReplyItem(title, reply, likeState, likeCount, onRefresh))