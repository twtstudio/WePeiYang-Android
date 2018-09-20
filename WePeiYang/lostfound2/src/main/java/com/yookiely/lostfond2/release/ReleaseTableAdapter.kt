package com.yookiely.lostfond2.release

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lostfond2.R
import com.yookiely.lostfond2.service.Utils

class ReleaseTableAdapter(val context: Context, val positionSelected: Int,
                          val releaseView: ReleaseContract.ReleaseView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ReleaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val release_item_type = itemView.findViewById<TextView>(R.id.release_item_type)
        val release_item_cardview = itemView.findViewById<CardView>(R.id.release_item_cardview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.lf_item_release_type, parent, false)

        return ReleaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ReleaseViewHolder
        viewHolder.apply {
            release_item_type.text = Utils.getType(position + 1)
            release_item_cardview.setCardBackgroundColor(Color.parseColor("#dfdfdf"))
        }

        if (position == positionSelected) {
            viewHolder.release_item_cardview.setCardBackgroundColor(Color.parseColor("#00a1e9"))
        }

        viewHolder.itemView.setOnClickListener {
            run {
                releaseView.apply {
                    drawRecyclerView(position)
                    onTypeItemSelected(position)
                }
            }
        }

    }

    override fun getItemCount() = 13
}