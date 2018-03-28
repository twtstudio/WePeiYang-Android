package com.twtstudio.service.dishesreviews.base

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by zhangyulong on 18-3-23.
 */
abstract class BaseItemViewHolder(itemView: View, val lifecycleOwner: LifecycleOwner) : RecyclerView.ViewHolder(itemView) {
    open fun bind(){

    }
    open fun bind(viewModel: ViewModel) {

    }
}