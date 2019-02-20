package com.twt.service.ecard.view

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.twt.service.ecard.R
import com.twt.service.ecard.model.*
import com.twt.service.ecard.window.ECardTransactionPop
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.mta.mtaClick
import com.twt.wepeiyang.commons.ui.rec.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.horizontalPadding
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.startActivity
import java.util.Collections.addAll

class EcardTransactionItem(val transactionInfo: TransactionInfo, val isCost: Boolean) : Item {
    override val controller: ItemController
        get() = Controller


    override fun areContentsTheSame(newItem: Item): Boolean {
        return transactionInfo == (newItem as? EcardTransactionItem)?.transactionInfo && isCost == (newItem as? EcardTransactionItem)?.isCost
    }

    override fun areItemsTheSame(newItem: Item): Boolean {
        return transactionInfo == (newItem as? EcardTransactionItem)?.transactionInfo && isCost == (newItem as? EcardTransactionItem)?.isCost
    }

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.ecard_item_transaction, parent, false)
            return ViewHolder(view, view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as EcardTransactionItem
            val transactionInfo = item.transactionInfo
            val isCost = item.isCost
            val transactionDate = "${transactionInfo.date.substring(4, 6)}-${transactionInfo.date.substring(6, 8)}"
            val transactionTime = "${transactionInfo.time.substring(0, 2)}:${transactionInfo.time.substring(2, 4)}"

            holder.apply {
                title.text = transactionInfo.location
                detail.text = if (!isCost) {
                    "POS机氪金"
                } else {
                    "POS机消费"
                } + " | " + transactionDate + " " + transactionTime
                cost.text = if (!isCost) {
                    "+"
                } else {
                    "-"
                } + transactionInfo.amount

                if (title.text.contains("超市"))
                    pic.setImageResource(R.drawable.ecard_shoppping)
                else
                    pic.setImageResource(R.drawable.ecard_dining_hall)
            }

            holder.rootView.setOnClickListener {
                val pop = ECardTransactionPop(it.context, transactionInfo, isCost)
                pop.show()
                mtaClick("ecard_点击查看校园卡消费_顶部PopWindow")
            }
        }

        class ViewHolder(itemView: View, val rootView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.tv_transaction_title)
            val detail: TextView = itemView.findViewById(R.id.tv_transaction_detail)
            val cost: TextView = itemView.findViewById(R.id.tv_transaction_cost)
            val pic: ImageView = itemView.findViewById(R.id.iv_transaction_pic)
        }

    }
}

fun MutableList<Item>.transactionItem(transactionInfo: TransactionInfo, isCost: Boolean) = add(EcardTransactionItem(transactionInfo, isCost))

class EcardTransactionInfoItem : Item {
    override val controller: ItemController
        get() = Controller

    override fun areItemsTheSame(newItem: Item): Boolean = true
    override fun areContentsTheSame(newItem: Item): Boolean = true

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = RecyclerView(parent.context).apply {
                horizontalPadding = dip(16)
                layoutManager = LinearLayoutManager(parent.context)
            }
            val homeItem = HomeItem(parent)
            homeItem.apply {
                itemName.text = "TRANSACTION TODAY"
            }
            homeItem.setContentView(view)
            return ViewHolder(homeItem.rootView, view, homeItem)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            holder.homeItem.apply {
                itemContent.text = "消费流水"
                listOf(imgGo, itemContent).forEach {
                    it.setOnClickListener { view ->
                        view.context.startActivity<EcardMainActivity>()
                        mtaClick("ecard_用户查看消费流水详情")
                    }
                }
            }
            val itemManager = holder.recyclerView.withItems(mutableListOf())
            itemManager.refreshAll {
                lightText("暂未获取到消费数据")
            }

            LiveEcardManager.getEcardLiveData().observeForever { refreshState ->
                when (refreshState) {
                    is RefreshState.Success -> refreshState.message.apply {
                        val transactionListWrapper = this.transactionListWrapper
                        val transactionList = mutableListOf<TransactionInfo>()
                        transactionList.apply {
                            addAll(transactionListWrapper.recharge)
                            addAll(transactionListWrapper.consumption)
                            sortWith(compareBy({ it.date }, { it.time }))
                            reverse()
                        }

                        itemManager.refreshAll {
                            if (transactionList.today().isEmpty()) {
                                holder.homeItem.apply {
                                    itemName.text = "TRANSACTION PREVIOUS"
                                }
                                transactionList.take(4).forEach {
                                    transactionItem(it, transactionListWrapper.consumption.contains(it))
                                }
                                if (transactionList.isEmpty()) {
                                    lightText("暂未获取到最近两天消费数据")
                                }
                            } else {
                                holder.homeItem.apply {
                                    itemName.text = "TRANSACTION TODAY"
                                }
                                transactionList.today().forEach {
                                    transactionItem(it, transactionListWrapper.consumption.contains(it))
                                }
                            }
                        }
                    }
                }
            }


        }

        class ViewHolder(itemView: View, val recyclerView: RecyclerView, val homeItem: HomeItem) : RecyclerView.ViewHolder(itemView)

    }
}

fun MutableList<Item>.ecardTransactionInfoItem() = add(EcardTransactionInfoItem())