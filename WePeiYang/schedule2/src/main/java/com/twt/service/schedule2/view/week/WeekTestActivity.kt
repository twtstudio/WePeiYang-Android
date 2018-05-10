package com.twt.service.schedule2.view.week

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.twt.service.schedule2.R
import com.twt.service.schedule2.view.adapter.indicatorText
import com.twt.service.schedule2.view.audit.buttonItem
import com.twt.service.schedule2.view.custom.singleText
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.textColor

class WeekTestActivity : AppCompatActivity() {

    val itemManager: ItemManager = ItemManager()
    var page = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.schedule_act_week_test)
        val recyclerView: RecyclerView = findViewById(R.id.rec_week_test)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@WeekTestActivity)
            adapter = ItemAdapter(itemManager)
            itemAnimator = DefaultItemAnimator()
        }

        itemManager.refreshAll {
            singleText("Today")
            buttonItem("狗贼到底啦") {
                setOnClickListener {
                    Toasty.success(this@WeekTestActivity, "fuck").show()
                    loadPage()
                }
            }
        }
        loadPage()

    }

    fun loadPage() {
        launch(UI + QuietCoroutineExceptionHandler) {
            val response = NewsApi.getNewsByPage(page++).await()
            val data = response.data ?: throw IllegalStateException("新闻数据错误")
            itemManager.autoRefresh {
                val item = removeAt(itemManager.size - 1)
                indicatorText("Page: $page")
//                removeAll { it is ButtonItem && it.text == "狗贼到底啦" }
                data.forEach {
                    singleText("${it.subject} -> ${it.summary}") {
                        textSize = 16f
                        textColor = Color.BLACK
                    }
                }
                add(item)
            }
        }

    }
}