package com.twt.service.announcement.ui.main

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.service.announcement.R
import com.twt.service.announcement.service.Question
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater

class QuestionItem(val questionDetail: Question, var onclick: () -> Unit) : Item {
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
            holder.title.text = item.questionDetail.name
            holder.apply {
                title.text = item.questionDetail.name
                detail.text = item.questionDetail.description
                userId.text = item.questionDetail.user_id.toString()
                createTime.text = item.questionDetail.created_at.split("T", ".").subList(0, 2).joinToString(separator = " ")
                comCount.text = "后台没给数据"
                likeCount.text = item.questionDetail.likes.toString()
                isAnswer.text = if (item.questionDetail.solved == 0) "官方未解决" else "官方已解决"
            }

            holder.itemView.setOnClickListener {
                item.onclick.invoke()
                Log.d("QuestionItem Test", item.onclick.toString())
            }
        }

        private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.ques_title)
            val detail: TextView = itemView.findViewById(R.id.ques_detail_text)
            val userId: TextView = itemView.findViewById(R.id.ques_user_id)
            val createTime: TextView = itemView.findViewById(R.id.ques_create_time)
            val comCount: TextView = itemView.findViewById(R.id.ques_comment_count)
            val likeCount: TextView = itemView.findViewById(R.id.ques_like_count)
            val isAnswer: TextView = itemView.findViewById(R.id.ques_is_admin_answer)
        }
    }
}