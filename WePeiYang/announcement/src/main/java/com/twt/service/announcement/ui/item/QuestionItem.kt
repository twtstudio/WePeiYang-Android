package com.twt.service.announcement.ui.item

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.service.announcement.R
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController

class QuestionItem(val context: String, var onclick: () -> Unit) : Item {
    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflate = LayoutInflater.from(parent.context)
            val view = inflate.inflate(R.layout.que_item, parent, false)
            val textView = view.findViewById<TextView>(R.id.ques_text)
            return ViewHolder(view, textView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as QuestionItem
            holder.textView.text = item.context
            holder.itemView.setOnClickListener {
                item.onclick.invoke()
                Log.d("QuestionItem Test", item.onclick.toString())
            }
        }

        private class ViewHolder(itemView: View?, val textView: TextView) : RecyclerView.ViewHolder(itemView)
    }
}