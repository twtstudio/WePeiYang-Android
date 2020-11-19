package com.twt.service.announcement.ui.main

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.service.announcement.R
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.textColorResource

class TagsDetailItem(val content: String, val id: Int, var state: Boolean, val onclick: () -> Unit) : Item {
    override fun areContentsTheSame(newItem: Item): Boolean {
        return content == (newItem as TagsDetailItem).content && id == newItem.id
    }

    override fun areItemsTheSame(newItem: Item): Boolean = areContentsTheSame(newItem)

    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.tag_detail, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as TagsDetailItem
            holder.apply {
                if (item.state) {
                    cardView.backgroundColorResource = R.color.colorPrimary
                    textView.textColorResource = R.color.white_color
                } else {
                    cardView.backgroundColorResource = R.color.white_color
                    textView.textColorResource = R.color.default_text_color
                }
                textView.text = item.content
                itemView.setOnClickListener {
                    if (item.state) {
                        cardView.backgroundColorResource = R.color.white_color
                        textView.textColorResource = R.color.default_text_color
                    } else {
                        cardView.backgroundColorResource = R.color.colorPrimary
                        textView.textColorResource = R.color.white_color
                    }
                    item.state = !item.state
                    item.onclick.invoke()
                    Log.d("tranced is debugging", item.state.toString())
                    Log.d("tagsDetail is clicked", item.content + " " + it.id)
                }
            }
        }

        private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(R.id.tag_detail)
            val cardView: CardView = itemView.findViewById(R.id.tag_detail_card)
        }

    }
}