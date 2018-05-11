package com.twtstudio.service.dishesreviews.home.view

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.base.LazyFragment
import com.twtstudio.service.dishesreviews.home.view.adapters.HomePagerAdapter


class HomeFragment : LazyFragment() {
    override fun getResId(): Int {
        return R.layout.dishes_reviews_fragment_home
    }

    override fun onRealViewLoaded(view: View) {
        view.findViewById<RecyclerView>(R.id.list).apply {
            layoutManager=LinearLayoutManager(context)
            adapter= HomePagerAdapter(listOf(HomePagerAdapter.BANNER,
                    HomePagerAdapter.DINNING_HALL,
                    HomePagerAdapter.AD,
                    HomePagerAdapter.REVIEWS
            ), context, this@HomeFragment)
        }
    }


}
