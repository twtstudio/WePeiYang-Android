package com.twt.service.theory.view


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twt.service.theory.R
import com.twt.wepeiyang.commons.ui.rec.withItems

class ExamFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.theory_fragment_common, container, false)
        val recyclerView  = view.findViewById<RecyclerView>(R.id.main_rec)
        recyclerView.withItems {
            setExamItem()
            setExamItem()
            setExamItem()
            setExamItem()
        }
        return view
    }
}