package com.yookiely.lostfond2.detail

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lostfond2.R
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater

class DetailItem(val text1: String, val text2: String, val isLast: Boolean) : Item {

    private companion object  Controller: ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.lf2_item_detailitem, parent, false)
            return DetailItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as DetailItemViewHolder
            item as DetailItem

            holder.title.text = item.text1
            holder.content.text = item.text2
            if (item.isLast) {
                holder.spilit.visibility = View.GONE
            } else {
                holder.spilit.visibility = View.VISIBLE
            }
        }

    }

    private class DetailItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.detail_title)
        val content : TextView = itemView.findViewById(R.id.detail_detail)
        val spilit: TextView = itemView.findViewById(R.id.detail_spilit)
    }


    override val controller: ItemController
        get() = DetailItem
}

fun MutableList<Item>.setDetail(text1: String, text2: String, isLast: Boolean) = add(DetailItem(text1, text2, isLast))