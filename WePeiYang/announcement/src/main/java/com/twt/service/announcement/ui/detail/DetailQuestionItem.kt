package com.twt.service.announcement.ui.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import cn.edu.twt.retrox.recyclerviewdsl.Item
import cn.edu.twt.retrox.recyclerviewdsl.ItemController
import cn.edu.twt.retrox.recyclerviewdsl.withItems
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jaeger.ninegridimageview.NineGridImageView
import com.jaeger.ninegridimageview.NineGridImageViewAdapter
import com.twt.service.announcement.R
import com.twt.service.announcement.service.AnnoPreference
import com.twt.service.announcement.service.AnnoService
import com.twt.service.announcement.service.Question
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.sdk27.coroutines.onClick


/**
 * DetailQuestionItem
 * @author TranceDream
 * 这个Item，是问题详情里面学生提出的问题
 * @param question 传进来的问题
 * @param likeState 该问题的点赞情况
 * @param likeCount 该问题的点赞数量
 * @param isLikable 是否能够点赞(狂暴Typo轰入DetailQuestionItem  (* 愤怒 *
 * @param onComment 这里是评论按钮的点击事件
 */
class DetailQuestionItem(
        val context: Context,
        val question: Question,
        var likeState: Boolean,
        val onRefresh: () -> Unit,
        var likeCount: Int = question.likes,
        var isCommentEnabled: Boolean = true,
        var isLikable: Boolean = true,
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
                        .split("T", ".")
                        .subList(0, 2).joinToString(separator = " ") {
                            if (it.contains(":"))
                                it.split(":").subList(0, 2).joinToString(separator = ":")
                            else
                                it
                        }
//                nameTv.text = item.question.username
                nameTv.text = "匿名"
                nameTv.maxEms = 10
                statusTv.text = when (item.question.solved) {
                    0 -> {
                        statusTv.setTextColor(Color.RED)
                        "未回复"
                    }
                    else -> {
                        statusTv.setTextColor(Color.GREEN)
                        "已回复"
                    }
                }
                statusTv.setTextColor(
                        when (item.question.solved) {
                            0 -> Color.parseColor("#FF5722")
                            else -> Color.parseColor("#00FF00")
                        }
                )


                tagListRec.apply {
                    layoutManager = LinearLayoutManager(item.context).apply {
                        orientation = LinearLayoutManager.HORIZONTAL
                    }
                    withItems {
                        if (item.question.tags.isNotEmpty()) {
                            tagListRec.visibility = View.VISIBLE
                            item.question.tags.sortedBy {
                                it.id
                            }.map {
                                if (it.id != 3) {
                                    addTagList(it.name)
                                }
                            }
                        } else {
                            tagListRec.visibility = View.VISIBLE
                            addTagList("未分类")
                        }

                    }
                }

                AnnoPreference.myId?.let {
                    GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                        AnnoService.getLikedState("question", it, item.question.id).awaitAndHandle {
//                            Toasty.error(itemView.context, "请求点赞数据失败").show()
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

                AnnoPreference.myId?.let { user_id ->
                    GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                        AnnoService.getDetailQuestion(id = item.question.id, user_id = user_id).awaitAndHandle {
                            Log.d("getDetailQuestion error", it.message)
                        }?.let {
                            if (it.ErrorCode == 0 && it.data?.likes != null) {
                                likeCountTv.text = it.data.likes.toString()
                            }
                        }
                    }
                }

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
                                        "question",
                                        when (item.likeState) {
                                            true -> "dislike"
                                            false -> "like"
                                        },
                                        item.question.id,
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

                commentButtonIv.onClick {
                    if (item.isCommentEnabled) {
                        item.isCommentEnabled = false
                        item.onComment.invoke()
                        item.isCommentEnabled = true

                    }
                }
                commentLabelTv.onClick {
                    if (item.isCommentEnabled) {
                        item.isCommentEnabled
                        item.onComment.invoke()
                    }
                }
                val myAdapter: NineGridImageViewAdapter<String> = object : NineGridImageViewAdapter<String>() {
                    override fun onDisplayImage(context: Context?, imageView: ImageView?, t: String?) {
                        // TODO:让后端返回压缩后的图片，在点击查看大图时，再显示原图
                        t?.let {
                            Glide.with(context)
                                    .load(it)
//                                .skipMemoryCache(true)
                                    .crossFade()
                                    .fitCenter()
                                    .listener(object : RequestListener<String, GlideDrawable> {
                                        override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                                            //图片加载失败
                                            Toast.makeText(item.context, "图片加载失败", Toast.LENGTH_SHORT).show()
                                            return false
                                        }

                                        override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                                            return false
                                        }

                                    })
                                    .into(imageView)
                        }
                    }

                    override fun onItemImageClick(context: Context?, index: Int, list: MutableList<String>?) {
                        showDialogOfPic(item.question.url_list.elementAt(index), item.context)
                    }

                    private fun showDialogOfPic(url: String, mainPage: Context) {
                        val dialog = Dialog(itemView.context, R.style.edit_AlertDialog_style)
                        dialog.apply {
                            setContentView(R.layout.big_image_layout)
                            val imageView = findViewById<ImageView>(R.id.annoBigImage)
                            Glide.with(context)
                                    .load(url)
                                    .crossFade()
                                    .fitCenter()
                                    .listener(object : RequestListener<String, GlideDrawable> {
                                        override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                                            //如果加载失败，就等一秒，然后dismiss
                                            GlobalScope.launch(Dispatchers.Main) {
                                                delay(1000)
                                                dismiss()
                                            }
//                                            Toast.makeText(mainPage, "图片加载失败", Toast.LENGTH_SHORT).show()
                                            //这里返回true表示事件已经消化了，不会往下传递，返回false表示没有消耗
                                            //如果设置为true error(int resid)设置异常占位图将会失效
                                            return false
                                        }

                                        override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                                            //这里返回true表示事件已经消化了，不会往下传递，返回false表示没有消耗
                                            //设置为ture了，就不会调用Target的onResourceReady()方法了
                                            return false
                                        }

                                    })
                                    .into(imageView)

                            setCanceledOnTouchOutside(true)
                            window?.let {
                                val lp = it.attributes
                                lp.x = 4
                                lp.y = 4
                                dialog.onWindowAttributesChanged(lp)
                                imageView.setOnClickListener { dismiss() }
                                show()
                            }
                        }
                    }
                }
                nineGridImageView.setAdapter(myAdapter)
                nineGridImageView.setGap(20)
                nineGridImageView.setSingleImgSize(800)
                if (item.question.thumb_url_list.isNotEmpty()) {
                    if (item.question.thumb_url_list.size == item.question.url_list.size) {
                        nineGridImageView.setImagesData(item.question.thumb_url_list)
                    } else {
                        Toast.makeText(item.context, "服务器数据出错", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    nineGridImageView.visibility = View.GONE
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
            val nineGridImageView: NineGridImageView<String> = itemView.findViewById(R.id.annoDetailQuestionImages)
            val tagListRec: RecyclerView = itemView.findViewById(R.id.ques_tags)
            return DetailQuestionItemViewHolder(itemView, titleTv, contentTv, timeTv, nameTv, statusTv, likeButtonIv, likeCountTv, commentButtonIv, commentLabelTv, tagListRec, nineGridImageView)
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
            val commentLabelTv: TextView,
            val tagListRec: RecyclerView,
            val nineGridImageView: NineGridImageView<String>
    ) : RecyclerView.ViewHolder(itemView)
}

/**
 * 向Item列表中添加一个[DetailQuestionItem]
 */
fun MutableList<Item>.addDetailQuestionItem(
        context: Context,
        question: Question,
        likeState: Boolean,
        onRefresh: () -> Unit,
        onComment: () -> Unit
) = add(
        DetailQuestionItem(
                context, question, likeState, onRefresh
        ) {
            onComment.invoke()
        }
)

class TagListItem(val text: String) : Item {
    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as TagListItem
            holder.text.text = "   ${item.text}   "
        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.tag_list_item, parent, false)
            return ViewHolder(view)
        }

        private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val text: TextView = itemView.findViewById(R.id.tag_list_text)
        }
    }
}

fun MutableList<Item>.addTagList(text: String) = add(TagListItem(text))