package com.twt.service.schedule2.view.exam

import android.arch.lifecycle.MutableLiveData
import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.Toast
import com.twt.service.schedule2.R
import com.twt.service.schedule2.model.exam.ExamTableBean
import com.twt.service.schedule2.model.exam.ExamTableService
import com.twt.service.schedule2.model.exam.examTableCache
import com.twt.wepeiyang.commons.experimental.extensions.*
import com.twt.wepeiyang.commons.mta.mtaExpose
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import com.twt.wepeiyang.commons.ui.rec.lightText
import com.twt.wepeiyang.commons.ui.text.spanned
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
    private val liveExamData = MutableLiveData<List<ExamTableBean>>()

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
            loadData()
        }
        bindLiveData()
        loadData()
    }

    private fun bindLiveData() {
        liveExamData.bindNonNull(this) { exams: List<ExamTableBean> ->
            val allList = mutableListOf<ExamTableBean>().apply {
                addAll(exams)
                sortBy { it.date + it.arrange }
            }
            mtaExpose("schedule_考表页_刷新成功")
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

    private fun loadData() {

        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
            val activity = this@ExamTableActivity.asReference()

            ExamTableService.getTable().awaitAndHandle {
                it.printStackTrace()
                Toasty.info(activity(), "加载出错，请稍后再试", Toast.LENGTH_SHORT).show()
                examTableCache.get().await()?.let {
                    liveExamData.postValue(it)
                }
                mtaExpose("schedule_考表页_加载出错")
            }?.data.apply {
                val table = this ?: return@launch
                examTableCache.set(table)
                liveExamData.postValue(table)
                Toasty.success(activity(), "加载成功", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
