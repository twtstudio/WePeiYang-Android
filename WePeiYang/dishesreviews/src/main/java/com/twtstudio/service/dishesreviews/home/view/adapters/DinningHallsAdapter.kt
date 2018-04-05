package com.twtstudio.service.dishesreviews.home.view.adapters

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.base.BaseListAdapter
import com.twtstudio.service.dishesreviews.home.view.viewholders.DinningHallViewHolder

class DinningHallAdapter(list: List<Any>, context: Context, owner: LifecycleOwner) : BaseListAdapter(list, context, owner) {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return DinningHallViewHolder(inflater.inflate(R.layout.dishes_reviews_item_home_dinning_hall,parent,false),owner)
    }

    override fun getItemCount(): Int {
        return 8
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if(holder is DinningHallViewHolder)
            holder.bind(position)
    }
}