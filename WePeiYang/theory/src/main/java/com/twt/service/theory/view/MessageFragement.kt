package com.twt.service.theory.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twt.service.theory.R
import com.twt.service.theory.model.NoticeBean
import com.twt.service.theory.model.TestBean
import com.twt.wepeiyang.commons.ui.rec.withItems

class MessageFragement : Fragment() {
    lateinit var recyclerView: RecyclerView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.theory_fragment_common, container, false)
        recyclerView = view.findViewById<RecyclerView>(R.id.main_rec)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        return view
    }

    fun setNoticeList(noticeList: List<NoticeBean.DataBean>?) {
        if (noticeList == null) return
        recyclerView.withItems {
            for (i: NoticeBean.DataBean in noticeList) {
                setMessage(i, this@MessageFragement)
            }
        }

    }
}