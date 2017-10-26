package com.twt.service.home.common

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.Intent
import android.databinding.ObservableArrayList
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.twt.service.R
import com.twt.service.home.common.gpaItem.GpaItemViewHolder
import com.twt.service.home.common.gpaItem.GpaItemViewModel
import com.twt.service.home.common.schedule.ScheduleItemViewHolder
import com.twt.service.home.common.schedule.ScheduleViewModel
import com.twtstudio.retrox.bike.homeitem.BikeHomeItemViewModel
import com.twtstudio.retrox.gpa.view.GpaActivity
import com.twtstudio.retrox.tjulibrary.home.HomeLibItemViewModel
import com.twtstudio.retrox.tjulibrary.homeitem.HomeLibItemComponent

/**
 * Created by retrox on 22/10/2017.
 */

class CommonPageAdapter(val list: List<Any>, val context: Context, val owner: LifecycleOwner) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val GPA = 1
        const val SCHEDULE = 2
        const val LIBRARY = 3
        const val BIKE = 4
        const val CLASSROOM = 5
    }

    val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = when (viewType) {
            GPA -> GpaItemViewHolder(inflater.inflate(R.layout.item_common_gpa, parent, false))
            SCHEDULE -> ScheduleItemViewHolder(inflater.inflate(R.layout.item_common_schedule, parent, false))
            LIBRARY -> HomeLibItemComponent(owner, inflater.inflate(R.layout.item_common_lib, parent, false))
            else -> null
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GpaItemViewHolder -> {
                holder.bind(owner, list[position] as? GpaItemViewModel)
                holder.cardView.setOnClickListener {
                    val intent = Intent(context, GpaActivity::class.java)
                    context.startActivity(intent)
                }
            }
            is ScheduleItemViewHolder -> {
                holder.bind(owner, list[position] as ScheduleViewModel)
            }
            is HomeLibItemComponent -> {
                holder.onBind()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is GpaItemViewModel -> return GPA
            is ScheduleViewModel -> return SCHEDULE
            is HomeLibItemViewModel -> return LIBRARY
            "LIB" -> return LIBRARY
            is BikeHomeItemViewModel -> return BIKE
            else -> 0
        }
    }
}
