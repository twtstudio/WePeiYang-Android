package com.twtstudio.retrox.tjulibrary.view


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

import android.support.v4.app.FragmentPagerAdapter
import java.util.ArrayList

class BookPagerAdapter(fm : FragmentManager)  : FragmentPagerAdapter(fm) {

    var fragmentsOfMylist: MutableList<Fragment> = ArrayList()

    fun add(fragment: Fragment) {
        fragmentsOfMylist.add(fragment)
    }

    fun removeAll(){
        fragmentsOfMylist.clear()
    }

    override fun getItem(position: Int): Fragment  = fragmentsOfMylist[position]

    override fun getCount(): Int = fragmentsOfMylist.size
}