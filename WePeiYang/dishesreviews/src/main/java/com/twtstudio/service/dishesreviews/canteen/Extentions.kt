package com.twtstudio.service.dishesreviews.canteen

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by SGXM on 2018/5/6.
 */
val recmove = fun RecyclerView.(n: Int) {
    val layoutManager = this.layoutManager as LinearLayoutManager
    layoutManager.scrollToPositionWithOffset(n, 0)
}