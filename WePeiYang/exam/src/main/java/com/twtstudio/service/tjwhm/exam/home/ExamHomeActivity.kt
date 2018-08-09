package com.twtstudio.service.tjwhm.exam.home

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.widget.ImageView
import com.githang.statusbar.StatusBarCompat
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twtstudio.service.tjwhm.exam.R
import com.twtstudio.service.tjwhm.exam.user.UserFragment
import com.twtstudio.service.tjwhm.exam.user.examUserLiveData

class ExamHomeActivity : AppCompatActivity() {

    private lateinit var tlHome: TabLayout
    private lateinit var vpHome: ViewPager

    private val pagerAdapter = ExamHomePagerAdapter(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exam_activity_home)

        StatusBarCompat.setStatusBarColor(this@ExamHomeActivity, ContextCompat.getColor(this@ExamHomeActivity, R.color.examToolbarBlue), true)

        findViewById<ImageView>(R.id.iv_home_back).setOnClickListener { onBackPressed() }

        tlHome = findViewById(R.id.tl_home)
        vpHome = findViewById(R.id.vp_home)
        pagerAdapter.add(Pair(ExamHomeFragment.newInstance(), "题库"))
        pagerAdapter.add(Pair(UserFragment.newInstance(), "我的"))

        vpHome.adapter = pagerAdapter
        tlHome.setupWithViewPager(vpHome)
        tlHome.tabGravity = TabLayout.GRAVITY_FILL
        tlHome.setSelectedTabIndicatorHeight(0)

    }

    override fun onResume() {
        super.onResume()
        examUserLiveData.refresh(CacheIndicator.REMOTE)
    }
}
