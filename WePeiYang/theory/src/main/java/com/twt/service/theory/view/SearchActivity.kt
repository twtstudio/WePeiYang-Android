package com.twt.service.theory.view

import android.graphics.Color
import android.os.Bundle
import android.support.design.R.attr.tabGravity
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.twt.service.theory.R
import com.twt.service.theory.R.drawable.theory_unselected_circle_shape
import kotlinx.android.synthetic.main.theory_common_toolbar.*
import kotlinx.android.synthetic.main.theory_common_toolbar.view.*
import kotlinx.android.synthetic.main.theory_search.*
import java.security.AccessController.getContext

class SearchActivity : AppCompatActivity() {
    private val state: Array<Boolean> = arrayOf(false, false, false, false)
    private val type: Array<Boolean> = arrayOf(false, false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.theory_search)
        search_toolbar.theory_back.setOnClickListener {
            finish()
        }
        search_toolbar.title.text = "搜索"
        search_toolbar.theory_search.visibility = View.GONE
        search_toolbar.theory_person_profile.visibility = View.GONE

        exam_necessary.setOnClickListener {
            if (type[0]) {
                exam_necessary.background = ResourcesCompat.getDrawable(resources, R.drawable.theory_unselected_circle_shape, null)
                exam_necessary.setTextColor(resources.getColor(R.color.grey))
            } else {
                exam_necessary.background = ResourcesCompat.getDrawable(resources, R.drawable.theory_type_a_circle_shape, null)
                exam_necessary.setTextColor(resources.getColor(R.color.blue))
            }
            type[0] = !type[0]
        }
        exam_optional.setOnClickListener {
            if (type[1]) {
                exam_optional.background = ResourcesCompat.getDrawable(resources, R.drawable.theory_unselected_circle_shape, null)
                exam_optional.setTextColor(resources.getColor(R.color.grey))
            } else {
                exam_optional.background = ResourcesCompat.getDrawable(resources, R.drawable.theory_type_b_circle_shape, null)
                exam_optional.setTextColor(resources.getColor(R.color.blue2))
            }
            type[1] = !type[1]
        }
        exam_running.setOnClickListener {
            if (state[0]) {
                exam_running.background = ResourcesCompat.getDrawable(resources, R.drawable.theory_unselected_circle_shape, null)
                exam_running.setTextColor(resources.getColor(R.color.grey))
            } else {
                exam_running.background = ResourcesCompat.getDrawable(resources, R.drawable.theory_state_a_circle_shape, null)
                exam_running.setTextColor(resources.getColor(R.color.green))
            }
            state[0] = !state[0]
        }
        exam_resit.setOnClickListener {
            if (state[1]) {
                exam_resit.background = ResourcesCompat.getDrawable(resources, R.drawable.theory_unselected_circle_shape, null)
                exam_resit.setTextColor(resources.getColor(R.color.grey))
            } else {
                exam_resit.background = ResourcesCompat.getDrawable(resources, R.drawable.theory_state_a_circle_shape, null)
                exam_resit.setTextColor(resources.getColor(R.color.green))
            }
            state[1] = !state[1]
        }
        exam_ending.setOnClickListener {
            if (state[2]) {
                exam_ending.background = ResourcesCompat.getDrawable(resources, R.drawable.theory_unselected_circle_shape, null)
                exam_ending.setTextColor(resources.getColor(R.color.grey))
            } else {
                exam_ending.background = ResourcesCompat.getDrawable(resources, R.drawable.theory_state_b_circle_shape, null)
                exam_ending.setTextColor(resources.getColor(R.color.yellow))
            }
            state[2] = !state[2]
        }
        exam_end.setOnClickListener {
            if (state[3]) {
                exam_end.background = ResourcesCompat.getDrawable(resources, R.drawable.theory_unselected_circle_shape, null)
                exam_end.setTextColor(resources.getColor(R.color.grey))
            } else {
                exam_end.background = ResourcesCompat.getDrawable(resources, R.drawable.theory_state_c_circle_shape, null)
                exam_end.setTextColor(resources.getColor(R.color.black))
            }
            state[3] = !state[3]
        }

        val adapter = TheoryPagerAdapter(supportFragmentManager)
        adapter.apply {
            add(ExamFragment(), "考试")
            add(MessageFragement(), "通知")
        }
        main_viewpager.adapter = adapter
        main_tablayout.apply {
            setupWithViewPager(main_viewpager)
            tabGravity = TabLayout.GRAVITY_FILL
            setSelectedTabIndicatorColor(Color.parseColor("#1E90FF"))
        }
    }
}