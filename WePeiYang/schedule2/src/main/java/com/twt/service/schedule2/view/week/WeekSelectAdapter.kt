package com.twt.service.schedule2.view.week

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twt.service.schedule2.R

/**
 * 顶部周数选择Rec的适配器
 */
class WeekSelectAdapter(val context: Context) : RecyclerView.Adapter<WeekSelectAdapter.WeekSelectViewHolder>() {

    private var weekSquareDataList = mutableListOf<WeekSquareView.WeekSquareData>()

    var clickListener: (Int) -> Unit = {}


    fun refreshWeekSquareData (data: List<WeekSquareView.WeekSquareData>) {
        weekSquareDataList.removeAll { true }
        weekSquareDataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekSelectViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.schedule_item_week_select, parent, false)
        return WeekSelectViewHolder(view)
    }

    override fun getItemCount(): Int = weekSquareDataList.size

    override fun onBindViewHolder(holder: WeekSelectViewHolder, position: Int) {
        val data = weekSquareDataList[position]
        holder.apply {
            bind(data)
            weekSquareView.setOnClickListener {
                clickListener.invoke(data.weekInt)
            }
        }
    }

    class WeekSelectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val weekSquareView = itemView.findViewById<WeekSquareView>(R.id.custom_week_view)

        fun bind(data: WeekSquareView.WeekSquareData) {
            weekSquareView.data = data
        }
    }
}