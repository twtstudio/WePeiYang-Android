package com.yookiely.lostfond2.detail

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.lostfond2.R
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.sdk25.coroutines.onClick

class DetailItem(val specificTitle: String, val content: String, val isLast: Boolean) : Item {

    private companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.lf2_item_detailitem, parent, false)
            return DetailItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as DetailItemViewHolder
            item as DetailItem

            holder.apply {
                detailTitle.text = item.specificTitle
                detailContent.text = item.content
                detailRecaptureComment.visibility = View.GONE
                detailQuestionMark.apply {
                    visibility = if (item.specificTitle != "领取站点") View.GONE else View.VISIBLE
                    onClick {
                        detailRecaptureComment.visibility = if (detailRecaptureComment.visibility == View.GONE) View.VISIBLE else View.GONE
                    }
                }
                spiltLine.visibility = if (item.isLast) View.GONE else View.VISIBLE
            }
        }
    }

    private class DetailItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val detailTitle: TextView = itemView.findViewById(R.id.tv_detail_title)
        val detailContent: TextView = itemView.findViewById(R.id.tv_detail_detail)
        val spiltLine: View = itemView.findViewById(R.id.vw_detail_spilit)
        val detailQuestionMark: ImageView = itemView.findViewById(R.id.iv_detail_question_mark)
        val detailRecaptureComment: TextView = itemView.findViewById(R.id.tv_detail_recapture_comment)
    }

    override val controller: ItemController
        get() = DetailItem
}

fun MutableList<Item>.setDetail(specificTitle: String, content: String, isLast: Boolean) = add(DetailItem(specificTitle, content, isLast))