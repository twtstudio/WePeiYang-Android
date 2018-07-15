package com.twtstudio.service.tjwhm.exam.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twtstudio.service.tjwhm.exam.R

class ExamHomeFragment : Fragment() {

    companion object {
        fun newInstance(): ExamHomeFragment {
            val fragment = ExamHomeFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.exam_fragment_home, container, false)

        return view
    }
}