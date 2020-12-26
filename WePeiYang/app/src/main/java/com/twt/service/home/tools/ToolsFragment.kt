package com.twt.service.home.tools

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.twt.service.R
import com.twt.service.schedule2.view.exam.ExamTableActivity
import com.yookiely.lostfond2.waterfall.WaterfallActivity
import xyz.rickygao.gpa2.view.GpaActivity

/**
 * Created by retrox on 2016/12/12.
 */

class ToolsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_tools, container, false).apply {
                findViewById<ImageView>(R.id.iv_fragment_tools_back).setOnClickListener { activity?.onBackPressed() }
                findViewById<RecyclerView>(R.id.recyclerView).apply {
                    layoutManager = GridLayoutManager(context, 4)
                    adapter = ToolsAdapter(context, listOf(
                            ToolItem(R.drawable.ic_main_schedule, "课程表", com.twt.service.schedule2.view.schedule.ScheduleActivity::class.java),
                            ToolItem(R.drawable.ic_main_gpa, "GPA", GpaActivity::class.java),
                            ToolItem(R.drawable.ic_main_lostfound, "失物招领", WaterfallActivity::class.java)
//                            ToolItem(R.drawable.ic_main_read, "考表", ExamTableActivity::class.java)
//                            ToolItem(R.drawable.ic_main_party, "党建", PartyActivity::class.java)
//                            ToolItem(R.drawable.ic_main_network, "上网", TjuNetActivity::class.java)
                    ))
                }
            }

}
