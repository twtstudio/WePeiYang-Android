package com.yookiely.lostfond2.waterfall

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lostfond2.R
import com.yookiely.lostfond2.service.Utils


class WaterfallTimeTableAdapter(val waterfallActivity: WaterfallActivity,
                                val context: WaterfallActivity,
                                val selectedItem: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class WaterfallFilterTableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val waterfallTypeItem = itemView.findViewById<TextView>(R.id.waterfall_type_item)!!
        val waterfallTypeLine = itemView.findViewById<View>(R.id.waterfall_type_line)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lf_item_waterfall_type, parent, false).also {
            it.layoutParams.width = -1
        }

        return WaterfallFilterTableViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as WaterfallFilterTableViewHolder

        viewHolder.waterfallTypeItem.apply {
            text = Utils.getDetailFilterOfTime(position + 1)
            typeface = Typeface.DEFAULT
        }

        if (position == itemCount - 1) {
            holder.waterfallTypeLine.visibility = View.GONE
        }

        when (selectedItem) {
            5 -> if (position == 0) {
                viewHolder.waterfallTypeItem.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            }
            position -> {
                viewHolder.waterfallTypeItem.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            }
            else -> {
            }
        }

        viewHolder.itemView.setOnClickListener {
            waterfallActivity.apply {
                popWaterfallFilter.typeface = Typeface.DEFAULT
                window.dismiss()

                when (position) {
                    0 -> setWaterfallTime(5)
                    else -> setWaterfallTime(position)
                }
            }
        }
    }

    override fun getItemCount(): Int = 5
}