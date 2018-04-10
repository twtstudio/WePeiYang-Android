package com.twt.service.schedule2.view.schedule

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView

class CustomLayoutManager(
        val recyclerView: RecyclerView,
        val context: Context,
        spanCount: Int,
        orientation: Int,
        reverseLayout: Boolean
) : GridLayoutManager(context, spanCount, orientation, reverseLayout) {
    override fun onMeasure(recycler: RecyclerView.Recycler?, state: RecyclerView.State?, widthSpec: Int, heightSpec: Int) {
        super.onMeasure(recycler, state, widthSpec, heightSpec)
        val measuredWith = recyclerView.measuredWidth
        val measuredHeight = recyclerView.measuredHeight
    }
}