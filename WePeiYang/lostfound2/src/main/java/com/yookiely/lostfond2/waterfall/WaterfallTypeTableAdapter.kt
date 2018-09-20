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
import kotlinx.android.synthetic.main.activity_water_fall.*

class WaterfallTypeTableAdapter(val waterfallActivity: WaterFallActivity,
                                val context: Context,
                                val selectedItem: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class WaterFallTypeTableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val waterfall_type_item = itemView.findViewById<TextView>(R.id.waterfall_type_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lf_item_waterfall_type, parent, false)
        return WaterFallTypeTableViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as WaterFallTypeTableViewHolder

        viewHolder.waterfall_type_item.apply {
            text = Utils.getType(position + 1)
            typeface = Typeface.DEFAULT
        }

        if (position == selectedItem - 1) {
            viewHolder.waterfall_type_item.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        }

        viewHolder.itemView.setOnClickListener { view ->
            waterfallActivity.setWaterfallType(position + 1)
            waterfallActivity.waterfall_type_blue.visibility = View.GONE
            waterfallActivity.waterfall_type_grey.visibility = View.VISIBLE
            waterfallActivity.window.dismiss()
        }

    }

    override fun getItemCount(): Int = 13
}