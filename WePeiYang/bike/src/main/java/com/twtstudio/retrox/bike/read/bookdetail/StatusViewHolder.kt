package com.twtstudio.retrox.bike.read.bookdetail

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.twtstudio.retrox.bike.R
import com.twtstudio.retrox.bike.extension.bind
import com.twtstudio.retrox.bike.model.read.Detail

/**
 * Created by zhangyulong on 18-1-26.
 */
class StatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvCallNo=itemView.findViewById<TextView>(R.id.tv_status_callno)
    private val tvLocal=itemView.findViewById<TextView>(R.id.tv_status_local)
    private val tvState=itemView.findViewById<TextView>(R.id.tv_status_state)
    private val statusData=MutableLiveData<Detail.HoldingBean.DataBean>()
    fun bind(owner: LifecycleOwner, status:Detail.HoldingBean.DataBean ) {
        statusData.value=status
        statusData.bind(owner){
            it?.apply {
                tvCallNo.text=it.callno
                tvLocal.text=it.local
                tvState.text=it.state
            }
        }
    }
}