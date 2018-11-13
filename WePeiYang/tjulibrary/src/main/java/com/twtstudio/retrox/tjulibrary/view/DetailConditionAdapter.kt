package com.twtstudio.retrox.tjulibrary.view

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.twtstudio.retrox.tjulibrary.R
import com.twtstudio.retrox.tjulibrary.tjulibservice.Holding
import org.jetbrains.anko.image
import org.jetbrains.anko.textColor

class DetailConditionAdapter(val list : List<Holding>, val context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val bookDemandNumber = view.findViewById<TextView>(R.id.book_demand_number)
        val bookPosition = view.findViewById<TextView>(R.id.book_position)
        val bookStatus = view.findViewById<ImageView>(R.id.book_status)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.lib_book_condition, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holding = list[position]
        holder as ViewHolder
        holder.apply {
            bookDemandNumber.text = holding.callno
            bookPosition.text = holding.local
            if (holding.state=="在馆"){
                bookStatus.setImageResource(R.drawable.inlib)
            }else{
                bookStatus.setImageResource(R.drawable.outlib)
            }

        }
    }

    override fun getItemCount(): Int = list.size
}