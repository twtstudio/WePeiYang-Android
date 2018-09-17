package com.twtstudio.retrox.tjulibrary.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import com.twtstudio.retrox.tjulibrary.R
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.textColor

class RankItem(val id : String,val urlOfPic: String, val titleString: String,
               val borrowNumber: String, val publishName: String,
               val context: Context, val rankNumber: Int, val isborrow : Boolean) : Item {
    private companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.lb_statistics_cardview, parent, false)

            return RankItemViewHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as RankItemViewHolder
            item as RankItem

            holder.apply {
                val colorString = when (item.rankNumber) {
                    0 -> "#A73870"
                    1 -> "#FD8B00"
                    2 -> "#05B601"
                    else -> "#999999"
                }
                title.text = item.titleString
                borrow_number.text = item.borrowNumber
                publish_name.text = item.publishName
                rank_number.apply {
                    text = item.rankNumber.toString() + "、"
                    textColor = Color.parseColor(colorString)
                }
                borrow_mark.visibility = View.GONE
                Glide.with(item.context)
                        .load(item.urlOfPic)
                        .into(statistics_img)

                if (item.isborrow) { borrow_mark.visibility = View.VISIBLE }
            }

            holder.itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("id", item.id)
                val intent = Intent()
                intent.putExtras(bundle)
                intent.setClass(item.context, DetialAcitvity::class.java)
                item.context.startActivity(intent)
            }
        }
    }

    private class RankItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.sta_name)
        val borrow_number = itemView.findViewById<TextView>(R.id.borrow_num)
        val publish_name = itemView.findViewById<TextView>(R.id.publish_name)
        val statistics_img = itemView.findViewById<ImageView>(R.id.statistics_img)
        val rank_number = itemView.findViewById<TextView>(R.id.sta_num)
        val borrow_mark = itemView.findViewById<ImageView>(R.id.isborrow)
    }

    override val controller: ItemController
        get() = RankItem
}

fun MutableList<Item>.setRankItem(id : String,urlOfPic: String, titleString: String,
                                  borrowNumber: String, publishName: String,
                                  context: Context, rankNumber: Int, isborrow: Boolean) = add(RankItem(id, urlOfPic, titleString, borrowNumber, publishName, context, rankNumber, isborrow))