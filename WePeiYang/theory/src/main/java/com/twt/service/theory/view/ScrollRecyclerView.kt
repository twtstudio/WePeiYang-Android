package com.twt.service.theory.view

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet


@RequiresApi(Build.VERSION_CODES.M)
class ScrollRecyclerView(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {
    private var index: Int = 0
    private var move: Boolean = false

    init {
        setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (move) {
                    move = false
                    val n = index - (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (0 <= n && n < recyclerView?.childCount!!) {
                        val top = recyclerView.getChildAt(n).top
                        recyclerView.smoothScrollBy(0, top)
                    }
                }
            }

        })
    }

    fun toPosition(pos: Int) {
        this.index = pos
        val firstItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        val lastItem = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        when {
            pos <= firstItem ->
                smoothScrollToPosition(pos)
            pos <= lastItem -> {
                val top = getChildAt(pos - firstItem).top
                smoothScrollBy(0, top)
            }
            else -> {
                smoothScrollToPosition(pos)
                move = true
            }
        }
    }

}