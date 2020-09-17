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
import kotlinx.android.synthetic.main.tag_detail.view.*

class TagsDetailItem(val content:String,val id:Int,val onclick:()->Unit) :Item{
    override val controller: ItemController
        get() = Controller

    companion object Controller:ItemController{
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflate = LayoutInflater.from(parent.context)
            val view = inflate.inflate(R.layout.tag_detail,parent,false)
            val tagsDetail = view.findViewById<TextView>(R.id.tag_detail)
            return ViewHolder(view,tagsDetail)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as TagsDetailItem
            holder.textView.text = item.content
            holder.itemView.setOnClickListener {
                item.onclick.invoke()
                Log.d("tagsDetail is clicked",item.content+" "+ it.id)
            }
        }

        private class ViewHolder(itemView:View,val textView:TextView):RecyclerView.ViewHolder(itemView)

    }
}