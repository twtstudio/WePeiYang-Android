package com.twt.service.announcement.ui.main

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.twt.service.announcement.R
import com.twt.service.announcement.service.Question
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater
import java.lang.Exception

class QuestionItem(val context: Context, val questionDetail: Question, var onclick: () -> Unit) : Item {
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

                    userName.text = ques.username
                    userName.paint.isFakeBoldText = true
                    createTime.text = ques.created_at
                            .split("T", ".")
                            .subList(0, 2)
                            .joinToString(separator = " ")

                    createTime.paint.isFakeBoldText = true
                    comCount.text = ques.msgCount.toString()
                    likeCount.text = ques.likes.toString()
                    if (ques.solved == 0) {
                        isAnswer.text = "未解决"
                        isAnswer.setTextColor(Color.RED)
                    } else {
                        isAnswer.text = "解决"
                        isAnswer.setTextColor(Color.GREEN)
                    }

                    if (ques.url_list.isNotEmpty() && ques.thumb_url_list.isNotEmpty()) {
                        picLayout.visibility = View.VISIBLE
                        Log.d("questionimg", ques.url_list.first())
                        Glide.with(item.context)
                                .load(ques.thumb_url_list.first())
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
                                .into(img)
                    } else {
                        picLayout.visibility = View.GONE
                        detail.minLines = 1
                    }
                }
            }




            holder.itemView.setOnClickListener {
                item.onclick.invoke()
                Log.d("QuestionItem Test", item.onclick.toString())
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

        }
    }
}