package com.twt.service.schedule2.view.schedule

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.twt.service.schedule2.R
import com.twt.service.schedule2.extensions.getScreenHeight
import com.twt.service.schedule2.extensions.getWeekCourseFlated
import com.twt.service.schedule2.extensions.getWeekCourseMatrix
import com.twt.service.schedule2.model.total.TotalCourseManager
import com.twt.service.schedule2.view.detail.CourseDetailBottomFragment
import com.twt.service.schedule2.view.detail.MultiCourseDetailFragment
import com.twt.service.schedule2.view.week.WeekSelectAdapter
import com.twt.service.schedule2.view.week.WeekSquareView
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.schedule_act_main.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.alert

class ScheduleActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    val classtableProvider by lazyOf(TotalCourseManager.getTotalCourseManager(refreshTju = false, refreshAudit = true))
    var currentWeek = -1

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
        recyclerView.itemAnimator = DefaultItemAnimator()
        // 课程表适配器的点击事件处理
        adapter.clickListener = {
            if (it.next.size == 0) {
                CourseDetailBottomFragment.showCourseDetailBottomSheet(this, it)
            } else {
                MultiCourseDetailFragment.showCourseDetailBottomSheet(this, it)
            }
        }
        val decoration = ScheduleDecoration()
        recyclerView.addItemDecoration(decoration)
        recyclerView.isNestedScrollingEnabled = false

        val weekSelectAdapter = WeekSelectAdapter(this)
        val weekSelectRecyclerView: RecyclerView = findViewById(R.id.rec_week_select)
        weekSelectAdapter.clickListener = {
            Toasty.success(this,"current: $it").show()
            currentWeek = it
            TotalCourseManager.getTotalCourseManager(refreshTju = false, refreshAudit = false)
        }
        weekSelectRecyclerView.adapter = weekSelectAdapter
        weekSelectRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val weekSquareDataList = generateDefaultWeekMatrix()

        weekSelectAdapter.refreshWeekSquareData(weekSquareDataList)

        classtableProvider.bindNonNull(this) {
            var week = 0
            if ( currentWeek == -1) {
               week = it.getCurrentWeek()
            } else {
                week = currentWeek
            }
            val result = it.getWeekCourseFlated(week)
            decoration.week = week
            adapter.refreshCourseListFlat(result)

            layoutManager.spanSizeLookup = CourseSpanSizeLookup(adapter.courseList)
            recyclerView.invalidateItemDecorations()

            // todo: 这部分应该去异步执行 但是目前有异常 所以过段时间再看看
            weekSquareDataList.removeAll { true }
            for (i in 1..25) {
                val weekMatrix = it.getWeekCourseMatrix(i)
                // 硬编码自定义view底部Text的行为比较僵硬 自定义view里面对字符串做判断可能导致维护时候的bug
                // 但是暂时这样子吧
                var btmText = ""
                if (i == week && i != it.getCurrentWeek()) {
                    btmText = "选中(非本周)"
                } else if (i == week && i == it.getCurrentWeek()) {
                    btmText = "选中(本周)"
                } else if (i != week && i == it.getCurrentWeek()) {
                    btmText = "本周"
                }
                weekSquareDataList.add(WeekSquareView.WeekSquareData(
                        weekInt = i,
                        booleanPoints = weekMatrix,
                        currentWeekText = btmText
                ))
            }

            weekSelectAdapter.refreshWeekSquareData(weekSquareDataList)

        }
    }

    private fun generateDefaultWeekMatrix(): MutableList<WeekSquareView.WeekSquareData> {
        return mutableListOf<WeekSquareView.WeekSquareData>().apply {
            for (i in 1..25) {
                add(WeekSquareView.WeekSquareData(
                        weekInt = i,
                        booleanPoints = WeekSquareView.WeekSquareData.generateDefaultMatrix()
                ))
            }
        }
    }

}
