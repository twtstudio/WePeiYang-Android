package com.twt.service.announcement.ui.detail

import android.graphics.Color
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
import com.twt.service.announcement.service.Question
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * DetailQuestionItem
 * @author TranceDream
 * 这个Item，是问题详情里面学生提出的问题
 * @param question 传进来的问题
 * @param likeState 该问题的点赞情况
 * @param likeCount 该问题的点赞数量
 * @param isLikeable 是否能够点赞(狂暴Typo轰入DetailQuestionItem  (* 愤怒 *
 * @param onComment 这里是评论按钮的点击事件
 */
class DetailQuestionItem(
        val question: Question,
        var likeState: Boolean,
        val onRefresh: () -> Unit,
        var likeCount: Int = question.likes,
        var isLikeable: Boolean = true,
        val onComment: () -> Unit
) : Item {
    companion object DetailQuestionItemController : ItemController {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as DetailQuestionItemViewHolder
            item as DetailQuestionItem
            holder.apply {
                titleTv.text = item.question.name
                contentTv.text = item.question.description
                timeTv.text = item.question.created_at
                nameTv.text = item.question.username
                statusTv.text = when (item.question.solved) {
                    0 -> "校方未回复"
                    else -> "校方已回复"
                }
                statusTv.setTextColor(
                        when (item.question.solved) {
                            0 -> Color.parseColor("#FF5722")
                            else -> Color.parseColor("#00FF00")
                        }
                )
                likeCountTv.text = item.likeCount.toString()
                /**
                 * 点赞按钮逻辑
                 * 点击按钮时在本地操作点赞数量
                 * 同时发送请求
                 * TODO:
                 */
                likeButtonIv.apply {
                    if (item.likeState) {
                        setImageResource(R.drawable.thumb_up_black)
                    } else {
                        setImageResource(R.drawable.thumb_up)
                    }
                    setOnClickListener {
                        if (item.isLikeable) {
                            item.isLikeable = false
                            if (item.likeState) {
                                GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                                    AnnoService.postThumbUpOrDown("question", "dislike", item.question.id, AnnoPreference.myId!!).awaitAndHandle {
                                        Toasty.error(context, "点赞错误").show()
                                    }?.ErrorCode?.let {
                                        if (it == 0) {
                                            Toasty.success(context, "成功").show()
                                            setImageResource(R.drawable.thumb_up)
                                            item.likeCount--
                                            likeCountTv.text = item.likeCount.toString()
                                            item.likeState = !item.likeState
                                            item.isLikeable = true
                                            item.onRefresh.invoke()
                                        }
                                    }
                                }.invokeOnCompletion {
                                }
                            } else {
                                GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                                    AnnoService.postThumbUpOrDown("question", "like", item.question.id, AnnoPreference.myId!!).awaitAndHandle {
                                        Toasty.error(context, "点赞错误").show()
                                    }?.ErrorCode?.let {
                                        if (it == 0) {
                                            Toasty.success(context, "成功").show()
                                            setImageResource(R.drawable.thumb_up)
                                            item.likeCount--
                                            likeCountTv.text = item.likeCount.toString()
                                            item.likeState = !item.likeState
                                            item.isLikeable = true
                                            item.onRefresh
                                        }
                                    }
                                }.invokeOnCompletion {
                                }
                            }
                        }
                    }
                }
                commentButtonIv.onClick {
                    item.onComment.invoke()
                }
                commentLabelTv.onClick {
                    item.onComment.invoke()
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.anno_detail_question_item, parent, false)
            val titleTv: TextView = itemView.findViewById(R.id.annoDetailQuestionTitle)
            val contentTv: TextView = itemView.findViewById(R.id.annoDetailQuestionContent)
            val timeTv: TextView = itemView.findViewById(R.id.annoDetailQuestionTime)
            val nameTv: TextView = itemView.findViewById(R.id.annoDetailQuestionName)
            val statusTv: TextView = itemView.findViewById(R.id.annoDetailQuestionStatus)
            val likeButtonIv: ImageView = itemView.findViewById(R.id.annoDetailQuestionLikeButton)
            val likeCountTv: TextView = itemView.findViewById(R.id.annoDetailQuestionLikeCount)
            val commentButtonIv: ImageView = itemView.findViewById(R.id.annoDetailQuestionCommentButton)
            val commentLabelTv: TextView = itemView.findViewById(R.id.annoDetailQuestionCommentLabel)
            return DetailQuestionItemViewHolder(itemView, titleTv, contentTv, timeTv, nameTv, statusTv, likeButtonIv, likeCountTv, commentButtonIv, commentLabelTv)
        }
    }

    override val controller: ItemController
        get() = DetailQuestionItemController

    private class DetailQuestionItemViewHolder(
            itemView: View,
            val titleTv: TextView,
            val contentTv: TextView,
            val timeTv: TextView,
            val nameTv: TextView,
            val statusTv: TextView,
            val likeButtonIv: ImageView,
            val likeCountTv: TextView,
            val commentButtonIv: ImageView,
            val commentLabelTv: TextView
    ) : RecyclerView.ViewHolder(itemView)
}

/**
 * 向Item列表中添加一个[DetailQuestionItem]
 */
fun MutableList<Item>.addDetailQuestionItem(
        question: Question,
        likeState: Boolean,
        onRefresh: () -> Unit,
        onComment: () -> Unit
) = add(
        DetailQuestionItem(
                question, likeState, onRefresh
        ) {
            onComment.invoke()
        }
)