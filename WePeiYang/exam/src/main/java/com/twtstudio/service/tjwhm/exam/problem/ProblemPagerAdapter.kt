package com.twtstudio.service.tjwhm.exam.problem

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class ProblemPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    private var fragmentList = mutableListOf<ProblemFragment>()

    fun add(fragmentIndex: Int, classID: Int, type: Int, mode: Int, problemID: Int) =
            fragmentList.add(ProblemFragment.newInstance(fragmentIndex, classID, type, mode, problemID))

    fun add(fragmentIndex: Int, oneProblemData: TestOneProblemData) =
            fragmentList.add(ProblemFragment.newInstance(fragmentIndex, oneProblemData))

    fun changeMode(index: Int) = fragmentList[index].changeMode()

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = fragmentList.size

}