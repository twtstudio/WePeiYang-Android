package com.twtstudio.service.dishesreviews.home.view.viewholders

import android.arch.lifecycle.LifecycleOwner
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.base.BaseItemViewHolder
import com.twtstudio.service.dishesreviews.home.view.adapters.DinningHallsAdapter

/**
 * Created by zhangyulong on 18-3-23.
 */
class DinningHallsViewHolder(itemView: View, lifecycleOwner: LifecycleOwner) : BaseItemViewHolder(itemView, lifecycleOwner) {
    private val recyclerView = itemView.findViewById<RecyclerView>(R.id.rv_dinning_halls)
    private val dinningHallsAdapter = DinningHallsAdapter(emptyList(), itemView.context, lifecycleOwner)
    override fun bind() {
        recyclerView.apply() {
            layoutManager = GridLayoutManager(itemView.context, 4)
            adapter = dinningHallsAdapter
        }

    }
}