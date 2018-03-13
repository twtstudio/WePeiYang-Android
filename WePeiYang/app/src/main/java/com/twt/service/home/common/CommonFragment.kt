package com.twt.service.home.common

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tapadoo.alerter.Alerter
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import com.twt.service.R
import com.twt.service.base.BaseFragment
import com.twt.service.home.common.schedule.ScheduleViewModel
import com.twt.service.push.PushProvider
import com.twtstudio.retrox.schedule.ScheduleActivity

/**
 * Created by retrox on 22/10/2017.
 */
class CommonFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_commons_new, container, false).apply {

                val rxActivity = activity as RxAppCompatActivity

                findViewById<RecyclerView>(R.id.recyclerView).apply {
                    layoutManager = LinearLayoutManager(this.context)
                    adapter = CommonPageAdapter(
                            listOf(ScheduleViewModel(rxActivity), "GPA", "LIB"),
                            inflater,
                            this@CommonFragment
                    )
                }

                PushProvider(activity as RxAppCompatActivity).queryCourseMessage {
                    Alerter.create(rxActivity)
                            .setTitle(it.title)
                            .setText(it.message)
                            .setDuration((3 * 1000).toLong())
                            .setOnClickListener {
                                val intent = Intent(this@CommonFragment.activity, ScheduleActivity::class.java)
                                startActivity(intent)
                            }
                            .show()
                }
            }

}