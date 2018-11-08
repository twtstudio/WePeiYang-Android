package com.yookiely.lostfond2.search

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import com.example.lostfond2.R
import com.yookiely.lostfond2.service.Utils
import kotlinx.android.synthetic.main.lf2_activity_search.*

class SearchChooseTimeAdapter(val context: SearchActivity, val selectedItem: Int, var window: PopupWindow) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SearchChooseTimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val waterfallTypeItem: TextView = this.itemView.findViewById(R.id.tv_waterfall_type_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lf2_item_waterfall_type, parent, false).also {
            it.layoutParams.width = -1
        }

        return SearchChooseTimeAdapter.SearchChooseTimeViewHolder(view)
    }

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as SearchChooseTimeViewHolder

        viewHolder.waterfallTypeItem.apply {
            text = Utils.getDetailFilterOfTime(position + 1)
            typeface = Typeface.DEFAULT
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
            context.apply {
                iv_search_type_blue.visibility = View.GONE
                iv_search_type_grey.visibility = View.VISIBLE
                chooseTimePopupWindow.dismiss()

                when (position) {
                    0 -> context.changeTime(5)
                    else -> context.changeTime(position)
                }
            }
        }
    }
}