package com.twt.service.schedule2.view.schedule

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.twt.service.schedule2.R
import com.twt.service.schedule2.extensions.getWeekCourseFlated
import com.twt.service.schedule2.model.total.TotalCourseManager
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull

class ScheduleActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    val classtableProvider by lazyOf(TotalCourseManager.getTotalCourseManager(refreshTju = false,refreshAudit = false))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.schedule_act_main)
        recyclerView = findViewById(R.id.rec_main)
        classtableProvider.bindNonNull(this) {
            val result = it.getWeekCourseFlated(it.getCurrentWeek())
            val adapter = ScheduleAdapter(this)
            adapter.refreshCourseListFlat(result)

            val layoutManager = GridLayoutManager(this,12,LinearLayoutManager.HORIZONTAL,false)
            layoutManager.spanSizeLookup = CourseSpanSizeLookup(adapter.courseList)

            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter

        }
    }

}
