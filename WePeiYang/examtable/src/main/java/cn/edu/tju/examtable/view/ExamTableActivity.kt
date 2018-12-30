package cn.edu.tju.examtable.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import cn.edu.tju.examtable.R
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar
import android.widget.Toast
import cn.edu.tju.examtable.service.ExamBean
import cn.edu.tju.examtable.service.ExamtablePreference
import cn.edu.tju.examtable.service.ExamtableService
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import com.twt.wepeiyang.commons.ui.rec.lightText
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.coroutines.experimental.asReference

class ExamTableActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var recyclerView: RecyclerView
    lateinit var itemManager: ItemManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_table)

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
            val result = ExamtableService.getTable().awaitAndHandle {
                it.printStackTrace()
                Toasty.info(activity(), "加载出错，请稍后再试", Toast.LENGTH_SHORT).show()
            }?.data

            val table = result ?: return@launch

            ExamtablePreference.exams = table.toTypedArray()
            load()
        }
    }

    private fun load() {
        val allList = mutableListOf<ExamBean>().apply {
            addAll(ExamtablePreference.exams)
            sortBy { it.date + it.arrange }
        }
        itemManager.refreshAll {
            allList.forEachIndexed { index, examBean ->
                val nextExam = allList.getOrNull(index + 1)
                if (index == 0) {
                    lightText(examBean.date) // 增加日期分割符号
                }
                examItem(examBean)
                if (examBean.date != nextExam?.date) {
                    nextExam?.let { lightText(it.date) } // 增加日期分割符号
                }
            }
        }
        swipeRefreshLayout.isRefreshing = false
        recyclerView.scrollToPosition(0)
    }
}
