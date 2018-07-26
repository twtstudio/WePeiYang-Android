package com.example.lostfond2.detail

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lostfond2.R
import com.example.lostfond2.mylist.MylistItem
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater

class OtherTagItem(val text1: String, val text2: String) : Item {

    private companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.lf_item_othertag, parent, false)
            return DetailItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as DetailItemViewHolder
            item as DetailItem

            holder.title.text = item.text1
            holder.content.text = item.text2
        }

    }

    private class DetailItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.othertag_title)
        val content: TextView = itemView.findViewById(R.id.othertag_detail)
    }


    override val controller: ItemController
        get() = OtherTagItem
}

fun MutableList<Item>.setOther(text1: String, text2: String) = add(OtherTagItem(text1, text2))