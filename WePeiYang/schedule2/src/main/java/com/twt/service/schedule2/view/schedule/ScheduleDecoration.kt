package com.twt.service.schedule2.view.schedule

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.twt.service.schedule2.extensions.*
import com.twt.service.schedule2.model.AbsClasstableProvider
import com.twt.service.schedule2.model.Classtable
import com.twt.service.schedule2.model.CommonClassTable
import java.text.SimpleDateFormat
import java.util.*

class ScheduleDecoration : RecyclerView.ItemDecoration() {

    var week: Int = 1 // 设置Schedule上面的日期指示栏
    val backGroundPaint = Paint().apply {
        color = rgbPercent(0.97f, 0.97f, 0.95f)
    }
    val textPaint = Paint().apply {
        color = rgbPercent(0.55f, 0.55f, 0.55f)
        textSize = dp2px(11f)
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    /**
     * 用一些无关课程的方法
     */
    val mockClasstableProvider: AbsClasstableProvider = CommonClassTable(Classtable(courses = listOf()))

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, parent, state)
        val leftMargin = parent.paddingStart.toFloat()
        val topMargin = parent.paddingTop.toFloat()
        val parentHeight = parent.height.toFloat()
        val parentWidth = parent.width.toFloat()
        layer {
            // 绘制左侧栏
            canvas.drawRect(0F, topMargin, leftMargin, parentHeight, backGroundPaint)
            val availableHeight = parentHeight - topMargin
            val perItemHeight = availableHeight / 12    // 一共12小节课
            val x = leftMargin / 2
            for (i in 1..12) {
                val y = topMargin + perItemHeight * (i - 0.5).toFloat() + 16
                // (0.5 + (i-1))* perHeight 更好理解吧 剩下的16f是弥补text绘制的偏移
                canvas.drawText("$i", x, y, textPaint)
            }

        }
        layer {
            // 绘制上侧栏
            canvas.drawRect(0F, 0F, parentWidth, topMargin, backGroundPaint)
            canvas.drawLine(0f, topMargin, parentWidth, topMargin, Paint().apply {
                color = rgbPercent(0.94f, 0.94f, 0.92f)
                style = Paint.Style.STROKE
                strokeWidth = 2f
            })

            val spanSize = when ((parent.adapter as ScheduleAdapter).displayType) {
                ScheduleAdapter.ScheduleDisplayType.FIVEDAYS -> 5
                ScheduleAdapter.ScheduleDisplayType.SEVENDAYS -> 7
                else -> 7
            }

            val monthLabelWidth = leftMargin
            val availableWith = parentWidth - monthLabelWidth
            val perItemWidth = availableWith / spanSize

            val (month, days) = getWeekDaysString(spanSize)
            val y = topMargin / 2
            val todayInt = mockClasstableProvider.getDayOfWeek(dayUnix = mockClasstableProvider.currentUnixTime)
            canvas.textCenter(month.split("\n"), textPaint, monthLabelWidth / 2, y, Paint.Align.CENTER)
            days.forEachIndexed { index, string ->
                val strList = string.split("\n")
                val x = monthLabelWidth + (index + 0.5).toFloat() * perItemWidth
                if (index + 1 == todayInt) {
                    // 绘制当前的星期 比如说 今天是星期四
                    canvas.drawRect(x - perItemWidth/2,y - topMargin/2,x+perItemWidth/2,y + topMargin/2,Paint().apply {
                        color = rgbPercent(0.93f, 0.93f, 0.91f)
                    })
                }
                canvas.textCenter(strList, textPaint, x, y, Paint.Align.CENTER)
            }

        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
//        val position = parent.getChildAdapterPosition(view)
//        val firstColumnSize = getFirstColunmSize(parent)
//        val firstRowIndexList = getFirstRowIndexList(parent)
//        when {
//            position == 0 -> outRect.set(leftMargin,topMargin,0,0) // 绘制第一个 左边上边间距
//            position < firstColumnSize -> outRect.set(leftMargin,0,0,0) // 第一列左边
//            position in firstRowIndexList -> outRect.set(0,topMargin,0,0) // 第一行上边
//            else -> outRect.set(0,0,0,0)
//        }
    }

    private fun rgbPercent(red: Float, green: Float, blue: Float): Int {
        return -0x1000000 or
                ((red * 255.0f + 0.5f).toInt() shl 16) or
                ((green * 255.0f + 0.5f).toInt() shl 8) or
                (blue * 255.0f + 0.5f).toInt()
    }

    /**
     * 生成月份 和 星期+日期
     * @param spanSize 5天课程表 还是 7天课程表
     */
    private fun getWeekDaysString(spanSize: Int = 7): Pair<String, List<String>> {
        val offset = 3600L // 加一个偏移量... 因为按照0点计算不保险
        val dayOfSeconds = 86400L
        val startUnixWithOffset = termStart + offset + (week - 1) * dayOfSeconds * 7
        val dayUnixList = mutableListOf<Long>() // 一周内每天的时间戳
        for (i in 0 until spanSize) {
            dayUnixList.add(startUnixWithOffset + dayOfSeconds * i)
        }
        // 要加上区域？
        val dateFormatDay = SimpleDateFormat("E\ndd")
        val dateFormatMonth = SimpleDateFormat("MM")

        val month: String = dateFormatMonth.format(Date(dayUnixList[0] * 1000L)).removePrefix("0") + "\n月"

        val list: List<String> = dayUnixList.map {
            val date = Date(it * 1000L)
            dateFormatDay.format(date)
        }
        return month to list
    }


}