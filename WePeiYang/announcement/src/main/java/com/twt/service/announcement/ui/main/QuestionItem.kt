package com.twt.service.announcement.ui.main

import android.content.Context
import android.graphics.Color
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.edu.twt.retrox.recyclerviewdsl.withItems
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.twt.service.announcement.R
import com.twt.service.announcement.service.Question
import com.twt.service.announcement.ui.detail.addTagList
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater

class QuestionItem(val context: Context,
                   val questionDetail: Question,
                   val onClick: () -> Unit,
                   val onLongClick: () -> Unit = { }
) : Item {
    override fun areContentsTheSame(newItem: Item): Boolean {
        return questionDetail.id == (newItem as QuestionItem).questionDetail.id
    }

    override fun areItemsTheSame(newItem: Item): Boolean = areContentsTheSame(newItem)

    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.que_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as QuestionItem

            val typeface = ResourcesCompat.getFont(item.context, R.font.simkai)

            item.questionDetail.let { ques ->
                holder.apply {
                    title.text = ques.name
                    title.paint.isFakeBoldText = true
                    title.setTextColor(Color.BLACK)
//                    typeface?.let { title.typeface = it }
                    detail.text = ques.description

                    tagList.apply {
                        layoutManager = LinearLayoutManager(item.context).apply {
                            orientation = LinearLayoutManager.HORIZONTAL
                        }
                        withItems {
                            if (item.questionDetail.tags.isNotEmpty()) {
                                tagList.visibility = View.VISIBLE
                                item.questionDetail.tags.sortedBy {
                                    it.id
                                }.map {
                                    if (it.id != 3) {
                                        addTagList(it.name)
                                    }
                                }
                            } else {
                                tagList.visibility = View.VISIBLE
                                addTagList("未分类")
                            }
                        }
                    }

                    userName.text = ques.username
                    userName.paint.isFakeBoldText = true
                    createTime.text = ques.created_at
                            .split("T", ".")
                            .subList(0, 2).joinToString(separator = " ") {
                                if (it.contains(":"))
                                    it.split(":").subList(0, 2).joinToString(separator = ":")
                                else
                                    it
                            }

                    createTime.paint.isFakeBoldText = true
                    comCount.text = ques.msgCount.toString()
                    likeCount.text = ques.likes.toString()
                    if (ques.solved == 0) {
                        isAnswer.text = "未回复"
                        isAnswer.setTextColor(Color.RED)
                    } else {
                        isAnswer.text = "已回复"
                        isAnswer.setTextColor(Color.GREEN)
                    }

                    if (ques.url_list.isNotEmpty() && ques.thumb_url_list.isNotEmpty()) {
                        picLayout.visibility = View.VISIBLE
                        detail.minLines = 2
                        Log.d("questionimg", ques.url_list.first())
                        Glide.with(item.context)
                                .load(ques.thumb_url_list.first())
                                .fitCenter()
                                .listener(object : RequestListener<String, GlideDrawable> {
                                    override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                                        //图片加载失败
//                                        Toast.makeText(item.context, "图片加载失败", Toast.LENGTH_SHORT).show()
                                        picLayout.visibility = View.INVISIBLE
                                        return false
                                    }

                                    override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                                        return false
                                    }

                                })
                                .into(img)
                    } else {
                        picLayout.visibility = View.GONE
                        detail.minLines = 1
                    }
                }
            }




            holder.itemView.setOnClickListener {
                item.onClick.invoke()
                Log.d("QuestionItem Test", item.onClick.toString())
            }

            holder.itemView.setOnLongClickListener {
                item.onLongClick.invoke()
                true
            }
        }

        private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.ques_title)
            val detail: TextView = itemView.findViewById(R.id.ques_detail_text)
            val userName: TextView = itemView.findViewById(R.id.ques_user_name)
            val createTime: TextView = itemView.findViewById(R.id.ques_create_time)
            val comCount: TextView = itemView.findViewById(R.id.ques_comment_count)
            val likeCount: TextView = itemView.findViewById(R.id.ques_like_count)
            val isAnswer: TextView = itemView.findViewById(R.id.ques_is_admin_answer)
            val img: ImageView = itemView.findViewById(R.id.ques_detail_img)
            val picLayout: LinearLayout = itemView.findViewById(R.id.pic_layout)
            val tagList: RecyclerView = itemView.findViewById(R.id.anno_ques_tags)
        }
    }
}