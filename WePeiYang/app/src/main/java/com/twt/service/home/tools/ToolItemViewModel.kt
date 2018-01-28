package com.twt.service.home.tools

import android.content.Context
import android.content.Intent

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.twt.service.R

/**
 * Created by retrox on 2017/1/15.
 */


class ToolItem(val iconres: Int, val title: String, val targetAct: Class<out AppCompatActivity>)

class ToolItemViewHolder(private val mContext: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {

    val layout: LinearLayout = itemView.findViewById(R.id.tool_linear_layout)
    val image: ImageView = itemView.findViewById(R.id.tool_image)
    val titleText: TextView = itemView.findViewById(R.id.tool_title)

    fun bind(toolItem: ToolItem) {
        toolItem.apply {
            Glide.with(mContext).load(iconres).into(image)
            titleText.text = title
            layout.setOnClickListener {
                val intent = Intent(mContext, targetAct)
                mContext.startActivity(intent)
            }
        }
    }

}
