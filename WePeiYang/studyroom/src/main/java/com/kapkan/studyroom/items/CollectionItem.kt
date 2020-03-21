package com.kapkan.studyroom.items

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.studyroom.R
import com.kapkan.studyroom.view.ClassroomActivity
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.sdk27.coroutines.onClick

class CollectionItem(val context: Context, val roomid: String,val roomname: String): Item{
    override val controller: ItemController
        get() = Controller

    private companion object Controller:ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.item_star,parent,false)
            return CollectViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            item as CollectionItem
            holder as CollectViewHolder
            holder.roomid.text = item.roomname
            holder.starBtn.onClick {
                //跳转课程表
                val intent = Intent(item.context,ClassroomActivity::class.java)
                intent.putExtra("classroomid",item.roomid)

                item.context.startActivity(intent)
            }
        }

        private class CollectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val roomid = itemView.findViewById<TextView>(R.id.roomId)
            val starBtn = itemView.findViewById<ImageView>(R.id.star)
        }

    }

}