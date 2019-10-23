package com.twt.service.home.message

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tjuwhy.yellowpages2.view.SubItem
import com.twt.service.R
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater

class MessageListItem (val context :Context, val info : Info ,val block: (MessageListItem,View) -> Unit): Item{
    override val controller: ItemController
        get() = MessageListItem

    override fun areContentsTheSame(newItem: Item): Boolean {
        return context==(newItem as? MessageListItem)?.context && info ==(newItem as? MessageListItem)?.info
    }
    override fun areItemsTheSame(newItem: Item)=true
    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.item_message_list,parent,false)
            val textView = view.findViewById<TextView>(R.id.message_headline)
            val textView2 = view.findViewById<TextView>(R.id.message_content)
            val textView3 = view.findViewById<TextView>(R.id.message_time)
            val imageView = view.findViewById<ImageView>(R.id.message_uncheck)
            return ViewHolder(view,textView,textView2,textView3,imageView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            item as MessageListItem
            holder as ViewHolder
            holder.textView.text = item.info.title
            holder.textView2.text = item.info.content
            holder.textView3.text = item.info.created_at
            holder.imageView.setOnClickListener {
                holder.imageView.setImageResource(R.drawable.check)
                item.block(item,it)
            }
            holder.itemView.setOnClickListener{
                val intent = Intent(item.context,MessageInfo::class.java)
                intent.putExtra("title",item.info.title)
                intent.putExtra("time",item.info.created_at)
                intent.putExtra("content",item.info.content)
                item.context.startActivity(intent)
            }
        }

    }
    class ViewHolder(itemview :View,val textView: TextView,val textView2: TextView,val textView3 : TextView,val imageView: ImageView) : RecyclerView.ViewHolder(itemview)

}

class AllKnowItem(val block: () -> Unit) : Item{
    override val controller: ItemController
        get() = AllKnowItem

    override fun areContentsTheSame(newItem: Item) = true
    override fun areItemsTheSame(newItem: Item) = true
    companion object Controller : ItemController{
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.item_message_know,parent,false)
            return  ViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as AllKnowItem
            holder.itemView.setOnClickListener {
                item.block()
            }
        }

    }
    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
}

class ReadItem() :Item{
    override val controller: ItemController
        get() = ReadItem
    companion object Controller :ItemController{
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater =parent.context.layoutInflater
            val view =inflater.inflate(R.layout.item_message_read,parent,false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as ReadItem
        }

    }
    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
}