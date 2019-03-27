package com.twt.service.ecard.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.service.ecard.R
import com.twt.service.ecard.model.TransactionInfo
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import org.jetbrains.anko.layoutInflater

class EcardPreTotalItem(val consumption: List<TransactionInfo>, val recharge: List<TransactionInfo>) : Item {

    override val controller: ItemController
        get() = Controller

    override fun areContentsTheSame(newItem: Item): Boolean {
        return consumption == (newItem as? EcardPreTotalItem)?.consumption && recharge == (newItem as? EcardPreTotalItem)?.recharge
    }

    override fun areItemsTheSame(newItem: Item): Boolean {
        return consumption == (newItem as? EcardPreTotalItem)?.consumption && recharge == (newItem as? EcardPreTotalItem)?.recharge
    }

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.ecard_item_pretotal, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as EcardPreTotalItem

            val totalConsume = item.consumption.fold(0f) { prev: Float, transactionInfo: TransactionInfo ->
                prev + transactionInfo.amount.toFloat()
            }
            val totalRecharge = item.recharge.fold(0f) { prev: Float, transactionInfo: TransactionInfo ->
                prev + transactionInfo.amount.toFloat()
            }

            holder.apply {
                totalRechargeText.text = "充值：${String.format("%.2f", totalRecharge)}元"
                totalConsumeText.text = "消费：${String.format("%.2f", totalConsume)}元"
            }
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val totalConsumeText: TextView = itemView.findViewById(R.id.tv_chart_consume)
            val totalRechargeText: TextView = itemView.findViewById(R.id.tv_chart_recharge)
        }
    }
}

fun MutableList<Item>.ecardPreTotalItem(consumption: List<TransactionInfo>, recharge: List<TransactionInfo>) = add(EcardPreTotalItem(consumption, recharge))