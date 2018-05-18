package com.twtstudio.service.dishesreviews.canteen

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import com.twtstudio.service.dishesreviews.R
import kotlinx.android.synthetic.main.dishes_reviews_activity_canteen.*
import kotlinx.android.synthetic.main.dishes_reviews_toolbar.*

class CanteenActivity : AppCompatActivity() {

    var fragments: MutableList<Pair<CanteenFragment, String>> = mutableListOf()
    var fpAdapter: FragmentPagerAdapter = object : FragmentPagerAdapter(supportFragmentManager) {

        override fun getItem(position: Int): Fragment = fragments[position].first
        override fun getCount(): Int = fragments.size
        override fun getPageTitle(position: Int): CharSequence? {
            return fragments[position].second
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dishes_reviews_activity_canteen)
        tv_toolbar_title.text = intent.getStringExtra("CanteenName")
        fragments.add(CanteenFragment() to "一层")
        fragments.add(CanteenFragment() to "二层")
        vp_stairs.adapter = fpAdapter
        vp_stairs.offscreenPageLimit = 2
        tab_stairs.setupWithViewPager(vp_stairs)
        tab_stairs.setIndicator(135, 130)
//        for (i in 0 until tab_stairs.childCount) {
//            tab_stairs.getChildAt(i).isClickable = false
//        }

        //这里面有代码会removeAllTabs，必须放前面
//            //removeAllTabs()
//            addTab(tab_stairs.newTab().apply { text = "一层" })
//            addTab(tab_stairs.newTab().apply { text = "二层" })
//        }
    }
}
