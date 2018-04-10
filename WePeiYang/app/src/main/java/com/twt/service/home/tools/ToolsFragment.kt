package com.twt.service.home.tools

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twt.service.R
import com.twt.service.base.BaseFragment
import com.twt.service.tjunet.view.TjuNetActivity
import com.twtstudio.retrox.bike.bike.ui.main.BikeActivity
import com.twtstudio.retrox.schedule.ScheduleActivity
import com.twtstudio.tjliqy.party.ui.home.PartyActivity
import xyz.rickygao.gpa2.view.GpaActivity

/**
 * Created by retrox on 2016/12/12.
 */

class ToolsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_tools, container, false).apply {
                findViewById<RecyclerView>(R.id.recyclerView).apply {
                    layoutManager = GridLayoutManager(context, 4)
                    adapter = ToolsAdapter(context, listOf(
                            ToolItem(R.drawable.ic_main_schedule, "课程表", ScheduleActivity::class.java),
                            ToolItem(R.drawable.ic_main_gpa, "全新 GPA", GpaActivity::class.java),
                            ToolItem(R.drawable.ic_main_bike, "自行车", BikeActivity::class.java),
                            ToolItem(R.drawable.ic_main_party, "党建", PartyActivity::class.java),
                            ToolItem(R.drawable.ic_main_read, "课程表2(Not work)", com.twt.service.schedule2.view.schedule.ScheduleActivity::class.java),
                            ToolItem(R.drawable.ic_main_network, "上网", TjuNetActivity::class.java)
//                ToolItem(R.drawable.ic_main_fellow_search, "老乡查询", com.example.caokun.fellowsearch.view.MainActivity::class.java),
//                ToolItem(R.drawable.ic_main_yellowpage, "黄页", HomeActivity::class.java)
                    ))
                }
            }

}
