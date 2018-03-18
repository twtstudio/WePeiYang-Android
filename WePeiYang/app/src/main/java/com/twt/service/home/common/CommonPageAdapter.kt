package com.twt.service.home.common

import android.arch.lifecycle.LifecycleOwner
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.twt.service.R
import com.twt.service.home.common.gpa.GpaItemViewHolder
import com.twt.service.home.common.schedule.ScheduleItemViewHolder
import com.twt.service.home.common.schedule.ScheduleViewModel
import com.twt.service.tjunet.view.TjuNetViewComponent
import com.twtstudio.retrox.tjulibrary.homeitem.LibraryItemViewHolder

/**
 * Created by retrox on 22/10/2017.
 */

class CommonPageAdapter(
        private val list: List<Any>,
        private val inflater: LayoutInflater,
        private val owner: LifecycleOwner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val GPA = 1
        const val SCHEDULE = 2
        const val LIBRARY = 3
//        const val BIKE = 4
//        const val CLASSROOM = 5
        const val NETWORK = 6
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        GPA -> GpaItemViewHolder(inflater.inflate(R.layout.item_common_gpa, parent, false), owner)
        SCHEDULE -> ScheduleItemViewHolder(inflater.inflate(R.layout.item_common_schedule, parent, false))
        LIBRARY -> LibraryItemViewHolder(owner, inflater.inflate(R.layout.item_common_lib, parent, false))
        NETWORK -> TjuNetViewComponent.create(inflater, parent, owner)
//            BIKE -> BikeHomeItemViewHolder(inflater.inflate(R.layout.item_bike_card,parent,false))
        else -> null
    }!!

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GpaItemViewHolder -> Unit
            is ScheduleItemViewHolder -> {
                holder.bind(owner, list[position] as ScheduleViewModel)
            }
            is LibraryItemViewHolder -> {
                holder.onBind()
            }
            is TjuNetViewComponent -> {
                holder.bind()
            }
//            is BikeHomeItemViewHolder->{
//                holder.bind(owner,list[position] as BikeHomeItemViewModel)
//            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int) = when (list[position]) {
        "GPA" -> GPA
        is ScheduleViewModel -> SCHEDULE
        "LIB" -> LIBRARY
        "NETWORK" -> NETWORK
//            is BikeHomeItemViewModel -> return BIKE
        else -> 0
    }
}
