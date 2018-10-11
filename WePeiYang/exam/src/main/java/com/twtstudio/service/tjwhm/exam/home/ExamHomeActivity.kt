package com.twtstudio.service.tjwhm.exam.home

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.Log
import android.widget.ImageView
import com.githang.statusbar.StatusBarCompat
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twtstudio.service.tjwhm.exam.R
import com.twtstudio.service.tjwhm.exam.user.UserFragment
import com.twtstudio.service.tjwhm.exam.user.examUserLiveData

class ExamHomeActivity : AppCompatActivity() {

    private lateinit var vpHome: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.exam_activity_home)

        StatusBarCompat.setStatusBarColor(this@ExamHomeActivity, ContextCompat.getColor(this@ExamHomeActivity, R.color.examToolbarBlue), true)

        findViewById<ImageView>(R.id.iv_home_back).setOnClickListener { onBackPressed() }

        vpHome = findViewById<ViewPager>(R.id.vp_home).apply {
            adapter = ExamHomePagerAdapter(supportFragmentManager).apply {
                add(Pair(ExamHomeFragment.newInstance(), "题库"))
                add(Pair(UserFragment.newInstance(), "我的"))
            }
        }

        findViewById<TabLayout>(R.id.tl_home).apply {
            setupWithViewPager(vpHome)
            tabGravity = TabLayout.GRAVITY_FILL
            setSelectedTabIndicatorHeight(0)
        }
    }

    override fun onResume() {
        super.onResume()
        examUserLiveData.refresh(CacheIndicator.REMOTE)
    }
}
