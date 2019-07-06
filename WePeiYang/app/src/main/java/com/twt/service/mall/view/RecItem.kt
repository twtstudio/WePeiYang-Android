package com.twt.service.mall.view

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.twt.service.R
import com.twt.service.mall.model.Goods
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import kotlinx.android.synthetic.main.mall_item_main_latest.view.*
import org.jetbrains.anko.layoutInflater

class RecItem(val mContext: Context, val info: Goods) : Item {

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

            val info = item.info
            Picasso.with(item.mContext).load(info.icon).into(holder.image)
            holder.name.text = info.name
            holder.price.text = info.price
            holder.locate.text = info.location
            holder.card.setOnClickListener {
                val intent = Intent(item.mContext, DetailActivity::class.java)
                        .putExtra("image", info.icon)
                        .putExtra("name", info.name)
                        .putExtra("price", info.price)
                        .putExtra("locate", info.location)
                //TODO:其他的信息
                item.mContext.startActivity(intent)
            }
        }

        class ViewHolder(
                itemView: View,
                val image: ImageView,
                val name: TextView,
                val price: TextView,
                val locate: TextView,
                val card: CardView
        ) : RecyclerView.ViewHolder(itemView)
    }

    override val controller: ItemController
        get() = RecItem
}