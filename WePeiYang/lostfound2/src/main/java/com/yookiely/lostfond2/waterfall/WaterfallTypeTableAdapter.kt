package com.yookiely.lostfond2.waterfall

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lostfond2.R
import com.yookiely.lostfond2.service.Utils

class WaterfallTypeTableAdapter(private val waterfallActivity: WaterfallActivity,
                                val context: Context,
                                private val selectedItem: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class WaterFallTypeTableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val waterfallTypeItem = itemView.findViewById<TextView>(R.id.waterfall_type_item)
        val waterfallTypeLine = itemView.findViewById<View>(R.id.waterfall_type_line)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lf2_item_waterfall_type, parent, false)
        return WaterFallTypeTableViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as WaterFallTypeTableViewHolder

        viewHolder.waterfallTypeItem.apply {
            text = Utils.getType(position)
            typeface = Typeface.DEFAULT
        }

        if (position == itemCount - 1 || position == itemCount - 2) {
            holder.waterfallTypeLine.visibility = View.GONE
        }

        if (position == selectedItem) {
            viewHolder.waterfallTypeItem.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        }

        viewHolder.itemView.setOnClickListener {
            waterfallActivity.apply {
                setWaterfallType(position)
                viewHolder.waterfallTypeItem.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                window.dismiss()
            }
        }

    }

    override fun getItemCount(): Int = 14
}