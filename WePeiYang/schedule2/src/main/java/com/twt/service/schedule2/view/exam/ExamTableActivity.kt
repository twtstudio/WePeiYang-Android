package com.twt.service.schedule2.view.exam

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.Toast
import com.twt.service.schedule2.R
import com.twt.service.schedule2.model.exam.ExamTableBean
import com.twt.service.schedule2.model.exam.ExamTablePreference
import com.twt.service.schedule2.model.exam.ExamTableService
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import com.twt.wepeiyang.commons.ui.rec.SingleTextItem
import com.twt.wepeiyang.commons.ui.rec.lightText
import com.twt.wepeiyang.commons.ui.text.spanned
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.coroutines.experimental.asReference
import org.jetbrains.anko.dip
import org.jetbrains.anko.horizontalMargin
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

class ExamTableActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var recyclerView: RecyclerView
    private lateinit var itemManager: ItemManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.WHITE
        enableLightStatusBarMode(true)
        setContentView(R.layout.schedule_activity_exam_table)

        toolbar = findViewById(R.id.toolbar)
        recyclerView = findViewById(R.id.table_rv)

        itemManager = ItemManager()
        recyclerView.adapter = ItemAdapter(itemManager)
        recyclerView.layoutManager = LinearLayoutManager(this)

        swipeRefreshLayout = findViewById(R.id.table_srl)
        swipeRefreshLayout.setOnRefreshListener {
            refresh()
        }

        load()
        refresh()
    }

    private fun refresh() {
        Toasty.info(this, "正在加载", Toast.LENGTH_SHORT).show()

        val activity = this@ExamTableActivity.asReference()
        launch(UI + QuietCoroutineExceptionHandler) {

            ExamTableService.getTable().awaitAndHandle {
                it.printStackTrace()
                Toasty.info(activity(), "加载出错，请稍后再试", Toast.LENGTH_SHORT).show()
                load()
            }?.data.apply {
                val table = this ?: return@launch
                ExamTablePreference.exams = table.toTypedArray()
                Toasty.success(activity(), "加载成功", Toast.LENGTH_SHORT).show()
                load()
            }
        }
    }

    private fun load() {
        val allList = mutableListOf<ExamTableBean>().apply {
            addAll(ExamTablePreference.exams)
            sortBy { it.date + it.arrange }
        }
        itemManager.refreshAll {
            lightText("") {
                text = "<span style=\"color:#E70C57\";>注意：数据来自教育教学信息管理系统, 具体安排以学院和学校通知为准</span>".spanned
                layoutParams = FrameLayout.LayoutParams(matchParent, wrapContent).apply {
                    horizontalMargin = dip(16)
                }
                gravity = Gravity.START
            }
            allList.forEachIndexed { index, examBean ->
                val nextExam = allList.getOrNull(index + 1)
                if (index == 0) {
                    lightText(examBean.date) // 增加日期分割符号
                }
                examTableItem(examBean)
                if (examBean.date != nextExam?.date) {
                    nextExam?.let { lightText(it.date) } // 增加日期分割符号
                }
            }
        }
        swipeRefreshLayout.isRefreshing = false
        recyclerView.scrollToPosition(0)
    }
}
