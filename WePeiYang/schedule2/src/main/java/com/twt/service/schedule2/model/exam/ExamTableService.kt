package com.twt.service.schedule2.model.exam

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import com.twt.wepeiyang.commons.experimental.cache.Cache
import com.twt.wepeiyang.commons.experimental.cache.hawk
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Deferred
import org.jetbrains.anko.alert
import retrofit2.http.*

interface ExamTableService {

    @GET("v1/examtable")
    fun getTable(): Deferred<CommonBody<List<ExamTableBean>>>

    companion object : ExamTableService by ServiceFactory()
}

val examTableCache = Cache.hawk<List<ExamTableBean>>("exam_table_key")

fun addEvent(context: Context, title: String, location: String, begin: Long, end: Long, exam: ExamTableBean) {
    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(CalendarContract.Events.TITLE, title)
        putExtra(CalendarContract.Events.EVENT_LOCATION, location)
        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end)
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.alert {
            this.title = "添加此考试课程到日历"
            message = "$title \n$location \n${exam.date} ${exam.arrange}"
            positiveButton("添加") {
                context.startActivity(intent)
            }
            negativeButton("取消") {
                it.dismiss()
            }
        }.show()
    } else {
        Toasty.error(context, "找不到日历软件，无法添加到系统日程").show()
    }
}