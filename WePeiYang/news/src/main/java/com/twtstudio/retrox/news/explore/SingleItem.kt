package com.twtstudio.retrox.news.explore

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.twtstudio.retrox.news.R

/**
 * Created by retrox on 09/04/2017.
 * 传入layoutres
 */
class SingleItem(val context: Context, val layoutHelper: LayoutHelper, @LayoutRes val layoutId: Int = R.layout.item_explore_vista_header ) : DelegateAdapter.Adapter<SingleItem.singleHolder>() {
    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): singleHolder {
        val view = LayoutInflater.from(context).inflate(layoutId,parent,false)
        return singleHolder(view)
    }

    override fun onBindViewHolder(holder: singleHolder?, position: Int) {

    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return layoutHelper
    }

    override fun getItemViewType(position: Int): Int {
        return 2
    }

    class singleHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}