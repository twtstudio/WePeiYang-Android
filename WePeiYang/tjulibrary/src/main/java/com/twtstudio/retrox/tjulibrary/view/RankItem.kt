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
import org.jetbrains.anko.image
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.textColor

class RankItem(val id: String, val urlOfPic: String, val titleString: String,
               val borrowNumber: String, val publishName: String,
               val rankFragment: RankFragment, val rankNumber: Int, val isborrow: Boolean) : Item {

    private companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.lb_statistics_cardview, parent, false)

            return RankItemViewHolder(view)
        }


        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as RankItemViewHolder
            item as RankItem

            holder.apply {

                title.text = item.titleString
                borrow_number.text = item.borrowNumber
                publish_name.text = item.publishName
                when (item.rankNumber.toString()) {
                    "1" -> {
                        statistics_img.setImageResource(R.drawable.frist)
                        rank_number.visibility = View.GONE
                    }
                    "2" -> {
                        statistics_img.setImageResource(R.drawable.second)
                        rank_number.visibility = View.GONE
                    }
                    "3" -> {
                        statistics_img.setImageResource(R.drawable.third)
                        rank_number.visibility = View.GONE
                    }
                    else -> {
                        rank_number.visibility = View.VISIBLE
                        rank_number.text = item.rankNumber.toString()

                    }
                }



                borrow_mark.visibility = View.GONE

                if (item.isborrow) {
                    borrow_mark.visibility = View.VISIBLE
                }
            }

            holder.itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("id", item.id)
                val intent = Intent()
                intent.putExtras(bundle)
                intent.setClass(item.rankFragment.context, DetailActivity::class.java)
                item.rankFragment.startActivity(intent)
            }
        }
    }

    private class RankItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.sta_name)
        val borrow_number = itemView.findViewById<TextView>(R.id.borrow_num)
        val publish_name = itemView.findViewById<TextView>(R.id.publish_name)
        val statistics_img = itemView.findViewById<ImageView>(R.id.statistics_img)
        val rank_number = itemView.findViewById<TextView>(R.id.rank_num)
        val borrow_mark = itemView.findViewById<ImageView>(R.id.isborrow)
    }

    override val controller: ItemController
        get() = RankItem
}

fun MutableList<Item>.setRankItem(id: String, urlOfPic: String, titleString: String,
                                  borrowNumber: String, publishName: String,
                                  rankFragment: RankFragment, rankNumber: Int, isborrow: Boolean) = add(RankItem(id, urlOfPic, titleString, borrowNumber, publishName, rankFragment, rankNumber, isborrow))