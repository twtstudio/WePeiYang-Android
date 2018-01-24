package com.twt.service.home.common

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.tapadoo.alerter.Alerter
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import com.twt.service.R
import com.twt.service.base.BaseFragment
import com.twt.service.home.common.gpaItem.GpaItemViewModel
import com.twt.service.home.common.schedule.ScheduleViewModel
import com.twt.service.push.PushProvider
import com.twtstudio.retrox.schedule.ScheduleActivity

/**
 * Created by retrox on 22/10/2017.
 */
class CommonFragmentNew : BaseFragment() {

    lateinit var recyclerview: RecyclerView
    lateinit var tadapter: CommonPageAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_commons_new, container, false)
        recyclerview = view.findViewById(R.id.recyclerView)
        val viewmodel = ViewModelProviders.of(this).get(GpaItemViewModel::class.java)
        val scheduleViewModel = ScheduleViewModel(this.activity as RxAppCompatActivity?)
        //todo 修改课程表暴露的接口 （重载）
        val list = listOf<Any>(scheduleViewModel,viewmodel,"LIB")
        tadapter = CommonPageAdapter(list,this.activity,this)
//        val animController = AnimationUtils.loadLayoutAnimation(activity,R.anim.layout_animation_from_bottom)
        recyclerview.apply {
            layoutManager = LinearLayoutManager(this.context)
//            layoutAnimation = animController
            this.adapter = tadapter
//            scheduleLayoutAnimation()
        }

        val pushProvider = PushProvider(this.activity as RxAppCompatActivity)
        pushProvider.queryCourseMessage { coursePushBean ->
            Alerter.create(this.activity)
                    .setTitle(coursePushBean.title)
                    .setText(coursePushBean.message)
                    .setDuration((3 * 1000).toLong())
                    .setOnClickListener {
                        val intent = Intent(this@CommonFragmentNew.getActivity(), ScheduleActivity::class.java)
                        startActivity(intent)
                    }
                    .show()
        }

        return view
    }


}