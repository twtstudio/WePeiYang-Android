package com.yookiely.lostfond2.waterfall

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lostfond2.R
import com.yookiely.lostfond2.service.Utils
import kotlinx.android.synthetic.main.activity_water_fall.*


class WaterfallFilterTableAdapter(val waterfallActivity: WaterFallActivity,
                                  val context: WaterFallActivity,
                                  val selectedItem: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class WaterfallFilterTableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val waterfall_type_item = itemView.findViewById<TextView>(R.id.waterfall_type_item)
        val waterfallTypeLine = itemView.findViewById<TextView>(R.id.waterfall_type_line)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lf_item_waterfall_type, parent, false).also {
            it.layoutParams.width = -1
        }

        return WaterfallFilterTableViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as WaterfallFilterTableViewHolder

        viewHolder.waterfall_type_item.apply {
            text = Utils.getDetailFilterOfTime(position + 1)
            typeface = Typeface.DEFAULT
        }

        if (position == itemCount - 1) {
            holder.waterfallTypeLine.visibility = View.GONE
        }

        when (selectedItem) {
            5 -> if (position == 0) {
                viewHolder.waterfall_type_item.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            }
            position -> {
                viewHolder.waterfall_type_item.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            }
            else -> {
            }
        }

        viewHolder.itemView.setOnClickListener {
            waterfallActivity.apply {
                waterfall_type_blue.visibility = View.GONE
                waterfall_type_grey.visibility = View.VISIBLE
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