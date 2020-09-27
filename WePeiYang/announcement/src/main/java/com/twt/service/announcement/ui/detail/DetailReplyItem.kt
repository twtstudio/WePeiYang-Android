package com.twt.service.announcement.ui.detail

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.text.Html
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
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        var onRefresh: () -> Unit,
        var isLikable: Boolean = true
        // TODO: 呵呵，你觉得这就完了么
) : Item {
    companion object DetailReplyItemController : ItemController {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as DetailReplyItemViewHolder
            item as DetailReplyItem
            holder.apply {
                nameTv.text = item.reply.user_name
                contentTv.apply {
                    text = Html.fromHtml(item.reply.contain.replace(Regex("<img.*?>"), ""))
                    setOnClickListener {
                        val mIntent: Intent = Intent(itemView.context, ReplyActivity::class.java)
                                .putExtra("title", item.title)
                                .putExtra("reply", item.reply)
                        itemView.context.startActivity(mIntent)
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
                    if (item.isLikable) {
                        item.isLikable = false
                        if (item.likeState) {
                            GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                                AnnoService.postThumbUpOrDown("answer", "dislike", item.reply.id, AnnoPreference.myId!!).awaitAndHandle {
                                    Toasty.error(context, "点赞错误").show()
                                }?.ErrorCode?.let {
                                    if (it == 0) {
                                        Toasty.success(context, "成功").show()
                                        setImageResource(R.drawable.thumb_up)
                                        item.likeCount--
                                        likeCountTv.text = item.likeCount.toString()
                                        item.likeState = !item.likeState
                                        item.isLikable = true
                                        item.onRefresh.invoke()
                                    }
                                }
                            }.invokeOnCompletion {
                            }
                        } else {
                            GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                                AnnoService.postThumbUpOrDown("answer", "like", item.reply.id, AnnoPreference.myId!!).awaitAndHandle {
                                    Toasty.error(context, "点赞错误").show()
                                }?.ErrorCode?.let {
                                    if (it == 0) {
                                        Toasty.success(context, "成功").show()
                                        setImageResource(R.drawable.thumb_up)
                                        item.likeCount--
                                        likeCountTv.text = item.likeCount.toString()
                                        item.likeState = !item.likeState
                                        item.isLikable = true
                                        item.onRefresh
                                    }
                                }
                            }.invokeOnCompletion {
                            }
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
        likeCount: Int,
        onRefresh: () -> Unit
) = add(DetailReplyItem(title, reply, likeState, likeCount, onRefresh))