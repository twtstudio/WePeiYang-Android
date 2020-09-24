package com.twt.service.announcement.ui.main

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log

class MyLinearLayoutManager(context: Context?) : LinearLayoutManager(context) {

    // 重写这个方法，防止RecyclerView遇到Inconsistency detected崩溃
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: Exception) {
            Log.d("bug", "crash in Recyclerview")
        }
    }

}