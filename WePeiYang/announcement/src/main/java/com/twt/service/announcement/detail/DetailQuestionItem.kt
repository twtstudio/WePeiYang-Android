package com.twt.service.announcement.detail

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.edu.twt.retrox.recyclerviewdsl.Item
import cn.edu.twt.retrox.recyclerviewdsl.ItemController
import com.twt.service.announcement.R

/**
 * DetailQuestionItem
 * @author TranceDream
 * 这个Item，是问题详情里面学生提出的问题
 * @param title 问题的标题
 * @param content 问题的具体内容
 * @param name 提问者的用户名(也可能是实名上网
 * @param status 问题的状态(指校方有无回复
 * @param time 问题发布的时间
 */
class DetailQuestionItem(
        val title: String,
        val content: String,
        val name: String,
        val status: Int,
        val time: String
        // TODO: 呵呵，你觉得这就完了么
) : Item {
    companion object DetailQuestionItemController : ItemController {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as DetailQuestionItemViewHolder
            item as DetailQuestionItem
            holder.apply {
                titleTv.text = item.title
                contentTv.text = item.content
                timeTv.text = item.time
                nameTv.text = item.name
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.anno_detail_question_item, parent, false)
            val titleTv: TextView = itemView.findViewById(R.id.annoDetailQuestionTitle)
            val contentTv: TextView = itemView.findViewById(R.id.annoDetailQuestionContent)
            val timeTv: TextView = itemView.findViewById(R.id.annoDetailQuestionTime)
            val nameTv: TextView = itemView.findViewById(R.id.annoDetailQuestionName)
            return DetailQuestionItemViewHolder(itemView, titleTv, contentTv, timeTv, nameTv)
        }
    }

    override val controller: ItemController
        get() = DetailQuestionItemController

    private class DetailQuestionItemViewHolder(
            itemView: View,
            val titleTv: TextView,
            val contentTv: TextView,
            val timeTv: TextView,
            val nameTv: TextView
    ) : RecyclerView.ViewHolder(itemView)
}

/**
 * 向Item列表中添加一个[DetailQuestionItem]
 */
fun MutableList<Item>.addDetailQuestionItem(title: String, content: String, name: String, status: Int, time: String) = add(DetailQuestionItem(title, content, name, status, time))