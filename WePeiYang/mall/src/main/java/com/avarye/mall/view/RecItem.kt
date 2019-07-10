package com.avarye.mall.view

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.avarye.mall.R
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import kotlinx.android.synthetic.main.mall_item_main_latest.view.*
import org.jetbrains.anko.layoutInflater

class RecItem : Item {
    var builder: (ViewHolder.() -> Unit)? = null

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.mall_item_main_latest, parent, false)
            val image = view.mall_iv_main_latest
            val name = view.mall_tv_main_name
            val price = view.mall_tv_main_price
            val locate = view.mall_tv_main_locate
            val card = view.mall_cv_main_latest
            return ViewHolder(view, image, name, price, locate, card)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as RecItem
            item.builder?.invoke(holder)
        }

        class ViewHolder(
                itemView: View,
                val image: ImageView,
                val name: TextView,
                val price: TextView,
                val campus: TextView,
                val card: CardView
        ) : RecyclerView.ViewHolder(itemView)
    }

    override val controller: ItemController
        get() = RecItem
}

fun MutableList<Item>.recItem() = add(RecItem())
fun MutableList<Item>.recItem(builder: RecItem.Controller.ViewHolder.() -> Unit) = add(RecItem().apply { this.builder = builder })

