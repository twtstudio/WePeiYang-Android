package com.twtstudio.retrox.schedule.view

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.twtstudio.retrox.schedule.R
import java.security.acl.Owner

/**
 * Created by DefaultAccount on 2017/10/26.
 */
class ScheduleNewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    //    lateinit var tvToday: TextView
//    lateinit var tvDate: TextView
//    init {
//        tvToday=itemView.findViewById(R.id.tv_today) as TextView
//        tvDate=itemView.findViewById(R.id.tv_date) as TextView
//    }
//    fun bind(owner: LifecycleOwner, viewModel: ScheduleNewViewModel) {
//        viewModel.today.observe(owner, Observer<String?>{
//            tvToday?.setText(it)
//        })
//        viewModel.date.observe(owner, Observer<String?>{
//            tvDate?.setText(it)
//        })
//    }
    class SelectedCoursesInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvClass: TextView
        val tvTime: TextView
        val courseName: TextView
        val place: TextView
        val card:CardView
        init {
            tvClass = itemView?.findViewById<TextView>(R.id.tv_class)
            tvTime = itemView?.findViewById<TextView>(R.id.tv_time)
            courseName = itemView?.findViewById<TextView>(R.id.course_name)
            place = itemView?.findViewById<TextView>(R.id.place)
            card=itemView?.findViewById<CardView>(R.id.cv_s)
        }

        fun bind(owner: LifecycleOwner, viewModel: SelectedCoursesInfoViewModel) {
            viewModel.timePeriod.observe(owner, Observer<String?> {
                tvClass?.setText(it)
            })
            viewModel.mTime.observe(owner, Observer<String?> {
                tvTime?.setText(it)
            })
            viewModel.courseName.observe(owner, Observer<String?> {
                courseName?.setText(it)
            })
            viewModel.location.observe(owner, Observer<String?> {
                place?.setText(it)
            })
            card.setOnClickListener(View.OnClickListener {
                viewModel.onClick(it)
            })
        }
    }

    class SelectedDateInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvToday: TextView
        val tvDate: TextView

        init {
            tvToday = itemView?.findViewById<TextView>(R.id.tv_today) as TextView
            tvDate = itemView?.findViewById<TextView>(R.id.tv_date) as TextView
        }

        fun bind(owner: LifecycleOwner, viewModel: SelectedDateInfoViewModel) {
            viewModel.today.observe(owner, Observer<String?>{
                tvToday?.setText(it)
            })
            viewModel.date.observe(owner, Observer<String?>{
                tvDate?.setText(it)
            })
        }
    }

    class CourseIsEmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView

        init {
            text = itemView?.findViewById<TextView>(R.id.tv_empty) as TextView
        }

        fun bind(owner: LifecycleOwner, viewModel: CourseIsEmptyViewModel) {
            viewModel.text.observe(owner, Observer<String?> {
                text?.setText(it)
            })
        }
    }
}