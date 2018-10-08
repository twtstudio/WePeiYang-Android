package com.twt.service.schedule2.calender

import android.Manifest
import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Bundle
import android.provider.CalendarContract.Calendars
import android.provider.CalendarContract.Events
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.twt.service.schedule2.model.total.TotalCourseManager
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.ui.rec.withItems
import io.multimoon.colorful.CAppCompatActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*
import android.provider.CalendarContract
import android.content.pm.PackageManager
import android.os.Build
import android.content.ContentResolver


class CalSyncActivity : CAppCompatActivity() {
    private val calanderURL = "content://com.android.calendar/calendars"
    private val calanderEventURL = "content://com.android.calendar/events"

    val EVENT_PROJECTION = arrayOf(Calendars._ID, Calendars.ACCOUNT_NAME, Calendars.CALENDAR_DISPLAY_NAME, Calendars.OWNER_ACCOUNT)
    private val PROJECTION_ID_INDEX = 0
    private val PROJECTION_ACCOUNT_NAME_INDEX = 1
    private val PROJECTION_DISPLAY_NAME_INDEX = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        back()
        EasyPermissions.requestPermissions(this, "微北洋需要系统日历的读取权限来同步课程表", 0, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)

    }

    fun real() {
        frameLayout {
            val recyclerView = RecyclerView(this@CalSyncActivity).lparams(matchParent, matchParent)
            recyclerView.layoutManager = LinearLayoutManager(this@CalSyncActivity)
            TotalCourseManager.getTotalCourseManager().bindNonNull(this@CalSyncActivity) {
                recyclerView.withItems {
                    it.getTodayCourse().forEach {

                    }
                }
            }
        }
    }


    @SuppressLint("SetTextI18n", "MissingPermission")
    fun back() {
        scrollView {
            verticalLayout {
                val text = textView("text here")
                button("Cal") {
                    onClick {
                        var cur: Cursor? = null
                        val cr = contentResolver
                        val uri = Calendars.CONTENT_URI
                        // String selection = "((" + Calendars.ACCOUNT_NAME +  // 给出查询条件，查询特定用户的日历
                        // " = ?) AND ("+ Calendars.ACCOUNT_TYPE + " = ?))";
                        // String[] selectionArgs = new String[]
                        // {"liushuaikobe@Gmail.com", "com.google"};
                        cur = cr.query(uri, EVENT_PROJECTION, null, null, null) // 查询条件为null，查询所有用户的所有日历
                        while (cur!!.moveToNext()) {
                            var calID: Long = 0
                            var displayName: String? = null
                            var accountName: String? = null
                            var ownerName: String? = null

                            calID = cur.getLong(PROJECTION_ID_INDEX)
                            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX)
                            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX)
                            ownerName = cur.getString(3)

                            text.append("日历ID：" + calID + "\n" + "日历显示名称：" + "\n"
                                    + displayName + "\n" + "日历拥有者账户名称：" + "\n"
                                    + accountName + " Owner: $ownerName")
                        }
                        cur.close()
                    }
                }

                val eventText = textView("Events")

                button("Eve") {
                    onClick {
                        val cr = contentResolver
                        val cur = cr.query(Events.CONTENT_URI, arrayOf(Events._ID, Events.TITLE, Events.DESCRIPTION, Events.DTSTART, Events.DTEND, Events.RRULE, Events.DURATION, Events.EVENT_TIMEZONE), null, null, null)/* Events._ID + "=" + myEventsId */ // 注释中的条件是是查询特定ID的events
                        eventText.setText("")
                        while (cur!!.moveToNext()) {
                            val tempEventsId = cur.getLong(0)
                            val tempEventsTitle = cur.getString(1)
                            val tempEventsDecription = cur.getString(2)
                            val tempEventsStartTime = cur.getString(3)
                            val tempEventsEndTime = cur.getString(4) ?: "0"
                            val tempEventsRule = cur.getString(5)
                            val duration = cur.getString(6)
                            Log.d("start", tempEventsStartTime)
                            Log.d("timeZone", cur.getString(7))
                            Log.d("rRule", tempEventsRule + " -->" + duration)
                            eventText.append(tempEventsId.toString() + "\n")
                            eventText.append(tempEventsTitle + " "
                                    + tempEventsDecription + "\n")
                            eventText.append(SimpleDateFormat(
                                    "yyyy/MM/dd hh:mm").format(Date(java.lang.Long
                                    .parseLong(tempEventsStartTime))) + "至")
                            eventText.append(SimpleDateFormat(
                                    "yyyy/MM/dd hh:mm").format(Date(java.lang.Long
                                    .parseLong(tempEventsEndTime))) + "\n")
                            eventText.append(" Rule: $tempEventsRule")
                            eventText.append(" DURA: $duration")
                        }
                        cur.close()
                    }
                }

                button("tryadd") {
                    onClick {
                        val livedata = TotalCourseManager.getTotalCourseManager()
                        livedata.bindNonNull(this@CalSyncActivity) {
                            val contentResolver = contentResolver
                            val courses = it.getCourseByWeek(it.getCurrentWeek())
                            courses.take(10).forEach {
                                val events = it.covertToCalEvent()
                                events.forEach {
                                    val contentValues = it.toContentValues()
                                    contentValues.put(Events.CALENDAR_ID, 4)
                                    Log.e("values", contentValues.toString())
                                    contentResolver.insert(Events.CONTENT_URI, contentValues)

                                }
                            }
                            livedata.removeObservers(this@CalSyncActivity)
                        }
                    }
                }

                button("delete") {
                    onClick {
                        val cr = contentResolver
                        val cur = cr.query(Events.CONTENT_URI, arrayOf(Events._ID, Events.TITLE, Events.DESCRIPTION, Events.DTSTART, Events.DTEND, Events.RRULE, Events.DURATION, Events.EVENT_TIMEZONE, Events.ORGANIZER), null, null, null)/* Events._ID + "=" + myEventsId */ // 注释中的条件是是查询特定ID的events
                        eventText.setText("")
                        while (cur!!.moveToNext()) {
                            val tempEventsId = cur.getLong(0)
                            val tempEventsTitle = cur.getString(1)
                            val tempEventsDecription = cur.getString(2)
                            val tempEventsStartTime = cur.getString(3)
                            val tempEventsEndTime = cur.getString(4) ?: "0"
                            val tempEventsRule = cur.getString(5)
                            val duration = cur.getString(6)
                            val timeZone = cur.getString(7)
                            val organizer = cur.getString(8)
                            if (organizer == "admin@wepeiyang.com" || tempEventsDecription.startsWith("逻辑班号")) {
                                Log.e("delete", deleteEvent(tempEventsId))
                            }
                            Log.d("start", tempEventsStartTime)
                            Log.d("timeZone", cur.getString(7))
                            Log.d("rRule", tempEventsRule + " -->" + duration)

                        }
                        cur.close()
                    }
                }

            }
        }
    }

    /**
     * 删除指定ID的日历事件及其提醒
     * @param eventId
     * @return
     */
    @SuppressLint("MissingPermission")
    private fun deleteEvent(eventId: Long): String {
        val cr = contentResolver
        val uri = CalendarContract.Events.CONTENT_URI
        val sb = StringBuilder()
        var deletedCount = 0

        val selection = "(" + CalendarContract.Events._ID + " = ?)"
        val selectionArgs = arrayOf(eventId.toString())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PackageManager.PERMISSION_GRANTED == checkSelfPermission("android.permission.WRITE_CALENDAR")) {
                deletedCount = cr.delete(uri, selection, selectionArgs)
            } else {
                return "请授予编辑日历的权限"
            }
        } else {
            deletedCount = cr.delete(uri, selection, selectionArgs)
        }

        val reminderSelection = "(" + CalendarContract.Reminders.EVENT_ID + " = ?)"
        val reminderSelectionArgs = arrayOf(eventId.toString())

        val deletedReminderCount = cr.delete(CalendarContract.Reminders.CONTENT_URI, reminderSelection, reminderSelectionArgs)

        sb.append("删除日历事件成功！\n")
        sb.append("删除事件数:$deletedCount").append("\n")
        sb.append("删除事件提醒数:$deletedReminderCount").append("\n")
        sb.append("\n")

        return sb.toString()
    }

}