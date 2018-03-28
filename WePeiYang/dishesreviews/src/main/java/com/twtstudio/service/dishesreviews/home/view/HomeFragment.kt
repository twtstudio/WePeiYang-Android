package com.twtstudio.service.dishesreviews.home.view

import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.base.LazyFragment


class HomeFragment : LazyFragment() {
    override fun getResId(): Int {
        return R.layout.dishes_reviews_fragment_home
    }

    override fun onRealViewLoaded(view: View) {
        view.findViewById<RecyclerView>(R.id.list).apply {
            layoutManager=LinearLayoutManager(context)
            adapter=HomePagerAdapter(listOf(HomePagerAdapter.BANNER,
                    HomePagerAdapter.DINNING_HALL,
                    HomePagerAdapter.AD,
                    HomePagerAdapter.REVIEWS),context,this@HomeFragment)
        }

    }


}
