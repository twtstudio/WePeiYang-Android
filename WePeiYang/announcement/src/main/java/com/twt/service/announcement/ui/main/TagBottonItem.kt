package com.twt.service.announcement.ui.main

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.service.announcement.R
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater

class TagBottomItem(val content: String, val index:Int, var onclick:()-> Unit) : Item {
    override fun areContentsTheSame(newItem: Item): Boolean {
        return content == (newItem as TagBottomItem).content && index == newItem.index
    }

    override fun areItemsTheSame(newItem: Item): Boolean = areContentsTheSame(newItem)

    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.tag_bottom, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as TagBottomItem
            holder.textView.text = item.content
            holder.itemView.setOnClickListener {
                item.onclick.invoke()
                Log.d("TagBottom Test", item.onclick.toString())
            }
        }

        private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(R.id.btn_text)
        }
    }
}