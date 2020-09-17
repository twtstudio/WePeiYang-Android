package com.twt.service.announcement.ui.item

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.twt.service.announcement.R
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController

class TagBottomItem(val content: String, val index:Int, var onclick:()-> Unit) : Item {

    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflate = LayoutInflater.from(parent.context)
            val view = inflate.inflate(R.layout.tag_bottom, parent, false)
            val tagButton = view.findViewById<Button>(R.id.btn)
            return ViewHolder(view, tagButton)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as TagBottomItem
            holder.button.text = item.content
            holder.button.layoutParams.width = 60 * item.content.length +110
            Log.d("button width",holder.button.layoutParams.width.toString())
            holder.itemView.setOnClickListener {
                item.onclick.invoke()
                Log.d("TagBottom Test",item.onclick.toString())
            }
        }

        private class ViewHolder(itemView: View?, val button: Button) : RecyclerView.ViewHolder(
                itemView!!
        )
    }
}