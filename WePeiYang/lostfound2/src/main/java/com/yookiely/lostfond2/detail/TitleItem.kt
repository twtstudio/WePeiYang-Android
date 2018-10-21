package com.yookiely.lostfond2.detail

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lostfond2.R
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater


class TitleItem(val text1 : String) : Item {

    private companion object  Controller: ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.lf2_item_titleitem, parent, false)
            return TitleItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as TitleItemViewHolder
            item as TitleItem

            holder.title.text = item.text1

        }

    }

    private class TitleItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.title_title)

    }


    override val controller: ItemController
        get() = TitleItem
}

fun MutableList<Item>.setTitle(text1: String) = add(TitleItem(text1))