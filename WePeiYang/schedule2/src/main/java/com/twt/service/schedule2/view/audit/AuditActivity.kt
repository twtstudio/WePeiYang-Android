package com.twt.service.schedule2.view.audit

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.twt.service.schedule2.model.audit.AuditCourseManager
import com.twt.service.schedule2.view.adapter.refreshItems
import com.twt.service.schedule2.view.custom.singleText
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import io.multimoon.colorful.CAppCompatActivity
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.textColor
import org.jetbrains.anko.verticalLayout

class AuditActivity : CAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val builder: TextView.() -> Unit = {
            textSize = 16f
            textColor = Color.parseColor("#60000000")
            typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
            setOnClickListener {
                async(CommonPool + QuietCoroutineExceptionHandler) {
                    AuditCourseManager.refreshAuditClasstable()
                }
            }
        }

        verticalLayout {
            val recyclerView = RecyclerView(context).lparams(width = matchParent, height = matchParent).apply {
                layoutManager = LinearLayoutManager(this@AuditActivity)
                itemAnimator = DefaultItemAnimator()
            }
            addView(recyclerView)

            AuditCourseManager.getAuditListLive().bindNonNull(this@AuditActivity) {
                recyclerView.refreshItems {
                    singleText("我的蹭课", builder)


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
            }
        }
    }
}