package com.twt.service.home.common.gpa

import android.arch.lifecycle.LifecycleOwner
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.twt.service.AppPreferences
import com.twt.service.R
import com.twt.wepeiyang.commons.experimental.extensions.bind
import xyz.rickygao.gpa2.api.GpaProvider
import xyz.rickygao.gpa2.view.GpaActivity

/**
 * Created by rickygao on 2018/1/26.
 */

class GpaItemViewHolder(itemView: View, lifecycleOwner: LifecycleOwner) : RecyclerView.ViewHolder(itemView) {

    private val cardView: CardView = itemView.findViewById(R.id.card_item_gpa)
    private val scoreTv: TextView = itemView.findViewById(xyz.rickygao.gpa2.R.id.tv_score)
    private val gpaTv: TextView = itemView.findViewById(xyz.rickygao.gpa2.R.id.tv_gpa)
    private val creditTv: TextView = itemView.findViewById(xyz.rickygao.gpa2.R.id.tv_credit)

    init {

        cardView.setOnClickListener {
            val intent = Intent(itemView.context, GpaActivity::class.java)
            itemView.context.startActivity(intent)
        }

        GpaProvider.gpaLiveData
                .bind(lifecycleOwner) {
                    it?.stat?.total?.let {
                        val fake = "***"
                        if (AppPreferences.isDisplayGpa) {
                            if (AppPreferences.isBoomGpa) {
                                scoreTv.text = "100.0"
                                gpaTv.text = "4.0"
                            } else {
                                scoreTv.text = it.score.toString()
                                gpaTv.text = it.gpa.toString()
                            }
                            creditTv.text = it.credit.toString()
                        } else {
                            scoreTv.text = fake
                            gpaTv.text = fake
                            creditTv.text = fake
                        }
                    }
                }

        GpaProvider.updateGpaLiveData(quiet = true)

    }
}
