package com.twt.service.theory.view


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twt.service.theory.R
import com.twt.service.theory.model.TestBean
import com.twt.wepeiyang.commons.ui.rec.withItems

class ExamFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.theory_fragment_common, container, false)
        recyclerView = view.findViewById(R.id.main_rec)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        return view
    }

    fun setTestList(testList: List<TestBean.DataBean>?) {
        if (testList == null) return
        recyclerView.withItems {
            for (i: TestBean.DataBean in testList) {
                setExamItem(i, this@ExamFragment)
            }
        }
    }
}