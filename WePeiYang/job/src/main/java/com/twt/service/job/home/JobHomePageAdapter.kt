package com.twt.service.job.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class JobHomePageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragmentsOfHome = mutableListOf<Fragment>()
    private val fragmentTitles = mutableListOf<String>()

    override fun getCount(): Int = fragmentsOfHome.size

    override fun getItem(position: Int): Fragment = fragmentsOfHome[position]

    override fun getPageTitle(position: Int): CharSequence? = fragmentTitles[position]

    // 这样实例化 pageadapter 的时候不用传 fragment 和 title ，并且可以继续添加，如果以后需要删除，可以再添加一个删除方法
    fun addFragment(fragment: Fragment, title: String) {
        fragmentsOfHome.add(fragment)
        fragmentTitles.add(title)
        notifyDataSetChanged()
    }
}
