package com.twtstudio.retrox.tjulibrary.view

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twtstudio.retrox.tjulibrary.R
import com.twtstudio.retrox.tjulibrary.tjulibservice.Holding
import kotlinx.android.synthetic.main.lb_book_condition.view.*
import org.jetbrains.anko.textColor

class DetialConditionAdapter(val list : List<Holding>, val context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val bookDemandNumber = view.findViewById<TextView>(R.id.book_demand_number)
        val bookPosition = view.findViewById<TextView>(R.id.book_position)
        val bookState = view.findViewById<TextView>(R.id.book_state)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.lb_book_condition, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holding = list[position]
        holder as ViewHolder
        holder.apply {
            bookDemandNumber.text = holding.callno
            bookPosition.text = holding.local
            bookState.text = holding.state

            bookState.textColor = if (bookState.text == "在馆") { Color.parseColor("#57B550") } else { Color.parseColor("#999999")}
        }
    }

    override fun getItemCount(): Int = list.size
}