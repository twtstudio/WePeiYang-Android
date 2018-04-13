package com.twt.service.schedule2.view.schedule

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.twt.service.schedule2.R
import com.twt.service.schedule2.extensions.getScreenHeight
import com.twt.service.schedule2.extensions.getWeekCourseFlated
import com.twt.service.schedule2.model.total.TotalCourseManager
import com.twt.service.schedule2.view.detail.CourseDetailBottomFragment
import com.twt.service.schedule2.view.detail.MultiCourseDetailFragment
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.schedule_act_main.*
import org.jetbrains.anko.alert

class ScheduleActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    val classtableProvider by lazyOf(TotalCourseManager.getTotalCourseManager(refreshTju = false, refreshAudit = true))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.schedule_act_main)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.rec_main)
        val layoutParams = recyclerView.layoutParams
        layoutParams.height = (getScreenHeight() * 1.2).toInt()
        recyclerView.layoutParams = layoutParams
        val adapter = ScheduleAdapter(this)
        val layoutManager = GridLayoutManager(this, 12, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        adapter.clickListener = {
            if (it.next.size == 0) {
                CourseDetailBottomFragment.showCourseDetailBottomSheet(this, it)
            } else {
                MultiCourseDetailFragment.showCourseDetailBottomSheet(this, it)
            }
        }
        val decoration = ScheduleDecoration()
        recyclerView.addItemDecoration(decoration)

        classtableProvider.bindNonNull(this) {
            val week = it.getCurrentWeek()
            val result = it.getWeekCourseFlated(week)
            decoration.week = week
            adapter.refreshCourseListFlat(result)

            layoutManager.spanSizeLookup = CourseSpanSizeLookup(adapter.courseList)
            recyclerView.invalidateItemDecorations()

        }
    }

}
