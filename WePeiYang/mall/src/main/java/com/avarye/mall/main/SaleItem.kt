package com.avarye.mall.main

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.avarye.mall.R
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import kotlinx.android.synthetic.main.mall_item_main.view.*
import org.jetbrains.anko.layoutInflater

/**
 * 所有sale类商品的Item
 */
class SaleItem : Item {
    internal var builder: (ViewHolder.() -> Unit)? = null

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.mall_item_main, parent, false)
            val image = view.iv_main_latest
            val name = view.tv_main_name
            val price = view.tv_main_price
            val locate = view.tv_main_locate
            val card = view.cv_main_latest
            return ViewHolder(view, image, name, price, locate, card)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as SaleItem
            item.builder?.invoke(holder)
        }

        internal class ViewHolder(
                itemView: View,
                val image: ImageView,
                val name: TextView,
                val price: TextView,
                val locate: TextView,
                val card: CardView
        ) : RecyclerView.ViewHolder(itemView)
    }

    override val controller: ItemController
        get() = SaleItem
}

fun MutableList<Item>.saleItem() = add(SaleItem())
internal fun MutableList<Item>.saleItem(builder: SaleItem.Controller.ViewHolder.() -> Unit) =
        add(SaleItem().apply { this.builder = builder })

