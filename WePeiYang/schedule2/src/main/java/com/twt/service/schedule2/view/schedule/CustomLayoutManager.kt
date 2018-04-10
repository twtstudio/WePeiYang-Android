package com.twt.service.schedule2.view.schedule

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

class CustomLayoutManager(
        val recyclerView: RecyclerView,
        val context: Context,
        spanCount: Int,
        orientation: Int,
        reverseLayout: Boolean
) : GridLayoutManager(context, spanCount, orientation, reverseLayout) {
    override fun onMeasure(recycler: RecyclerView.Recycler?, state: RecyclerView.State?, widthSpec: Int, heightSpec: Int) {
        super.onMeasure(recycler, state, widthSpec, heightSpec)

    }

    override fun measureChild(child: View, widthUsed: Int, heightUsed: Int) {
        super.measureChild(child, widthUsed, heightUsed)
        val lp = child.getLayoutParams() as LayoutParams

        val mode = View.MeasureSpec.EXACTLY
        val size = recyclerView.measuredWidth / 7
        val measureSpec = View.MeasureSpec.makeMeasureSpec(size, mode)
        val widthSpec = getChildMeasureSpec(width, widthMode,
                paddingLeft + paddingRight + widthUsed, lp.width,
                canScrollHorizontally())
        val heightSpec = measureSpec
        child.measure(widthSpec, heightSpec)

    }
}