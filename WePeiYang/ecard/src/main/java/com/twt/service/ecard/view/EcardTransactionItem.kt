package com.twt.service.ecard.view

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
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
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.dip
import org.jetbrains.anko.horizontalPadding
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.startActivity
import java.util.Collections.addAll

class EcardTransactionItem(val transactionInfo: TransactionInfo, val isCost: Boolean, val isShowLine: Boolean) : Item {
    override val controller: ItemController
        get() = Controller


    override fun areContentsTheSame(newItem: Item): Boolean {
        return transactionInfo == (newItem as? EcardTransactionItem)?.transactionInfo && isCost == (newItem as? EcardTransactionItem)?.isCost && isShowLine == (newItem as? EcardTransactionItem)?.isShowLine
    }

    override fun areItemsTheSame(newItem: Item): Boolean {
        return transactionInfo == (newItem as? EcardTransactionItem)?.transactionInfo && isCost == (newItem as? EcardTransactionItem)?.isCost && isShowLine == (newItem as? EcardTransactionItem)?.isShowLine
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
            val isShowLine = item.isShowLine
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

                line.visibility = if (isShowLine) View.VISIBLE else View.GONE
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
            val line: View = itemView.findViewById(R.id.v_transaction_line)
        }

    }
}

fun MutableList<Item>.transactionItem(transactionInfo: TransactionInfo, isCost: Boolean, isShowLine: Boolean = true) = add(EcardTransactionItem(transactionInfo, isCost, isShowLine))

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
                                val list = transactionList.take(4)
                                list.forEachWithIndex { i, transactionInfo ->
                                    Log.d("mom", i.toString())
                                    transactionItem(transactionInfo, transactionListWrapper.consumption.contains(transactionInfo), (i < list.size - 1))
                                }
                                if (transactionList.isEmpty()) {
                                    lightText("暂未获取到最近两天消费数据")
                                }
                            } else {
                                holder.homeItem.apply {
                                    itemName.text = "TRANSACTION TODAY"
                                }
                                val list = transactionList.today().take(4)
                                list.forEachWithIndex { i, transactionInfo ->
                                    transactionItem(transactionInfo, transactionListWrapper.consumption.contains(transactionInfo), (i < list.size - 1))
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