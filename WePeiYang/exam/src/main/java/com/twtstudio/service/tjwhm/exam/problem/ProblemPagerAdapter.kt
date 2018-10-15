package com.twtstudio.service.tjwhm.exam.problem

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class ProblemPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    private var fragmentList = mutableListOf<ProblemFragment>()

    fun add(fragmentIndex: Int, type: Int, mode: Int, problemID: Int) =
            fragmentList.add(ProblemFragment.newInstance(fragmentIndex, type, mode, problemID))

    fun add(fragmentIndex: Int, testProblemBean: TestProblemBean) =
            fragmentList.add(ProblemFragment.newInstance(fragmentIndex, testProblemBean))

    fun changeMode(index: Int) = fragmentList[index].changeMode()

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = fragmentList.size

}
