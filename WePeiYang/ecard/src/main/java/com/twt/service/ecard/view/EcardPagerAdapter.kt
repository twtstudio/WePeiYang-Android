package com.twt.service.ecard.view

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup

class EcardPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments = mutableListOf<Fragment>()
    private var mCurrentfragment: Fragment? = null

    fun add(fragment: Fragment) = fragments.add(fragment)

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        mCurrentfragment = `object` as Fragment
        super.setPrimaryItem(container, position, `object`)
    }

    fun getCurrentFragment(): Fragment? = mCurrentfragment
}