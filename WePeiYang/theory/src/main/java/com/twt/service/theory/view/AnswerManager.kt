package com.twt.service.theory.view

import android.graphics.Rect
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.PopupWindow
import com.twt.wepeiyang.commons.ui.rec.withItems
import kotlinx.android.synthetic.main.theory_popupwindow_layout.view.*


object AnswerManager {
    //注意：下标从1开始！！
    private var ans: MutableList<Int> = mutableListOf()
    private var numOfDone = 0
    private var popupWindow: PopupWindow? = null
    private var list: MutableList<ProblemItem> = mutableListOf()
    private var numOfQue: Int = 0

    fun init(number: Int) {
        numOfQue = number
        ans.clear()
        for (i in 0..numOfQue) ans.add(0)// 0表示还没选

    }

    fun getAnswer(num: Int): Int { // 获取一个答案
        return ans[num]
    }

    fun isDone(num: Int): Boolean { // 查询一个题有没有做，已做返回true
        return ans[num] != 0
    }

    fun getNumberHasDone(): Int { // 获取已经做了的题目数量
        return numOfDone
    }

    fun getTotalNumber(): Int { // 获取总的题目数量
        return numOfQue
    }

    fun update(num: Int, answer: Int) { // 更新一个答案：num题目编号、ans题目答案。
        if (ans[num] == 0 && answer != 0) {
            ++numOfDone
            if (popupWindow != null) {
                list[num - 1].done = true
                popupWindow?.contentView?.theory_recyclerView?.adapter?.notifyItemChanged(num - 1)
            }
        } else if (ans[num] != 0 && answer == 0) {
            --numOfDone
            if (popupWindow != null) {
                list[num - 1].done = false
                popupWindow?.contentView?.theory_recyclerView?.adapter?.notifyItemChanged(num - 1)
            }
        }
        ans[num] = answer
    }

    fun isPopUPWindowInstalled(): Boolean {
        return popupWindow != null
    }

    fun getPopUpWindow(): PopupWindow? {
        return popupWindow
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun installPopUpWindow(src: PopupWindow, recyclerView: ScrollRecyclerView) { // 安装一个题卡弹窗
        popupWindow = src
        list.clear()
        for (i in 1..(numOfQue)) {
            val item = ProblemItem(i, ans[i] != 0) {
                recyclerView.toPosition(it - 1)
            }
            list.add(item)
        }
        popupWindow?.contentView?.theory_recyclerView?.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect?.top = 8
                outRect?.bottom = 0
                outRect?.left = 10
                outRect?.right = 0
            }

        })
        popupWindow?.contentView?.theory_recyclerView?.addItemDecoration(GridSpacingItemDecoration(6, 15))
        popupWindow?.contentView?.theory_recyclerView?.withItems(list)

    }

    fun uninstall() {
        popupWindow = null
    }
}