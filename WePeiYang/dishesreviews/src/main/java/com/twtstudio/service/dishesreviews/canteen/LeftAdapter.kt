package com.twtstudio.service.dishesreviews.canteen

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import com.twtstudio.service.dishesreviews.R

/**
 * Created by SGXM on 2018/5/6.
 */
class LeftAdapter(var list: List<Any>, val context: Context, val block: (Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var selectedPos: Int = 0
    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyLeftHolder(LayoutInflater.from(context).inflate(R.layout.dishes_reviews_item_food_leftlist, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyLeftHolder) {

            holder.chtv.apply {
                text = list[position] as String
                setTextColor(if (selectedPos == position) resources.getColor(R.color.black_color) else resources.getColor(R.color.colorTextGray1))
                setOnClickListener {
                    block(position)
                    selectItem(position)
//                    notifyItemChanged(selectedPos, "not")
//                    selectedPos = position
//                    notifyItemChanged(selectedPos, "not")

                }
            }
        }
    }

    public fun selectItem(pos: Int) {
        notifyItemChanged(selectedPos, "not")
        selectedPos = pos
        notifyItemChanged(selectedPos, "not")
    }

    inner class MyLeftHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val chtv = view.findViewById<CheckedTextView>(R.id.ch_tv)
    }


}