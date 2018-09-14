package com.twtstudio.retrox.tjulibrary.view

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import java.util.ArrayList

class HomeLibraryPagerAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm){

    var fragmentsOfMylist: MutableList<Fragment> = ArrayList()
    var fragmentsTitles: MutableList<String> = ArrayList()

    fun add(fragment: Fragment,title  : String) {
        fragmentsOfMylist.add(fragment)
        fragmentsTitles.add(title)

    }

    override fun getItem(position: Int): Fragment = fragmentsOfMylist[position]


    override fun getCount(): Int = fragmentsOfMylist.size

    override fun getPageTitle(position: Int): CharSequence = fragmentsTitles[position]
}