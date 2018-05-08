package com.twtstudio.service.dishesreviews.canteen

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twtstudio.service.dishesreviews.R

/**
 * Created by SGXM on 2018/5/6.
 */
class RightAdapter(var list: List<Any>, val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyLeftHolder(LayoutInflater.from(context).inflate(R.layout.dishes_reviews_item_food_rightlist, parent, false))

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyLeftHolder) {
            holder.tv_name.text = list[position] as String
        }
    }

    override fun getItemCount(): Int = list.size


    inner class MyLeftHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tv_name = view.findViewById<TextView>(R.id.tv_name)
    }

}