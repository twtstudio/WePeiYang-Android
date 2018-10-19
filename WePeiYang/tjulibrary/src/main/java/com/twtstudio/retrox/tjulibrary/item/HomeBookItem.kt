package com.twtstudio.retrox.tjulibrary.item

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import com.twtstudio.retrox.tjulibrary.R
import com.twtstudio.retrox.tjulibrary.provider.Book
import kotlinx.android.synthetic.main.lib_home_cardview_detail.view.*
import org.jetbrains.anko.layoutInflater

class HomeBookItem(val book : Book, val  context1 : Activity) : Item {
    private companion object Controller : ItemController {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as HomeBookItemViewHolder
            item as HomeBookItem

            holder.apply {
                Glide.with(item.context1)
                //记得写代码！！
                //！！！
                //！！！！

                bookName.text = item.book.title
                bookWriter.text =  item.book.author
                bookBorrow.text = item.book.loanTime
                bookLeft.text = item.book.timeLeft().toString()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.lib_home_cardview_detail, parent, false)
            return HomeBookItemViewHolder(view, view.home_image, view.home_bookname, view.detail_writer, view.detail_publish, view.borrow, view.detail_left)
        }


        private class HomeBookItemViewHolder(itemView: View,
                                             val bookImg : ImageView,
                                             val bookName : TextView,
                                             val bookWriter : TextView,
                                             val bookPublish : TextView,
                                             val bookBorrow : TextView,
                                             val bookLeft : TextView) : RecyclerView.ViewHolder(itemView) {
            val rootView get() = itemView
        }

    }




        override val controller: ItemController
            get() = HomeBookItem
}