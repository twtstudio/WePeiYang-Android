package com.twt.service.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.orhanobut.hawk.Hawk
import com.orhanobut.logger.Logger
import com.twt.service.R
import com.twt.service.home.HomeNewActivity
import com.twt.service.schedule2.model.Course
import com.twt.service.schedule2.model.total.TotalCourseManager
import com.twt.service.schedule2.view.schedule.ScheduleActivity
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.mta.mtaExpose
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by retrox on 24/03/2017.
 */

class ScheduleWidgetProvider : AppWidgetProvider() {
    private var manager: AppWidgetManager? = null

    private val todayString: String
        get() {
            val stringBuilder = StringBuilder()
            val time = Calendar.getInstance().time.let { dateFormate.format(it) }
            stringBuilder.append(time)
            stringBuilder.append("  ")
            val s = "星期" + getChineseCharacter(getTodayNumber())
            stringBuilder.append(s)
            return stringBuilder.toString()
        }


    private fun getTodayNumber(): Int {
        val day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        return if (day == Calendar.SUNDAY) {
            7
        } else {
            day - 1
        }
    }

    private fun getChineseCharacter(num: Int): String {
        val cDay = arrayOf("零", "一", "二", "三", "四", "五", "六", "日")
        return cDay[num]

    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        mtaExpose("schedule_课程表小部件被启用", context ?: CommonContext.application.applicationContext)
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.twt.appwidget.refresh") {
            val name = ComponentName(context, ScheduleWidgetProvider::class.java)
            this@ScheduleWidgetProvider.onUpdate(context, AppWidgetManager.getInstance(context), AppWidgetManager.getInstance(context).getAppWidgetIds(name))
            Logger.d("widget refresh click!")
        }
        super.onReceive(context, intent)
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {

            val intent = Intent(context, HomeNewActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

            val remoteViews = RemoteViews(context.packageName, R.layout.widget_schedule)
            remoteViews.setOnClickPendingIntent(R.id.widget_framelayout, pendingIntent)

            val imageClickIntent = Intent(context, ScheduleWidgetProvider::class.java)
            imageClickIntent.action = "com.twt.appwidget.refresh"
            val imageClickPendingIntent = PendingIntent.getBroadcast(context, 0, imageClickIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            remoteViews.setOnClickPendingIntent(R.id.widget_image_button, imageClickPendingIntent)

            remoteViews.setTextViewText(R.id.widget_today_date, todayString)

            manager = appWidgetManager
            getData(context, appWidgetId, remoteViews, false)
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)

        }
    }

    private fun setupList(context: Context, appWidgetId: Int, remoteViews: RemoteViews, list: List<Course>) {
        val serviceIntent = Intent(context, WidgetService::class.java)

        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        serviceIntent.data = Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME))
        Hawk.put("scheduleCache2", list)

        val startActivityIntent = Intent(context, ScheduleActivity::class.java).apply {
            putExtra("from", "Widget") // 埋点用
        }
        val startActivityPendingIntent = PendingIntent.getActivity(context, 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        remoteViews.setPendingIntentTemplate(R.id.widget_listview, startActivityPendingIntent)

        remoteViews.setRemoteAdapter(R.id.widget_listview, serviceIntent)
        remoteViews.setEmptyView(R.id.widget_listview, R.id.widget_empty_view)

        manager?.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_listview)
        manager?.updateAppWidget(appWidgetId, remoteViews)

    }

    fun getData(context: Context, appWidgetId: Int, remoteViews: RemoteViews, update: Boolean) {

        val classTableProviderLiveData = TotalCourseManager.getTotalCourseManager(false, false, false) { cacheIndicatorRefreshState -> null }
        classTableProviderLiveData.observeForever { mergedClassTableProvider ->
            if (mergedClassTableProvider == null) return@observeForever
            try {
                val courses = mergedClassTableProvider.getTodayCourse().filter {
                    it.dayAvailable && it.weekAvailable
                }.sortedBy { course ->
                    course.arrange.getOrNull(0)?.start ?: 100 //如果越界或者没有就给他个100
                }
                setupList(context, appWidgetId, remoteViews, courses)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }

    companion object {
        val dateFormate = SimpleDateFormat("yyyy/MM/dd", Locale.CHINA)
    }
}
