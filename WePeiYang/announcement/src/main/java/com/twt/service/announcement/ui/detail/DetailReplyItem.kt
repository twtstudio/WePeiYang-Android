package com.twt.service.announcement.ui.detail

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cn.edu.twt.retrox.recyclerviewdsl.Item
import cn.edu.twt.retrox.recyclerviewdsl.ItemController
import com.twt.service.announcement.R
import com.twt.service.announcement.service.Reply

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
        var likeCount: Int
        // TODO: 呵呵，你觉得这就完了么
) : Item {
    companion object DetailReplyItemController : ItemController {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as DetailReplyItemViewHolder
            item as DetailReplyItem
            holder.apply {
                nameTv.text = item.reply.user_name
                contentTv.apply {
                    text = item.reply.contain
                    setOnClickListener {
                        val mIntent: Intent = Intent(itemView.context, ReplyActivity::class.java)
                                .putExtra("title", item.title)
//                                .putExtra("reply", item.reply)
                    }
                }
                timeTv.text = item.reply.created_at
                likeCountTv.text = item.likeCount.toString()
                /**
                 * 点赞按钮逻辑
                 * 点击按钮时在本地操作点赞数量
                 * 同时发送请求
                 */
                likeButtonIv.apply {
                    if (item.likeState) {
                        setImageResource(R.drawable.thumb_up_black)
                    } else {
                        setImageResource(R.drawable.thumb_up)
                    }
                    setOnClickListener {
                        if (item.likeState) {
                            setImageResource(R.drawable.thumb_up)
                            item.likeCount--
                            likeCountTv.text = item.likeCount.toString()
                            item.likeState = !item.likeState
                        } else {
                            setImageResource(R.drawable.thumb_up_black)
                            item.likeCount++
                            likeCountTv.text = item.likeCount.toString()
                            item.likeState = !item.likeState
                        }
                    }
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
            return DetailReplyItemViewHolder(itemView, nameTv, contentTv, timeTv, likeButtonIv, likeCountTv)
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
            val likeCountTv: TextView
    ) : RecyclerView.ViewHolder(itemView)
}

/**
 * 向Item列表中添加一个[DetailReplyItem]
 */
fun MutableList<Item>.addDetailReplyItem(
        title: String,
        reply: Reply,
        likeState: Boolean,
        likeCount: Int) = add(DetailReplyItem(title, reply, likeState, likeCount))