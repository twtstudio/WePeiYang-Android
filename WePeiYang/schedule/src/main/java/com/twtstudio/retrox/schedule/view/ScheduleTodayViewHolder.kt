package com.twtstudio.retrox.schedule.view

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.twtstudio.retrox.schedule.R

/**
 * Created by DefaultAccount on 2017/10/30.
 */
class ScheduleTodayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    class TodayInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView
        val tvWeekNumber: TextView
        val tvTodayNumber: TextView

        init {
            tvDate = itemView?.findViewById<TextView>(R.id.tv_date)
            tvWeekNumber = itemView?.findViewById<TextView>(R.id.tv_week_number)
            tvTodayNumber = itemView?.findViewById<TextView>(R.id.tv_today_number)
        }

        fun bind(owner: LifecycleOwner, viewModel: TodayInfoViewModel) {
            viewModel.date.observe(owner, Observer<String?> {
                tvDate?.setText(it)
            })
            viewModel.weekNumber.observe(owner, Observer<String?> {
                tvWeekNumber?.setText(it)
            })
            viewModel.todayNumber.observe(owner, Observer<String?> {
                tvTodayNumber?.setText(it)
            })
        }
    }

    class CourseDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCourseName: TextView
        val tvTeacherName: TextView
        val tvCredit: TextView
        val tvLocation: TextView
        val tvTimePriod: TextView
        val card: CardView

        init {
            tvCourseName = itemView?.findViewById<TextView>(R.id.tv_course_name)
            tvTeacherName = itemView?.findViewById<TextView>(R.id.tv_teacher_name)
            tvCredit = itemView?.findViewById<TextView>(R.id.tv_credit)
            tvLocation = itemView?.findViewById<TextView>(R.id.tv_location)
            tvTimePriod = itemView?.findViewById<TextView>(R.id.tv_time_period)
            card=itemView?.findViewById<CardView>(R.id.cv_course_detail)
        }

        fun bind(owner: LifecycleOwner, viewModel: CourseDetailViewModel) {
            viewModel.courseName.observe(owner, Observer<String?> {
                tvCourseName?.setText(it)
            })
            viewModel.teacherName.observe(owner, Observer<String?> {
                tvTeacherName?.setText(it)
            })
            viewModel.credit.observe(owner, Observer<String?> {
                tvCredit?.setText(it)
            })
            viewModel.location.observe(owner, Observer<String?> {
                tvLocation?.setText(it)
            })
            viewModel.timePeriod.observe(owner, Observer<String?> {
                tvTimePriod?.setText(it)
            })
            viewModel.textColor.observe(owner, Observer<Int?> {
                tvCourseName.setTextColor(it!!.toInt())
                tvTeacherName.setTextColor(it!!.toInt())
                tvCredit.setTextColor(it!!.toInt())
                tvLocation.setTextColor(it!!.toInt())
                tvTimePriod.setTextColor(it!!.toInt())
            })
            viewModel.cardColor.observe(owner, Observer<Int?> {
                card.setCardBackgroundColor(it!!.toInt())
            })

        }
    }

    class ToScheduleActViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: CardView

        init {
            card = itemView?.findViewById<CardView>(R.id.cv_course_jump)
        }

        fun bind(owner: LifecycleOwner, viewModel: ToScheduleActViewModel) {
            card.setOnClickListener { view ->
                viewModel.clickCmd
            }
        }
    }
    class CourseEmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCourseName: TextView
        val card: CardView
        init {
            tvCourseName = itemView?.findViewById<TextView>(R.id.tv_course_name)
            card = itemView?.findViewById<CardView>(R.id.cv_course_empty)
        }

        fun bind(owner: LifecycleOwner, viewModel: CourseEmptyViewModel) {
            viewModel.courseName.observe(owner, Observer<String?> {
                tvCourseName?.setText(it)
            })
            viewModel.textColor.observe(owner, Observer<Int?> {
                tvCourseName.setTextColor(it!!.toInt())
            })
            viewModel.cardColor.observe(owner, Observer<Int?> {
                card.setCardBackgroundColor(it!!.toInt())
            })
        }
    }

}