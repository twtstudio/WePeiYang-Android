package com.twtstudio.service.dishesreviews

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.twtstudio.service.dishesreviews.account.view.AccountFragment
import com.twtstudio.service.dishesreviews.home.view.HomeFragment

/**
 * Created by zhangyulong on 18-3-16.
 */
class MainPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0-> return HomeFragment()
            2-> return AccountFragment()
            else -> return HomeFragment()
        }
    }
}