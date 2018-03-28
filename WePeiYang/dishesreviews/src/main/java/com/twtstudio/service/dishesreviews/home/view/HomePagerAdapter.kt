package com.twtstudio.service.dishesreviews.home.view

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.base.BaseListAdapter
import com.twtstudio.service.dishesreviews.home.view.viewholders.ADViewHolder
import com.twtstudio.service.dishesreviews.home.view.viewholders.BannerViewHolder
import com.twtstudio.service.dishesreviews.home.view.viewholders.DinningHallViewHolder
import com.twtstudio.service.dishesreviews.home.view.viewholders.ReviewsViewHolder

/**
 * Created by zhangyulong on 18-3-22.
 */
class HomePagerAdapter(list: List<Any>, context: Context, owner: LifecycleOwner) : BaseListAdapter(list, context, owner) {
    companion object {
        const val BANNER = 1
        const val DINNING_HALL = 2
        const val AD = 3
        const val REVIEWS = 4
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        BANNER -> BannerViewHolder(inflater.inflate(R.layout.dishes_reviews_item_home_banner, parent, false), owner)
        DINNING_HALL -> DinningHallViewHolder(inflater.inflate(R.layout.dishes_reviews_item_home_dinning_hall,parent,false),owner)
        AD-> ADViewHolder(inflater.inflate(R.layout.dishes_reviews_item_home_ad,parent,false),owner)
        REVIEWS -> ReviewsViewHolder(inflater.inflate(R.layout.dishes_reviews_item_home_reviews,parent,false),owner)
        else -> null
    }!!


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {

    }

    override fun getItemViewType(position: Int): Int {
        return list[position] as Int
    }
}