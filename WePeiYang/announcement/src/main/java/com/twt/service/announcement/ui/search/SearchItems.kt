package com.twt.service.announcement.ui.search

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.service.announcement.R
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater


class SearchHistoryItem(val str: String, val block: (String) -> Unit) : Item {

    override fun areContentsTheSame(newItem: Item): Boolean {
        return str == (newItem as SearchHistoryItem).str
    }

    override fun areItemsTheSame(newItem: Item): Boolean = areContentsTheSame(newItem)

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.search_history_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            item as SearchHistoryItem
            holder as ViewHolder
            holder.textView.text = item.str
            holder.itemView.setOnClickListener {
                item.block(item.str)
            }
        }
    }

    override val controller: ItemController
        get() = Controller

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.history_text)
    }

}

class DeleteHistoryItem(val block: () -> Unit) : Item {

    companion object Controller : ItemController {

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as DeleteHistoryItem
            holder.itemView.setOnClickListener {
                item.block()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.search_history_delete_item, parent, false)
            return ViewHolder(view)
        }
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    override val controller: ItemController
        get() = Controller
}