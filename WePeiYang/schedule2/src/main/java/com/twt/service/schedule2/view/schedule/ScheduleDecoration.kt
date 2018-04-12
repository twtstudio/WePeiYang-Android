package com.twt.service.schedule2.view.schedule

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class ScheduleDecoration: RecyclerView.ItemDecoration() {

    val paint = Paint().apply {
        color = Color.GREEN
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, parent, state)
        val leftMargin = parent.paddingStart
        val topMargin = parent.paddingTop
        canvas.drawRect(0F, topMargin.toFloat(), leftMargin.toFloat(), parent.height.toFloat(), paint)
        canvas.drawRect(0F,0F, parent.width.toFloat(),topMargin.toFloat(),Paint().apply { color = Color.RED })
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


    private fun getFirstColunmSize(recyclerView: RecyclerView): Int {
        val adapter = recyclerView.adapter as ScheduleAdapter
        return adapter.firstColumnSize
    }

    private fun getFirstRowIndexList(recyclerView: RecyclerView): List<Int> {
        val adapter = recyclerView.adapter as ScheduleAdapter
        return adapter.firstRowIndexList
    }
}