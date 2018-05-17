package com.twtstudio.service.dishesreviews.canteen

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View


/**
 * Created by SGXM on 2018/5/11.
 */
class FoodItemDividerDecoration() : RecyclerView.ItemDecoration() {

    val mPaint = Paint().apply {
        color = Color.parseColor("#EFEFF3")
        strokeWidth = 4f
    }


    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        var pos = (view?.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        if (pos != parent?.adapter?.itemCount!! - 1) {
            outRect?.bottom = 2
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDraw(c, parent, state)
        var childcount = parent.childCount
        for (i in 0 until childcount) {
            val view = parent.getChildAt(i)
            val pos = (view?.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
            if (pos != parent.adapter.itemCount - 1) {
                val bottom = view.bottom * 1f
                val left = 24f + parent.paddingLeft
                val right = view.right - 24f
                c.drawLine(left, bottom, right, bottom, mPaint)

            }
        }
    }


}