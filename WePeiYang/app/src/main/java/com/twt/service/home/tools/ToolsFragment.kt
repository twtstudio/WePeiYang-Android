package com.twt.service.home.tools

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bdpqchen.yellowpagesmodule.yellowpages.activity.HomeActivity

import com.twt.service.R
import com.twt.service.base.BaseFragment
import com.twtstudio.retrox.bike.bike.ui.main.BikeActivity
import com.twtstudio.retrox.bike.read.home.BookHomeActivity
import com.twtstudio.retrox.schedule.ScheduleActivity
import com.twtstudio.tjliqy.party.ui.home.PartyActivity
import xyz.rickygao.gpa2.view.GpaActivity

/**
 * Created by retrox on 2016/12/12.
 */

class ToolsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_tools, container, false)
        val recyclerview: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerview.layoutManager = GridLayoutManager(activity,4)
        val itemList = listOf(
                ToolItem(R.drawable.ic_main_schedule, "课程表", ScheduleActivity::class.java),
                ToolItem(R.drawable.ic_main_gpa, "全新 GPA", GpaActivity::class.java),
                ToolItem(R.drawable.ic_main_bike, "自行车", BikeActivity::class.java),
                ToolItem(R.drawable.ic_main_party, "党建", PartyActivity::class.java),
                ToolItem(R.drawable.ic_main_read, "阅读", BookHomeActivity::class.java),
                ToolItem(R.drawable.ic_main_fellow_search, "老乡查询", com.example.caokun.fellowsearch.view.MainActivity::class.java),
                ToolItem(R.drawable.ic_main_yellowpage, "黄页", HomeActivity::class.java)
        )
        recyclerview.adapter = ToolsAdapter(activity,itemList)
        return view

    }

    companion object {

        fun newInstance(): ToolsFragment {

            val args = Bundle()

            val fragment = ToolsFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
