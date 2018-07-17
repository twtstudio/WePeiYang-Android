package com.twtstudio.service.tjwhm.exam.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.twtstudio.service.tjwhm.exam.R
import com.twtstudio.service.tjwhm.exam.list.ListActivity
import org.jetbrains.anko.startActivity

class ExamHomeFragment : Fragment(), View.OnClickListener {

    lateinit var ivParty: ImageView

    companion object {
        fun newInstance(): ExamHomeFragment {
            val fragment = ExamHomeFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.exam_fragment_home, container, false)
        ivParty = view.findViewById(R.id.iv_party)
        ivParty.setOnClickListener { context?.startActivity<ListActivity>() }
        return view
    }

    override fun onClick(v: View?) {

    }
}