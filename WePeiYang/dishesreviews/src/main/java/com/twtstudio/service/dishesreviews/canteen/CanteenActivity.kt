package com.twtstudio.service.dishesreviews.canteen

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import com.twtstudio.service.dishesreviews.R
import kotlinx.android.synthetic.main.dishes_reviews_activity_canteen.*

class CanteenActivity : AppCompatActivity() {

    var fragments: MutableList<CanteenFragment> = mutableListOf()
    var fpAdapter: FragmentPagerAdapter = object : FragmentPagerAdapter(supportFragmentManager) {
        override fun getItem(position: Int): Fragment = fragments[position]
        override fun getCount(): Int = fragments.size
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dishes_reviews_activity_canteen)
        fragments.add(CanteenFragment())
        vp_stairs.adapter = fpAdapter
    }
}
