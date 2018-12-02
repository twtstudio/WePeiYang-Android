package com.twt.service.ecard.view

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
import com.twt.wepeiyang.commons.ui.rec.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.horizontalPadding
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.startActivity

class EcardTransactionItem(val transactionInfo: TransactionInfo) : Item {
    override val controller: ItemController
        get() = Controller


    override fun areContentsTheSame(newItem: Item): Boolean {
        return transactionInfo == (newItem as? EcardTransactionItem)?.transactionInfo
    }

    override fun areItemsTheSame(newItem: Item): Boolean {
        return transactionInfo == (newItem as? EcardTransactionItem)?.transactionInfo
    }

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.ecard_item_transaction, parent, false)
            return ViewHolder(view, view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as EcardTransactionItem
            val transactionInfo = item.transactionInfo
            val transactionTime = "${transactionInfo.time.substring(0, 2)}:${transactionInfo.time.substring(2, 4)}"
            holder.apply {
                title.text = transactionInfo.location
                detail.text = "POS消费: ${transactionInfo.amount}  $transactionTime"
                if (transactionInfo.isCost == false) {
                    // 充钱
                    detail.text = "POS氪金: ${transactionInfo.amount}  $transactionTime"
                    icon.setImageResource(R.drawable.ecard_ic_banlance_up)
                }
            }
            holder.rootView.setOnClickListener {
                val pop = ECardTransactionPop(it.context, transactionInfo)
                pop.show()
            }
        }

        class ViewHolder(itemView: View, val rootView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.tv_transaction_title)
            val detail: TextView = itemView.findViewById(R.id.tv_transaction_detail)
            val icon: ImageView = itemView.findViewById(R.id.img_transaction)
        }

    }
}

fun MutableList<Item>.transactionItem(transactionInfo: TransactionInfo) = add(EcardTransactionItem(transactionInfo))

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
                itemName.text = "TRANSCATION TODAY"
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
                        view.context.startActivity<EcardPreviousActivity>()
                    }
                }
            }
            val itemManager = holder.recyclerView.withItems(mutableListOf())
            itemManager.refreshAll {
                lightText("暂未获取到消费数据")
            }

            LiveEcardManager.getEcardLiveData().observeForever {
                when(it) {
                    is RefreshState.Success -> it.message.apply {
                        val transactionList = this.transactionInfoList
                        itemManager.refreshAll {
                            if (transactionList.today().isEmpty()) {
                                holder.homeItem.apply {
                                    itemName.text = "TRANSACTION PREVIOUS"
                                }
                                transactionList.subList(0, 4).forEach {
                                    transactionItem(it)
                                }
                            } else {
                                holder.homeItem.apply {
                                    itemName.text = "TRANSACTION TODAY"
                                }
                                transactionList.today().forEach {
                                    transactionItem(it)
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