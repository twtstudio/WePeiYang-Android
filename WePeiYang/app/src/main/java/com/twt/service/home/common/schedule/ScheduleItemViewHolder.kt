package com.twt.service.home.common.schedule

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.Observable
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.kelin.mvvmlight.base.ViewModel
import com.twt.service.R
import com.twt.service.home.common.gpaItem.GpaItemViewModel
import com.twtstudio.retrox.gpa.GpaChartBindingAdapter
import com.twtstudio.retrox.schedule.ScheduleActivity

/**
 * Created by retrox on 23/10/2017.
 */

class ScheduleItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val board: LinearLayout = itemView.findViewById(R.id.ll_courses)
    val title: TextView = itemView.findViewById(R.id.tv_schedule_title)
    val inflater = LayoutInflater.from(itemView.context)

    fun bind(owner: LifecycleOwner, viewModel: ScheduleViewModel) {
        board.setOnClickListener {
            val intent = Intent(itemView.context, ScheduleActivity::class.java)
            itemView.context.startActivity(intent)
        }
        viewModel.title.observe(owner, Observer<String?> {
            title.setText(it)
        })
        viewModel.liveItems.observe(owner, Observer<List<ViewModel>> {
//            board.removeAllViews()
//            board.invalidate()
            it?.apply {
                for (vm in it) {
                    Log.d("schedule", vm.toString())
                    val view = inflater.inflate(R.layout.item_common_course, board, false)
                    CourseItemHolder(view).apply {
                        bind(owner, vm)
                    }
                    board.addView(view)
                }
            }

        })
    }

    class CourseItemHolder(itemView: View) {
        val cardview: CardView = itemView.findViewById(R.id.card_home_item_course)
        val textview: TextView = itemView.findViewById(R.id.tv_home_item_course)
        fun bind(owner: LifecycleOwner, viewModel: ViewModel) {
            if (viewModel is CourseBriefViewModel) {
                viewModel.apply {
                    cardColor.observe(owner, Observer<Int> {
                        it?.let { it1 -> cardview.setCardBackgroundColor(it1) }
                    })
                    courseName.observe(owner, Observer<String> {
                        it?.let { it1 -> textview.setText(it1) }
                    })
                    textColor.observe(owner, Observer<Int> {
                        it?.let { it1 -> textview.setTextColor(it1) }
                    })
                }


            }
        }
    }
}
