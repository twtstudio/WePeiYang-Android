package com.yookiely.lostfond2.detail

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lostfond2.R
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater

class OtherTagItem(val text3: String, val text2: String) : Item {

    private companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.lf2_item_othertag, parent, false)
            return OtherTagViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as OtherTagViewHolder
            item as OtherTagItem

            holder.apply {
                otherTagTitle.text = item.text3
                otherTagContent.text = item.text2
            }
        }
    }

    private class OtherTagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val otherTagTitle: TextView = itemView.findViewById(R.id.tv_othertag_title)
        val otherTagContent: TextView = itemView.findViewById(R.id.tv_othertag_detail)
    }

    override val controller: ItemController
        get() = OtherTagItem
}

fun MutableList<Item>.setOther(text1: String, text2: String) = add(OtherTagItem(text1, text2))
