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
class CommonFragmentNew : BaseFragment() {

    lateinit var recyclerview: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val hostActivity = activity as RxAppCompatActivity
        val view = inflater.inflate(R.layout.fragment_commons_new, container, false)
        recyclerview = view.findViewById(R.id.recyclerView)
        //todo 修改课程表暴露的接口 （重载）
        val scheduleViewModel = ScheduleViewModel(hostActivity)
//        val animController = AnimationUtils.loadLayoutAnimation(activity,R.anim.layout_animation_from_bottom)
        recyclerview.apply {
            layoutManager = LinearLayoutManager(this.context)
//            layoutAnimation = animController
            adapter = CommonPageAdapter(listOf(scheduleViewModel, "GPA", "LIB"),
                    hostActivity, this@CommonFragmentNew)
//            scheduleLayoutAnimation()
        }

        PushProvider(this.activity as RxAppCompatActivity).apply {
            queryCourseMessage { coursePushBean ->
                Alerter.create(hostActivity)
                        .setTitle(coursePushBean.title)
                        .setText(coursePushBean.message)
                        .setDuration((3 * 1000).toLong())
                        .setOnClickListener {
                            val intent = Intent(this@CommonFragmentNew.activity, ScheduleActivity::class.java)
                            startActivity(intent)
                        }
                        .show()
            }
        }

        return view
    }


}