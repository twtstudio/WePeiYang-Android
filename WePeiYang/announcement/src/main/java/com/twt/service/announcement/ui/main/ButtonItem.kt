package com.twt.service.announcement.ui.main

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.twt.service.announcement.R
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater

enum class ViewType {
    PROCESS_BAR, BOTTOM_TEXT
}

class ButtonItem(val viewStyle: ViewType = ViewType.BOTTOM_TEXT) : Item {
    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as ButtonItem
            when (item.viewStyle) {
                ViewType.BOTTOM_TEXT -> {
                    holder.pgBar.visibility = View.GONE
                    holder.text.visibility = View.VISIBLE
                    holder.text.text = "没有更多了"
                }
                ViewType.PROCESS_BAR -> {
                    holder.text.visibility = View.GONE
                    holder.pgBar.visibility = View.VISIBLE

                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.button_item, parent, false)
            return ViewHolder(view)
        }

        private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val text: TextView = itemView.findViewById(R.id.no_more_text)
            val pgBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
        }
    }
}