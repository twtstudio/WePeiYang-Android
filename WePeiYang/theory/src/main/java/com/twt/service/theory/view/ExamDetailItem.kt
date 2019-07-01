package com.twt.service.theory.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.service.theory.R
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater


class ExamDetailItem : Item {
    private companion object Controller : ItemController {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ExamDetailItemViewHolder
            item as ExamDetailItem

            holder.apply {
                title.text = "考试时间"
                times.text = "2019-4-20"
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.theory_item_examdetail, parent, false)

            return ExamDetailItemViewHolder(view)
        }




    }
    private class ExamDetailItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.eaxm_detail_name)
        val times = itemView.findViewById<TextView>(R.id.eaxm_detail_detail)
    }


    override val controller: ItemController
        get() = ExamDetailItem
}