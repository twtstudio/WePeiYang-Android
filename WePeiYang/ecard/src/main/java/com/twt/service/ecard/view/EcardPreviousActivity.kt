package com.twt.service.ecard.view

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.twt.service.ecard.R
import com.twt.service.ecard.model.EcardPref
import com.twt.service.ecard.model.EcardService
import com.twt.service.ecard.model.TransactionInfo
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import com.twt.wepeiyang.commons.mta.mtaClick
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.lightText
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.twt.wepeiyang.commons.ui.text.spanned
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.dip
import org.jetbrains.anko.horizontalPadding
import org.jetbrains.anko.startActivity

class EcardPreviousActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private val itemManager by lazy { recyclerView.withItems(listOf()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.ecard_activity_main)
        enableLightStatusBarMode(true)
        window.statusBarColor = Color.parseColor("#ffeb86")
        val toolbar: Toolbar = findViewById(R.id.tb_main)
        val titleOfToolbar: TextView = findViewById(R.id.tv_main_title)
        val refreshOfToolbar: ImageView = findViewById(R.id.iv_main_refresh)
        titleOfToolbar.text = "流水查账"
        toolbar.apply {
            title = " "
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }

        recyclerView = findViewById(R.id.rv_main_main)
        recyclerView.layoutManager = LinearLayoutManager(this@EcardPreviousActivity)

        refreshOfToolbar.setImageResource(R.drawable.ecard_chart)
        refreshOfToolbar.setOnClickListener {
            it.context.startActivity<EcardChartActivity>()
        }
        refreshData()
    }

    private fun refreshData() {
        itemManager.refreshAll {
            lightText("正在加载数据")
        }

        launch(UI + QuietCoroutineExceptionHandler) {
            val transactionListWrapper = EcardService.getEcardTransaction(term = EcardPref.ecardHistoryLength).awaitAndHandle {
                it.printStackTrace()
                itemManager.refreshAll {
                    firstSelectItem()
                    lightText("") {
                        horizontalPadding = dip(16)
                        text = ("<span style=\"color:#E70C57\";>拉取${EcardPref.ecardHistoryLength}天校园卡数据失败，这可能是因为您的拉取时长超越了校园卡有效期，" +
                                "此情况通常会发生在补办饭卡之后，查询期限超越补办期 \n 当然也可能是是饭卡的账号密码错了...</span>").spanned
                    }
                }
            }?.data

            val historyData = transactionListWrapper ?: return@launch

            Toasty.success(this@EcardPreviousActivity, "拉取数据成功：${EcardPref.ecardHistoryLength}天").show()
            val transactionList = mutableListOf<TransactionInfo>().apply {
                addAll(historyData.recharge)
                addAll(historyData.consumption)
                sortWith(compareBy({ it.date }, { it.time }))
                reverse()
            }

            itemManager.refreshAll {
                firstSelectItem()
                transactionList.forEach {
                    transactionItem(it, historyData.consumption.contains(it))
                }
            }
            recyclerView.scrollToPosition(0)
        }
    }

    private fun MutableList<Item>.firstSelectItem() {
        val lengthList = listOf("7天", "15天", "30天", "60天", "120天")
        ecardHistorySelectItem(lengthList) {
            val length = lengthList[it].split("天")[0].toInt()
            if (length != EcardPref.ecardHistoryLength) {
                EcardPref.ecardHistoryLength = length
                refreshData()
            }
            mtaClick("ecard_用户设置流水查询时长_${lengthList[it]}")
        }
    }
}