package com.twt.service.slide.flash

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.service.slide.R
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import kotlinx.android.synthetic.main.flash_layout_float_window.view.*
import org.jetbrains.anko.layoutInflater
import java.text.SimpleDateFormat
import java.util.*

class FlashTodayItem : Item {
    override val controller: ItemController
        get() = Controller

    companion object Controller: ItemController{
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.flash_header_today, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            val dateFormat = SimpleDateFormat("yyyy/MM/dd E")
            val date = Date(Calendar.getInstance().timeInMillis)
            val dateString = dateFormat.format(date)
            holder.headerTodayDetailText.text = dateString
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val rootView = itemView
            val headerTodayDetailText = itemView.findViewById<TextView>(R.id.tv_flash_today_detail)

        }

    }
}

fun MutableList<Item>.flashTodayHeader() = add(FlashTodayItem())