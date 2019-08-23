package com.twt.service.ecard.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.twt.service.ecard.R
import com.twt.service.ecard.model.*
import com.twt.service.ecard.window.ECardInfoPop
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.cache.handleError
import com.twt.wepeiyang.commons.mta.mtaClick
import com.twt.wepeiyang.commons.mta.mtaExpose
import com.twt.wepeiyang.commons.ui.rec.HomeItem
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import com.twt.wepeiyang.commons.ui.text.spanned
import org.jetbrains.anko.*
import java.lang.NumberFormatException
import java.text.DecimalFormat
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
                    it.context.startActivity(Intent(it.context, EcardLoginActivity::class.java).apply {
                        putExtra("from", "HomeItem")
                    })
                }
            }

            var holder: ViewHolder by Delegates.notNull()
            val view = parent.context.verticalLayout {
                val balance = textView {
                    text = "校园卡余额"
                    textSize = 16f
                    typeface = ResourcesCompat.getFont(context, R.font.montserrat_regular)
                    textColor = Color.BLACK
                }.lparams {
                    topMargin = dip(6)
                }

                val todayCost = textView {
                    text = "今日消费"
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
                    gravity = Gravity.CENTER_HORIZONTAL
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

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder

            holder.stateText.setOnClickListener {
                mtaClick("ecard_首页ECard Item点击刷新")
                LiveEcardManager.refreshEcardFullInfo()
            }

            LiveEcardManager.getEcardLiveData().observeForever { eCardRefreshState ->
                when (eCardRefreshState) {
                    is RefreshState.Success -> eCardRefreshState.message.apply {
                        holder.balanceText.text = "校园卡余额：${ecardProfile.balance}"

                        try {
                            holder.todayCostView.text = "今日消费：${String.format("%.2f", totalCost.total_day)}元"
                        } catch (e: NumberFormatException) {
                            e.printStackTrace()
                            holder.todayCostView.text = "你遇到了待解析的特殊数据，多包涵~"
                        }

                        if (!cache) {
                            holder.stateText.text = "校园卡数据拉取成功，点击刷新"
                        }
                        holder.rootView.setOnClickListener { view ->
                            view.context.startActivity<EcardMainActivity>()
                            mtaClick("ecard_用户查看消费流水详情")
                        }
//                        holder.rootView.setOnClickListener {
//                            val infoPop = ECardInfoPop(it.context, ecardProfile, this.totalCost.total_day.toFloat())
//                            infoPop.show()
//                            mtaClick("ecard_点击查看校园卡详情_顶部PopWindow")
//                        }
                        mtaExpose("ecard_校园卡数据被成功刷新")
                    }
                    is RefreshState.Refreshing -> {
                        holder.stateText.text = "正在加载校园卡数据"
                    }
                    is RefreshState.Failure -> eCardRefreshState.handleError { _, _, message, _ ->
                        holder.stateText.text = "<span style=\"color:#E70C57\";>校园卡数据拉取错误 尝试重新绑定</span>".spanned
                        mtaExpose("ecard_校园卡无法被成功刷新_一般是没有绑定")
                    }
                }
            }

        }

        class ViewHolder(itemView: View?, val rootView: View, val balanceText: TextView, val todayCostView: TextView, val stateText: TextView) : RecyclerView.ViewHolder(itemView)

    }
}

fun MutableList<Item>.ecardInfoItem() = add(EcardInfoItem())

class EcardPersonInfoItem(val ecardProfile: EcardProfileBean, val ecardTotalConsumption: EcardTotalConsumptionBean) : Item {
    override val controller: ItemController
        get() = Controller

    override fun areItemsTheSame(newItem: Item): Boolean = true
    override fun areContentsTheSame(newItem: Item): Boolean = true

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.ecard_item_profile, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as EcardPersonInfoItem
            val ecardProfile = item.ecardProfile
            val ecardTotalConsumption = item.ecardTotalConsumption

            holder.apply {
                cardNum.text = ecardProfile.cardnum
                name.text = ecardProfile.name
                balance.text = "￥" + ecardProfile.balance.split("元")[0]
                todayConsume.text = "￥${String.format("%.2f", ecardTotalConsumption.total_day)}"
                monthConsume.text = "￥${String.format("%.2f", ecardTotalConsumption.total_month)}"

                if (ecardTotalConsumption.total_day >= 50) {
                    crownOfDay.visibility = View.VISIBLE
                }

                if (ecardTotalConsumption.total_month >= 2500) {
                    crownOfMonth.visibility = View.VISIBLE
                }

                if (ecardProfile.balance.split("元")[0].toFloat() <= 20L) {
                    warning.visibility = View.VISIBLE
                }
            }
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val cardNum: TextView = itemView.findViewById(R.id.tv_profile_number)
            val name: TextView = itemView.findViewById(R.id.tv_profile_name)
            val balance: TextView = itemView.findViewById(R.id.tv_profile_balance)
            val crownOfMonth: ImageView = itemView.findViewById(R.id.iv_profile_month_crown)
            val crownOfDay: ImageView = itemView.findViewById(R.id.iv_profile_day_crown)
            val todayConsume: TextView = itemView.findViewById(R.id.tv_profile_comsume_today)
            val monthConsume: TextView = itemView.findViewById(R.id.tv_profile_consume_month)
            val warning: TextView = itemView.findViewById(R.id.tv_profile_warning)
        }
    }
}

fun MutableList<Item>.ecardPersonInfoItem(ecardProfile: EcardProfileBean, ecardTotalConsumption: EcardTotalConsumptionBean) = add(EcardPersonInfoItem(ecardProfile, ecardTotalConsumption))