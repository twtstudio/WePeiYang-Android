package com.twt.service.schedule2.view.schedule

import android.arch.lifecycle.LiveData
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.twt.service.schedule2.R
import com.twt.service.schedule2.extensions.RefreshCallback
import com.twt.service.schedule2.extensions.getScreenHeight
import com.twt.service.schedule2.extensions.getWeekCourseFlated
import com.twt.service.schedule2.extensions.getWeekCourseMatrix
import com.twt.service.schedule2.model.MergedClassTableProvider
import com.twt.service.schedule2.model.total.TotalCourseManager
import com.twt.service.schedule2.view.detail.CourseDetailBottomFragment
import com.twt.service.schedule2.view.detail.MultiCourseDetailFragment
import com.twt.service.schedule2.view.week.WeekSelectAdapter
import com.twt.service.schedule2.view.week.WeekSquareView
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import es.dmoral.toasty.Toasty
import io.multimoon.colorful.CAppCompatActivity
import kotlinx.android.synthetic.main.schedule_act_main.*
import java.net.SocketTimeoutException

class ScheduleActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    var currentWeek = -1

    val refreshCallback: RefreshCallback = {
        when(it) {
            is RefreshState.Success -> {
                if (it.message == CacheIndicator.REMOTE) {
                    Toasty.success(this,"成功拉取课程表数据").show()
                }
            }
            is RefreshState.Failure -> {
                val throwable = it.throwable
                when(throwable) {
                    is IllegalStateException -> throwable.message?.let {
                        Toasty.error(this,it).show()
                    }
                    is SocketTimeoutException -> {
                        Toasty.success(this,"虽然从服务器拉取数据失败了，但是我们机智的为你保存了一份本地课表ʕ•ٹ•ʔ")
                    }
                    else -> throwable.printStackTrace()
                }
            }
            is RefreshState.Refreshing -> {
                Toasty.info(this,"正在尝试刷新课程表数据").show()
            }
        }
    }

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
//            Toasty.success(this,"current: $it").show()
            currentWeek = it
            TotalCourseManager.getTotalCourseManager(refreshTju = false, refreshAudit = false)
        }
        weekSelectRecyclerView.adapter = weekSelectAdapter
        weekSelectRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val weekSquareDataList = generateDefaultWeekMatrix()
        weekSelectAdapter.refreshWeekSquareData(weekSquareDataList)

        val refreshBtn: ImageView = findViewById(R.id.iv_toolbar_refresh)
        refreshBtn.setOnClickListener {
            CourseRefreshBottomFragment.showCourseDetailBottomSheet(this, refreshCallback)
        }

        val classtableProvider: LiveData<MergedClassTableProvider> = TotalCourseManager.getTotalCourseManager(
                refreshTju = false,
                refreshAudit = false,
                refreshCallback = {
                    // 这里不想让它多弹出Toast
                    when(it) {
                        is RefreshState.Failure -> {
                            val throwable = it.throwable
                            when(throwable) {
                                is IllegalStateException -> throwable.message?.let {
                                    Toasty.error(this,it).show()
                                }
                            }
                        }
                    }
                }
        )

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
