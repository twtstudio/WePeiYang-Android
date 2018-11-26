package com.twtstudio.service.tjwhm.exam.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ExamHomePagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    var list: MutableList<Pair<Fragment, String>> = mutableListOf()

    override fun getItem(position: Int): Fragment = list[position].first

    override fun getCount(): Int = list.size

    fun add(pair: Pair<Fragment, String>) = list.add(pair)

    override fun getPageTitle(position: Int): CharSequence? = list[position].second
}
