package com.twtstudio.service.dishesreviews.home.view.viewholders

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.base.BaseItemViewHolder
import com.twtstudio.service.dishesreviews.home.model.HomeDataViewModel
import com.twtstudio.service.dishesreviews.home.view.adapters.ReviewsAdapter
import com.twtstudio.service.dishesreviews.model.GoodComment

/**
 * Created by zhangyulong on 18-3-23.
 */
class ReviewsViewHolder(itemView: View, lifecycleOwner: LifecycleOwner) : BaseItemViewHolder(itemView, lifecycleOwner) {
    private val rvReviews = itemView.findViewById<RecyclerView>(R.id.rv_reviews)
    private val homeDataViewModel = ViewModelProviders.of(lifecycleOwner as Fragment).get(HomeDataViewModel::class.java)
    var reviewsList: MutableList<GoodComment> = mutableListOf()
        set(value) {
            adapter.notifyDataSetChanged()
        }
    private val adapter = ReviewsAdapter(reviewsList, itemView.context, lifecycleOwner)

    override fun bind() {
        rvReviews.apply {
            layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
            adapter = this@ReviewsViewHolder.adapter
        }
        homeDataViewModel.homeBeanLiveData.bindNonNull(lifecycleOwner) {
            reviewsList.clear()
            reviewsList.addAll(it.goodComment)
        }
    }

    //test
    private fun updateData() {
        for (i in 1..10)
            reviewsList.add(GoodComment(i, commenter_name = "user+${i}"))
    }
}