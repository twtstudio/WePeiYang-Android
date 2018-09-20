package com.yookiely.lostfond2.waterfall

import android.support.v4.app.FragmentManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import java.util.ArrayList

class WaterfallPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragmentsOfWaterfall = ArrayList<Fragment>()
    private val fragmentsTitles = ArrayList<String>()

    fun add(fragment: Fragment, title: String) {
        fragmentsOfWaterfall.add(fragment)
        fragmentsTitles.add(title)
    }

    override fun getItem(position: Int): Fragment = fragmentsOfWaterfall[position]

    override fun getCount(): Int = fragmentsOfWaterfall.size

    override fun getPageTitle(position: Int): CharSequence? = fragmentsTitles[position]
}

