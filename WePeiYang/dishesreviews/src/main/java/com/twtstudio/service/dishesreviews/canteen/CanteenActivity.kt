package com.twtstudio.service.dishesreviews.canteen

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.canteen.model.CanteenDishInfoViewModel
import kotlinx.android.synthetic.main.dishes_reviews_activity_canteen.*
import kotlinx.android.synthetic.main.dishes_reviews_toolbar.*

class CanteenActivity : AppCompatActivity() {

    lateinit var canteenDishVM: CanteenDishInfoViewModel
    var canteenId = 15
    var fragments: MutableList<Pair<CanteenFragment, String>> = mutableListOf()
    var fpAdapter: FragmentPagerAdapter = object : FragmentPagerAdapter(supportFragmentManager) {

        override fun getItem(position: Int): Fragment = fragments[position].first
        override fun getCount(): Int = fragments.size
        override fun getPageTitle(position: Int): CharSequence? {
            return fragments[position].second
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dishes_reviews_activity_canteen)
        canteenDishVM = ViewModelProviders.of(this).get(CanteenDishInfoViewModel::class.java)


        canteenDishVM.getDishes(15).bindNonNull(this) {
            Log.d("woggle", "CanteenActivity")
            fragments.clear()
            if (it.firstFloor.foodList == null) return@bindNonNull
            fragments.add(CanteenFragment() to "一层")

            if (it.secondFloor.foodList != null) {
                fragments.add(CanteenFragment().apply { mTag = "二层" } to "二层")
                vp_stairs.offscreenPageLimit = 2
            }
            vp_stairs.adapter = fpAdapter.apply { notifyDataSetChanged() }
            tab_stairs.setupWithViewPager(vp_stairs)
            tab_stairs.setIndicator(135, 130)
        }

        tv_toolbar_title.text = intent.getStringExtra("CanteenName")


//        for (i in 0 until tab_stairs.childCount) {
//            tab_stairs.getChildAt(i).isClickable = false
//        }

        //这里面有代码会removeAllTabs，必须放前面
//            //removeAllTabs()
//            addTab(tab_stairs.newTab().apply { text = "一层" })
//            addTab(tab_stairs.newTab().apply { text = "二层" })
//        }
    }
}
