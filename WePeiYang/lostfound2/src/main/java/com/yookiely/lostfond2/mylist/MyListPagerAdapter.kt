package com.yookiely.lostfond2.mylist

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import java.util.ArrayList

class MyListPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var fragmentsOfMyList: MutableList<Fragment> = ArrayList()
    private var fragmentsTitles: MutableList<String> = ArrayList()

    fun add(fragment: Fragment, title: String) {
        fragmentsOfMyList.add(fragment)
        fragmentsTitles.add(title)
    }

    override fun getItem(position: Int): Fragment = fragmentsOfMyList[position]

    override fun getCount(): Int = fragmentsOfMyList.size

    override fun getPageTitle(position: Int): CharSequence = fragmentsTitles[position]
}

