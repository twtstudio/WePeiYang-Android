package com.twtstudio.retrox.tjulibrary.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import com.twtstudio.retrox.tjulibrary.R
import org.jetbrains.anko.layoutInflater

class RankItem(val id: String, val urlOfPic: String, val titleString: String,
               val borrowNumber: String, val publishName: String,
               val rankFragment: RankFragment, val rankNumber: Int, val isborrow: Boolean) : Item {

    private companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.lib_statistics_cardview, parent, false)

            return RankItemViewHolder(view)
        }


        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as RankItemViewHolder
            item as RankItem

            holder.apply {

                title.text = item.titleString
                borrowNumber.text = item.borrowNumber
                publishName.text = item.publishName
                when (item.rankNumber.toString()) {
                    "1" -> {
                        statisticsImg.setImageResource(R.drawable.frist)
                        rankNumber.visibility = View.GONE
                    }
                    "2" -> {
                        statisticsImg.setImageResource(R.drawable.second)
                        rankNumber.visibility = View.GONE
                    }
                    "3" -> {
                        statisticsImg.setImageResource(R.drawable.third)
                        rankNumber.visibility = View.GONE
                    }
                    else -> {
                        rankNumber.visibility = View.VISIBLE
                        rankNumber.text = item.rankNumber.toString()

                    }
                }



                borrowMark.visibility = View.GONE

                if (item.isborrow) {
                    borrowMark.visibility = View.VISIBLE
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
        val borrowNumber = itemView.findViewById<TextView>(R.id.borrow_num)
        val publishName = itemView.findViewById<TextView>(R.id.publish_name)
        val statisticsImg = itemView.findViewById<ImageView>(R.id.statistics_img)
        val rankNumber = itemView.findViewById<TextView>(R.id.rank_num)
        val borrowMark = itemView.findViewById<ImageView>(R.id.isborrow)
    }

    override val controller: ItemController
        get() = RankItem
}

fun MutableList<Item>.setRankItem(id: String, urlOfPic: String, titleString: String,
                                  borrowNumber: String, publishName: String,
                                  rankFragment: RankFragment, rankNumber: Int, isborrow: Boolean) = add(RankItem(id, urlOfPic, titleString, borrowNumber, publishName, rankFragment, rankNumber, isborrow))