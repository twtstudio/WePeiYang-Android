package com.avarye.mall.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup

class MallPagerAdapter(fragmentManager: android.support.v4.app.FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private var fragments = mutableListOf<Fragment>()
    private var titles = mutableListOf<String>()
    private var current = Fragment()

    fun add(fragment: Fragment, title: String) {
        fragments.add(fragment)
        titles.add(title)
    }

    fun getCurrent() : Fragment = current

    override fun getCount(): Int = fragments.size

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getPageTitle(position: Int): CharSequence = titles[position]

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        current = `object` as Fragment
        super.setPrimaryItem(container, position, `object`)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) { }

}