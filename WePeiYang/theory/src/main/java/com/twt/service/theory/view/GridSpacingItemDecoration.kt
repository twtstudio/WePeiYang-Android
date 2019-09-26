package com.twt.service.theory.view

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View


class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount + 1
        outRect.left = spacing - spacing / spanCount * column
        outRect.right = spacing / spanCount - spacing / spanCount * column
        if (position >= spanCount) {
            outRect.top = spacing
        }
    }
}