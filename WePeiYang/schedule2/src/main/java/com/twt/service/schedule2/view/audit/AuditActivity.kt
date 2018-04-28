package com.twt.service.schedule2.view.audit

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.twt.service.schedule2.R
import com.twt.service.schedule2.model.audit.AuditCourseManager
import com.twt.service.schedule2.model.audit.auditPopluarLiveData
import com.twt.service.schedule2.view.adapter.*
import com.twt.service.schedule2.view.custom.SingleTextItem
import com.twt.service.schedule2.view.custom.singleText
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import es.dmoral.toasty.Toasty
import io.multimoon.colorful.CAppCompatActivity
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.textColor

class AuditActivity : CAppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.schedule_act_audit)
        recyclerView = findViewById(R.id.rec_audit)
        val itemManager = ItemManager()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AuditActivity)
            itemAnimator = DefaultItemAnimator()
            adapter = ItemAdapter(itemManager)
        }

        val titleTextBuilder: TextView.() -> Unit = {
            textSize = 16f
            textColor = Color.parseColor("#60000000")
            typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
            setOnClickListener {
                async(CommonPool + QuietCoroutineExceptionHandler) {
                    AuditCourseManager.refreshAuditClasstable()
                }
            }
        }

        auditPopluarLiveData.bindNonNull(this) {
            val snapshot = itemManager.itemListSnapshot.toMutableList()
            snapshot.apply {
                removeAll { it is IconLabelItem }
                removeAll { it is SingleTextItem && it.text == "热门课程" }

                val needItems = mutableListOf<Item>().apply {
                    singleText("热门课程", builder = titleTextBuilder)
                    it.forEach {
                        iconLabel {
                            imgResId = R.drawable.ic_schedule_event
                            content = it.course.name
                            clickBlock = {
                                Toasty.success(this@AuditActivity, "Fuck").show()
                            }
                        }
                    }
                }

                snapshot.addAll(0, needItems)
            }
            itemManager.refreshItems(snapshot)
        }

        auditPopluarLiveData.refresh(CacheIndicator.REMOTE)

        AuditCourseManager.getAuditListLive().bindNonNull(this@AuditActivity) {
            val snapshot = itemManager.itemListSnapshot.toMutableList()
            snapshot.apply {

                removeAll { it is AuditCourseItem }
                removeAll { it is SingleTextItem && it.text == "我的蹭课" }

                singleText("我的蹭课", titleTextBuilder)
                it.forEach {
                    auditCourseItem(it)
                    if (it.infos.size > 1) {
                        for (i in 1 until it.infos.size) {
                            val additional = it.copy(infos = listOf(it.infos[i]))
                            auditCourseItem(additional)
                        }
                    }
                }

            }
            itemManager.refreshItems(snapshot)
        }
    }
}