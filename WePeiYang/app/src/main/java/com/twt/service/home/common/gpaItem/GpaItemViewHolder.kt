package com.twt.service.home.common.gpaItem

import android.arch.lifecycle.LifecycleOwner
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.twt.service.R
import xyz.rickygao.gpa2.api.GpaBean
import xyz.rickygao.gpa2.api.GpaProvider
import xyz.rickygao.gpa2.api.Stat
import xyz.rickygao.gpa2.ext.bind
import xyz.rickygao.gpa2.ext.map

/**
 * Created by rickygao on 26/1/2018.
 */

class GpaItemViewHolder(itemView: View, val lifecycleOwner: LifecycleOwner) : RecyclerView.ViewHolder(itemView) {

    val cardView: CardView = itemView.findViewById(R.id.card_item_gpa)
    private val scoreTv: TextView = itemView.findViewById(xyz.rickygao.gpa2.R.id.tv_score)
    private val gpaTv: TextView = itemView.findViewById(xyz.rickygao.gpa2.R.id.tv_gpa)
    private val creditTv: TextView = itemView.findViewById(xyz.rickygao.gpa2.R.id.tv_credit)


    fun update() {
        GpaProvider.gpaLiveData
                .map(GpaBean::stat)
                .map(Stat::total)
                .bind(lifecycleOwner) {
                    it?.let {
                        scoreTv.text = it.score.toString()
                        gpaTv.text = it.gpa.toString()
                        creditTv.text = it.credit.toString()
                    }
                }
        GpaProvider.updateGpaLiveData(silent = true)
    }
}
