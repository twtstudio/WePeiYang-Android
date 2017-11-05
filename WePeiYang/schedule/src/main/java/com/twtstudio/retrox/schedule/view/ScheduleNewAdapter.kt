package com.twtstudio.retrox.schedule.view

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kelin.mvvmlight.base.ViewModel
import com.twtstudio.retrox.schedule.R
import java.util.*

/**
 * Created by DefaultAccount on 2017/10/26.
 */
class ScheduleNewAdapter(val context: Context, val owner: LifecycleOwner) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val SELECTED_COURSE_INFO = 1
        const val SELECTED_DATE_INFO = 2
        const val COURSE_IS_EMPTY = 3
    }

    val list: MutableList<ViewModel> = ArrayList<ViewModel>()
    val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = when (viewType) {
            SELECTED_COURSE_INFO -> ScheduleNewViewHolder.SelectedCoursesInfoViewHolder(inflater.inflate(R.layout.item_selected_courses, parent, false))
            SELECTED_DATE_INFO -> ScheduleNewViewHolder.SelectedDateInfoViewHolder(inflater.inflate(R.layout.item_selected_date, parent, false))
            COURSE_IS_EMPTY -> ScheduleNewViewHolder.CourseIsEmptyViewHolder(inflater.inflate(R.layout.schedule_item_course_is_empty, parent, false))
            else -> null
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ScheduleNewViewHolder.SelectedCoursesInfoViewHolder ->
                holder.bind(owner, (list.get(position) as? SelectedCoursesInfoViewModel)!!)
            is ScheduleNewViewHolder.SelectedDateInfoViewHolder ->
                holder.bind(owner, (list.get(position) as? SelectedDateInfoViewModel)!!)
            is ScheduleNewViewHolder.CourseIsEmptyViewHolder ->
                holder.bind(owner, (list.get(position) as? CourseIsEmptyViewModel)!!)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is SelectedCoursesInfoViewModel -> return SELECTED_COURSE_INFO
            is SelectedDateInfoViewModel -> return SELECTED_DATE_INFO
            is CourseIsEmptyViewModel -> return COURSE_IS_EMPTY
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