package com.twtstudio.retrox.schedule.view

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.kelin.mvvmlight.base.ViewModel
import com.twtstudio.retrox.schedule.R
import java.util.ArrayList

/**
 * Created by DefaultAccount on 2017/10/30.
 */
class ScheduleTodayAdapter(val context: Context, val owner: LifecycleOwner) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TODAY_INFO = 1
        const val COURSE_DETAIL = 2
        const val TO_SCHEDULE_ACT = 3
        const val COURSE_EMPTY=4
    }

    val list: MutableList<ViewModel> = ArrayList<ViewModel>()
    val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = when (viewType) {
            TODAY_INFO -> ScheduleTodayViewHolder.TodayInfoViewHolder(inflater.inflate(R.layout.item_schedule_course_today_info, parent, false))
            COURSE_DETAIL -> ScheduleTodayViewHolder.CourseDetailViewHolder(inflater.inflate(R.layout.item_schedule_course_detail, parent, false))
            TO_SCHEDULE_ACT -> ScheduleTodayViewHolder.ToScheduleActViewHolder(inflater.inflate(R.layout.item_schedule_course_jump, parent, false))
            COURSE_EMPTY-> ScheduleTodayViewHolder.CourseEmptyViewHolder(inflater.inflate(R.layout.item_schedule_course_empty,parent,false))
            else -> null
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ScheduleTodayViewHolder.TodayInfoViewHolder ->
                holder.bind(owner, (list.get(position) as? TodayInfoViewModel)!!)
            is ScheduleTodayViewHolder.CourseDetailViewHolder ->
                holder.bind(owner, (list.get(position) as? CourseDetailViewModel)!!)
            is ScheduleTodayViewHolder.ToScheduleActViewHolder ->
                holder.bind(owner, (list.get(position) as? ToScheduleActViewModel)!!)
            is  ScheduleTodayViewHolder.CourseEmptyViewHolder ->
                holder.bind(owner, (list.get(position) as? CourseEmptyViewModel)!!)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is TodayInfoViewModel -> return TODAY_INFO
            is CourseDetailViewModel -> return COURSE_DETAIL
            is ToScheduleActViewModel -> return TO_SCHEDULE_ACT
            is CourseEmptyViewModel -> return COURSE_EMPTY
            else -> 0
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun refreshData(list: MutableList<ViewModel>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun addData(viewModel: ViewModel) {
        this.list.add(viewModel)
        notifyDataSetChanged()
    }
}