package com.twt.service.theory.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.service.theory.R
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater

class MessageItem : Item{
    private companion object Controller : ItemController {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ExamItemViewHolder
            item as ExamItem
            holder.apply {
                name.text = "一次小考试"
                content.text = "一次小考试"
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.theory_item_announce, parent, false)

            return ExamItemViewHolder(view)
        }


    }

    private class ExamItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.eaxm_announce_name)
        val content = itemView.findViewById<TextView>(R.id.eaxm_announce_detail)
    }
    override val controller: ItemController
        get() = MessageItem
}

fun MutableList<Item>.setMessage() = add(MessageItem())