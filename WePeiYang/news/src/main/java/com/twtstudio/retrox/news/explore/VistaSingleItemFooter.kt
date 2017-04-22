package com.twtstudio.retrox.news.explore

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.twtstudio.retrox.news.R
import es.dmoral.toasty.Toasty

/**
 * Created by retrox on 09/04/2017.
 */
class VistaSingleItemFooter (val context: Context, val layoutHelper: LayoutHelper) : DelegateAdapter.Adapter<VistaSingleItemFooter.singleHolder>() {
    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): singleHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_explore_vista_footer,parent,false)
        return singleHolder(view)
    }

    override fun onBindViewHolder(holder: singleHolder?, position: Int) {
        holder?.button?.setOnClickListener { Toasty.info(context,"gg", Toast.LENGTH_SHORT).show() }
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return layoutHelper
    }

    override fun getItemViewType(position: Int): Int {
        return 3
    }


    class singleHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        val button = itemView?.findViewById(R.id.button) as Button
    }
}