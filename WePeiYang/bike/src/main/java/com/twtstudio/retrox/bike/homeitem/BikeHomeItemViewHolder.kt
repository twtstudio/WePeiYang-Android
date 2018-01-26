package com.twtstudio.retrox.bike.homeitem

import android.arch.lifecycle.LifecycleOwner
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.twtstudio.retrox.bike.R
import com.twtstudio.retrox.bike.extension.bind
import org.w3c.dom.Text

/**
 * Created by zhangyulong on 18-1-25.
 */
class BikeHomeItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val progressBar = itemView.findViewById<ProgressBar>(R.id.pb_back_card)
    val tvUserBalance = itemView.findViewById<TextView>(R.id.bike_user_balance)
    val tvLeaveStation = itemView.findViewById<TextView>(R.id.leave_station)
    val tvLeaveTime = itemView.findViewById<TextView>(R.id.leave_time)
    val tvArrStation= itemView.findViewById<TextView>(R.id.arr_station)
    val tvArrTime= itemView.findViewById<TextView>(R.id.arr_time)
    val tvBikeFee=itemView.findViewById<TextView>(R.id.bike_fee)
    val btRefresh=itemView.findViewById<Button>(R.id.bt_bike_card_refresh)
    val btCall=itemView.findViewById<Button>(R.id.bt_call_bike_center)
    fun bind(owner: LifecycleOwner, viewModel: BikeHomeItemViewModel) {
        viewModel.isProgressing.bind(owner) {
            if (it!!) progressBar.visibility = View.VISIBLE
            else progressBar.visibility = View.INVISIBLE
        }
        viewModel.moneyLeft.bind(owner){
            tvUserBalance.text=it
        }
        viewModel.lastLeavePostion.bind(owner){
            tvLeaveStation.text=it
        }
        viewModel.lastLeaveTime.bind(owner){
            tvLeaveTime.text=it
        }
        viewModel.lastArrivePostion.bind(owner){
            tvArrStation.text=it
        }
        viewModel.lastArriveTime.bind(owner){
            tvArrTime.text=it
        }
        viewModel.costMoney.bind(owner){
            tvBikeFee.text=it
        }
        btRefresh.setOnClickListener {
            viewModel.refreshClick
        }
        btCall.setOnClickListener {
            viewModel.callBike()
        }
    }
}