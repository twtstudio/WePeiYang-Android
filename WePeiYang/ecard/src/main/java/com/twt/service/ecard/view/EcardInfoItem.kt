package com.twt.service.ecard.view

import android.content.Intent
import android.graphics.Color
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.service.ecard.R
import com.twt.service.ecard.model.*
import com.twt.service.ecard.window.ECardInfoPop
import com.twt.wepeiyang.commons.ui.rec.HomeItem
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import com.twt.wepeiyang.commons.ui.text.spanned
import org.jetbrains.anko.*
import java.io.EOFException
import java.net.SocketTimeoutException
import kotlin.properties.Delegates

class EcardInfoItem : Item {
    override val controller: ItemController
        get() = Controller

    override fun areItemsTheSame(newItem: Item): Boolean = true
    override fun areContentsTheSame(newItem: Item): Boolean = true

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val homeItem = HomeItem(parent)
            homeItem.apply {
                itemName.text = "ECARD (EXPERIMENTAL)"
            }
            homeItem.itemContent.apply {
                text = "绑定设置"
                setOnClickListener {
                    it.context.startActivity(Intent(it.context, EcardLoginActivity::class.java))
                }
            }

            var holder: ViewHolder by Delegates.notNull()
            val view = parent.context.verticalLayout {
                val balance = textView {
                    textSize = 16f
                    typeface = ResourcesCompat.getFont(context, R.font.montserrat_regular)
                    textColor = Color.BLACK
                }.lparams {
                    topMargin = dip(6)
                }

                val todayCost = textView {
                    textSize = 14f
                    typeface = ResourcesCompat.getFont(context, R.font.montserrat_regular)
                    textColor = Color.BLACK
                }.lparams {
                    verticalMargin = dip(6)
                }

                val stateInfo = textView {
                    textSize = 12f
                    typeface = ResourcesCompat.getFont(context, R.font.montserrat_regular)
                    textColor = Color.BLACK
                    textColor = Color.parseColor("#B9B9B9")
                    text = "点击刷新"
                }.lparams(width = wrapContent, height = wrapContent) {
                    gravity = Gravity.CENTER_HORIZONTAL
                    bottomMargin = dip(6)
                }
                holder = ViewHolder(homeItem.rootView, this, balance, todayCost, stateInfo)
                horizontalPadding = dip(32)
            }
            homeItem.setContentView(view)
            return holder
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder

            fun refresh(forceReload: Boolean) {
                holder.apply {
                    balanceText.text = "Fetching Balance Infomation"
                    todayCostView.text = "Just a second..."
                }
                LiveEcardManager.refreshEcardFullInfo(forceReload)
            }

            refresh(false)

            holder.stateText.setOnClickListener { view ->
                refresh(false)
            }

            LiveEcardManager.getEcardLiveData().observeForever {
                it?.apply {
                    holder.balanceText.text = "校园卡余额：${personInfo.balance}"
                    holder.todayCostView.text = "今日消费：${todayCost}元"
                    if (!cache) {
                        holder.stateText.text = "校园卡数据拉取成功，点击刷新"
                    }
                    holder.rootView.setOnClickListener {
                        val infoPop = ECardInfoPop(it.context, personInfo, todayCost)
                        infoPop.show()
                    }
                }
            }

            LiveEcardManager.getEcardExceptionLiveData().observeForever {
                it?.let { throwable ->
                    if (throwable is EOFException) {
                        holder.stateText.text = "<span style=\"color:#E70C57\";>尚未绑定校园卡或校园卡密码错误，点击卡片右上角'绑定设置'绑定</span>".spanned
                    } else if (throwable is SocketTimeoutException) {
                        holder.stateText.text = "<span style=\"color:#E70C57\";>校园卡服务仅能在tjuwlan访问，切换网络后点击重试</span>".spanned
                    }
                }
            }


        }

        class ViewHolder(itemView: View?, val rootView: View, val balanceText: TextView, val todayCostView: TextView, val stateText: TextView) : RecyclerView.ViewHolder(itemView)

    }
}

fun MutableList<Item>.ecardInfoItem() = add(EcardInfoItem())