package com.twt.service.schedule2.view.detail

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.twt.service.schedule2.model.Classtable
import com.twt.service.schedule2.model.Course
import com.twt.service.schedule2.view.schedule.CourseRefreshStatusComponent

class CourseDetailAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TYPE_INFO = 0
        const val TYPE_INDICATOR = 1
        const val TYPE_DETAIL = 2
        const val TYPE_REFRESH_DETAIL = 3
    }

    private val dataList = mutableListOf<Any>()

    fun refreshDataList(list: List<Any>) {
        dataList.removeAll { true }
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = when (viewType) {
            TYPE_INFO -> CourseInfoComponent.create(inflater, parent)
            TYPE_INDICATOR -> CourseIndicatorComponent.create(inflater, parent)
            TYPE_DETAIL -> CourseDetailComponent.create(inflater, parent)
            TYPE_REFRESH_DETAIL -> CourseRefreshStatusComponent.create(inflater, parent)
            else -> CourseIndicatorComponent.create(inflater, parent)
        }
        return view
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = dataList[position]
        when (holder) {
            is CourseInfoComponent -> holder.bind(data as Course)
            is CourseIndicatorComponent -> holder.bind(data as String)
            is CourseDetailComponent -> holder.bind(data as CourseDetailViewModel)
            is CourseRefreshStatusComponent -> holder.bind(data as Classtable)
        }
    }

    override fun getItemCount(): Int = dataList.size

    override fun getItemViewType(position: Int): Int = when (dataList[position]) {
        is Course -> TYPE_INFO
        is String -> TYPE_INDICATOR
        is CourseDetailViewModel -> TYPE_DETAIL
        is Classtable -> TYPE_REFRESH_DETAIL
        else -> TYPE_INDICATOR
    }


}