package com.example.lostfond2.waterfall

import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import com.example.lostfond2.R
import com.example.lostfond2.service.Utils
import kotlinx.android.synthetic.main.activity_water_fall.*
import android.view.Gravity


class WaterfallFilterTableAdapter(val waterfallActivity: WaterFallActivity,
                                  val context: WaterFallActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class WaterfallFilterTableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val waterfall_type_item = itemView.findViewById<TextView>(R.id.waterfall_type_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lf_item_waterfall_type, parent, false)


        return WaterfallFilterTableViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as WaterfallFilterTableViewHolder

        viewHolder.waterfall_type_item.apply {
            text = Utils.getDetailFilterOfTime(position + 1)
        }

        viewHolder.itemView.setOnClickListener {
            waterfallActivity.waterfall_type_blue.visibility = View.GONE
            waterfallActivity.waterfall_type_grey.visibility = View.VISIBLE
            waterfallActivity.window.dismiss()
            //do something
        }
    }

    override fun getItemCount(): Int = 5
}