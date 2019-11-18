package com.twt.service.theory.view

import android.graphics.Rect
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.PopupWindow
import com.orhanobut.hawk.Hawk
import com.tapadoo.alerter.Alert
import com.twt.service.theory.model.PaperBean
import com.twt.wepeiyang.commons.mta.mtaBegin
import com.twt.wepeiyang.commons.ui.rec.withItems
import kotlinx.android.synthetic.main.theory_popupwindow_layout.view.*


object AnswerManager {
    //注意：下标从1开始！！
    private var ans: MutableList<Int> = mutableListOf()
    private var numOfDone = 0
    private var popupWindow: PopupWindow? = null
    private var list: MutableList<ProblemItem> = mutableListOf()
    private var questionList: MutableList<PaperBean.BodyBean> = mutableListOf()
    private var numOfQue: Int = 0
    private var testId: Int = 0
    private var bTime: Long = 0//考试开始的时间戳(s)
    private var dur: Int = 0//考试时间

    /** @param number 题目数量
     *  @param qList 题目信息列表
     *  @param id 试卷id
     *  @param beginTime 考试开始时间戳(s)
     *  @param duration 持续时间(s)
     *  默认所有题目无选择
     *  这个操作会完全重置AnswerManager
     */
    fun init(number: Int, qList: MutableList<PaperBean.BodyBean>, id: Int, beginTime: Long, duration: Int) {
        testId = id
        numOfQue = number
        ans.clear()
        questionList.clear()
        this.bTime = beginTime
        dur = duration
        questionList = qList
        for (i in 0..numOfQue) ans.add(0)// 0表示还没选
        save()
    }

    fun setBeginTIme(beginTime: Long) {
        bTime = beginTime
    }

    fun setDuration(duration: Int) {
        dur = duration
    }

    private fun save() {//这个函数将会向Hawk中写入AnswerManager的实体
        Hawk.put("theory_answer_manager", this)
    }

    fun getBeginTime(): Long {
        return bTime
    }

    fun getDuration(): Int {
        return dur
    }

    fun getTestId(): Int {
        return testId
    }

    fun getQustionList(): List<PaperBean.BodyBean> {
        return questionList.toList()
    }

    fun recover() {//从Hawk中恢复AnswerManager
        val s = Hawk.get<AnswerManager>("theory_answer_manager", null) ?: return
        ans = s.ans
        numOfDone = s.numOfDone
        list = s.list
        questionList = s.questionList
        numOfQue = s.numOfQue
        testId = s.testId
        bTime = s.bTime
        dur = s.dur
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

    fun update(num: Int, answer: Int) { // 更新一个答案：num题目编号、ans题目答案，你需要对答案自行编码
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
        save()//实时保存
    }

    fun isPopUPWindowInstalled(): Boolean {//这里用于与题卡交互，下面的题卡相关函数不要动它就好
        return popupWindow != null
    }

    fun getPopUpWindow(): PopupWindow? {//同上
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

    fun uninstall() {//卸载题卡弹窗
        popupWindow = null
    }
}