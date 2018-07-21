package com.example.lostfond2.detail

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.lostfond2.R
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater

class ImageItem(val url : String, val  context1 : Activity) : Item {

    private companion object Controller : ItemController{
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.lf_item_imageitem,parent,false)
            return ImageItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ImageItemViewHolder
            item as ImageItem
            Glide.with(item.context1)
                    .load(item.url)
                    .placeholder(R.drawable.lf_detail_np)
                    .into(holder.image)
        }

    }

    private class  ImageItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image : ImageView = itemView.findViewById(R.id.image_image)

    }


    override val controller: ItemController
        get() = ImageItem
}

fun MutableList<Item>.setImage(url : String,context1: Activity) = add(ImageItem(url,context1))