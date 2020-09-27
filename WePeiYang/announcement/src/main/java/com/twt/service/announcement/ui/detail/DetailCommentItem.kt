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
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * DetailReplyItem
 * @author TranceDream
 * 这个是显示学生评论的Item
 * @param name 用户名称
 * @param content 回复内容
 * @param time 回复时间
 * @param likeState 该用户是否点过赞
 * @param likeCount 该问题的点赞数量
 */
class DetailCommentItem(
        val id: Int,
        val name: String,
        val content: String,
        val time: String,
        var likeState: Boolean,
        var likeCount: Int,
        var onRefresh: () -> Unit,
        var isLikable: Boolean = true
        // TODO: 呵呵，你觉得这就完了么
) : Item {
    companion object DetailCommentItemController : ItemController {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as DetailCommentItemViewHolder
            item as DetailCommentItem
            holder.apply {
                nameTv.text = item.name
                contentTv.apply {
                    text = item.content
                    setOnClickListener {
                        // TODO: 这里跳转到评论页面
                    }
                }
                timeTv.text = item.time
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
                        if (item.isLikable) {
                            item.isLikable = false
                            if (item.likeState) {
                                GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                                    AnnoService.postThumbUpOrDown("question", "dislike", item.id, AnnoPreference.myId!!).awaitAndHandle {
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
                                    AnnoService.postThumbUpOrDown("question", "like", item.id, AnnoPreference.myId!!).awaitAndHandle {
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
        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val itemView: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.anno_detail_comment_item, parent, false)
            val nameTv: TextView = itemView.findViewById(R.id.annoDetailCommentName)
            val contentTv: TextView = itemView.findViewById(R.id.annoDetailCommentContent)
            val timeTv: TextView = itemView.findViewById(R.id.annoDetailCommentTime)
            val likeButtonIv: ImageView = itemView.findViewById(R.id.annoDetailCommentLikeButton)
            val likeCountTv: TextView = itemView.findViewById(R.id.annoDetailCommentLikeCount)
            return DetailCommentItemViewHolder(itemView, nameTv, contentTv, timeTv, likeButtonIv, likeCountTv)
        }
    }

    override val controller: ItemController
        get() = DetailCommentItemController

    private class DetailCommentItemViewHolder(
            itemView: View,
            val nameTv: TextView,
            val contentTv: TextView,
            val timeTv: TextView,
            val likeButtonIv: ImageView,
            val likeCountTv: TextView
    ) : RecyclerView.ViewHolder(itemView)
}

/**
 * 向Item列表中添加一个[DetailCommentItem]
 */
fun MutableList<Item>.addDetailCommentItem(
        id: Int,
        name: String,
        content: String,
        time: String,
        likeState: Boolean,
        likeCount: Int,
        onRefresh: () -> Unit
) = add(DetailCommentItem(id, name, content, time, likeState, likeCount, onRefresh))