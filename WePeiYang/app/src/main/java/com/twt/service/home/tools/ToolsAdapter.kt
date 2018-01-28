package com.twt.service.home.tools

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twt.service.R

class ToolsAdapter(val context: Context, val list: List<ToolItem>) : RecyclerView.Adapter<ToolItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ToolItemViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_tool,parent,false)
        return ToolItemViewHolder(context, view)
    }

    override fun onBindViewHolder(holder: ToolItemViewHolder?, position: Int) {
        holder?.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}