package com.twt.service.ecard.view

import android.arch.lifecycle.Observer
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.twt.service.ecard.R
import com.twt.service.ecard.model.*
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import com.twt.wepeiyang.commons.mta.mtaClick
import com.twt.wepeiyang.commons.ui.rec.*
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.*
import org.jetbrains.anko.startActivity
import java.text.SimpleDateFormat
import java.util.*

class EcardMainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val itemManager by lazy { recyclerView.withItems(mutableListOf()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.ecard_activity_main)
        enableLightStatusBarMode(true)
        window.statusBarColor = Color.parseColor("#ffeb86")
        val toolbar: Toolbar = findViewById(R.id.tb_main)
        val titleOfToolbar: TextView = findViewById(R.id.tv_main_title)
        val refreshButton: ImageView = findViewById(R.id.iv_main_refresh)
        titleOfToolbar.text = "校园卡"
        toolbar.apply {
            title = " "
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }
        val today = SimpleDateFormat("yyyyMMdd").format(Date().time)
        val month = today.substring(4, 6)
        val day = today.substring(6, 8)
        recyclerView = findViewById(R.id.rv_main_main)
        recyclerView.layoutManager = LinearLayoutManager(this@EcardMainActivity)

        refreshButton.setOnClickListener {
            LiveEcardManager.refreshEcardFullInfo()
        }

        itemManager.refreshAll { lightText("正在加载数据") }

        LiveEcardManager.getEcardLiveData().observe(this, Observer { refreshState ->
            when (refreshState) {
                is RefreshState.Success -> refreshState.message.apply {
                    val ecardProfile = this.ecardProfile
                    val ecardTotalConsumption = this.totalCost
                    val transactionListWrapper = this.transactionListWrapper
                    val transactionList = mutableListOf<TransactionInfo>().apply {
                        addAll(transactionListWrapper.recharge)
                        addAll(transactionListWrapper.consumption)
                        sortWith(compareBy({ it.date }, { it.time }))
                        reverse()
                    }

                    itemManager.refreshAll {
                        ecardPersonInfoItem(ecardProfile, ecardTotalConsumption)
                        ecardElseItem("今日流水", TypeOfElse.TODAY) { _, view ->
                            view.context.startActivity<EcardPreviousActivity>()
                            mtaClick("ecard_用户查看消费流水详情")
                        }
                        ecardElseItem("$month/$day", TypeOfElse.DATE)

                        if (transactionList.today().isNotEmpty()) {
                            transactionList.today().take(4).forEach {
                                transactionItem(it, transactionListWrapper.consumption.contains(it))
                            }

                            val todayElse = transactionList.today().size - 4
                            if (todayElse > 0) {
                                ecardElseItem("${todayElse}条记录被折叠", TypeOfElse.TIPS) { item, _ ->
                                    itemManager.autoRefresh {
                                        addAll(7, mutableListOf<Item>().apply {
                                            transactionList.today().takeLast(todayElse).forEach {
                                                transactionItem(it, transactionListWrapper.consumption.contains(it))
                                            }
                                        })
                                        remove(item)
                                    }
                                }

                            }

                            lightText("") {
                                layoutParams = FrameLayout.LayoutParams(matchParent, dip(41))
                            }
                        } else {
                            lightText("今天你还没有消费哟～") {
                                layoutParams = FrameLayout.LayoutParams(matchParent, dip(58)).apply {
                                    verticalMargin = dip(6)
                                }
                                gravity = Gravity.CENTER_HORIZONTAL
                            }
                        }

                        ecardElseItem("帮助", TypeOfElse.TITLE)
                        ecardElseItem("补办校园卡流程说明", TypeOfElse.TEXT) { _, view ->
                            view.context.startActivity<EcardAssistanceActivity>(EcardPref.ASSISTANCE_MARK to EcardPref.KEY_REISSUE)
                        }
                        ecardElseItem("校园卡常见问题", TypeOfElse.TEXT) { _, view ->
                            view.context.startActivity<EcardAssistanceActivity>(EcardPref.ASSISTANCE_MARK to EcardPref.KEY_PROBLEM)
                        }
                        lightText("") {
                            layoutParams = FrameLayout.LayoutParams(matchParent, dip(5))
                        }
                    }
                }
                is RefreshState.Failure -> Toasty.error(this@EcardMainActivity, "刷新失败，请稍后重试").show()
            }
        })

    }
}
