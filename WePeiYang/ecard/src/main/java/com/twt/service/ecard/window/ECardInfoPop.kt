package com.twt.service.ecard.window

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.twt.service.ecard.R
import com.twt.service.ecard.model.EcardProfileBean
import com.twt.wepeiyang.commons.ui.popup.TopPopupWindow
import kotlinx.android.synthetic.main.ecard_top_pop.view.*
import org.jetbrains.anko.*

class ECardInfoPop(context: Context, val eCardProfile: EcardProfileBean, val todayCost: Float) : TopPopupWindow(context) {
    override fun createContentView(parent: ViewGroup?): View {
        val view = context.layoutInflater.inflate(R.layout.ecard_top_pop, parent, false).apply {
            layoutParams = FrameLayout.LayoutParams(matchParent, wrapContent).apply {
                gravity = Gravity.TOP
            }
        }
        view.apply {
            tv_transaction_amount.text = "¥" + eCardProfile.balance.removeSuffix("元")
            tv_trans_location.text = "今日消费：$todayCost"
            tv_trans_time.text = "卡有效期：${eCardProfile.expiry}"
            tv_ecard_unused_money.text = "未领取补助: ${eCardProfile.subsidy}"
            tv_ecard_status.text = "卡状态：${eCardProfile.cardstatus}"
        }

        return view
    }


    init {
        isDismissOnClickBack = true
        isDismissOnTouchBackground = true
        Log.d("InfoPop2", eCardProfile.toString())
    }
}