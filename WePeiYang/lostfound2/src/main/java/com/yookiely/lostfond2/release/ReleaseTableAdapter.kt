package com.yookiely.lostfond2.release

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.lostfond2.R
import com.yookiely.lostfond2.service.Utils
import org.jetbrains.anko.textColor

class ReleaseTableAdapter(val context: Context, val positionSelected: Int,
                          val releaseView: ReleaseContract.ReleaseView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ReleaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val releaseItemType = itemView.findViewById<TextView>(R.id.release_item_type)!!
        val releaseItemCardView = itemView.findViewById<CardView>(R.id.release_item_cardview)!!
        val releaseItemSuperscript = itemView.findViewById<ImageView>(R.id.release_item_superscript)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.lf2_item_release_type, parent, false)

        return ReleaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ReleaseViewHolder
        viewHolder.apply {
            releaseItemType.text = Utils.getType(position + 1)

            if (position == positionSelected) {
                releaseItemType.textColor = Color.parseColor("#00A1EA")
                releaseItemCardView.setCardBackgroundColor(Color.parseColor("#BEDFEE"))
                releaseItemSuperscript.visibility = View.VISIBLE
            } else {
                releaseItemType.textColor = Color.parseColor("#333333")
                releaseItemCardView.setCardBackgroundColor(Color.parseColor("#DFDFDF"))
                releaseItemSuperscript.visibility = View.GONE
            }
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