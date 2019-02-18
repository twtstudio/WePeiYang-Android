package com.twt.service.ecard.view

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.widget.FrameLayout
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
import org.jetbrains.anko.*

class EcardPreviousActivity : AppCompatActivity() {
    val recyclerView by lazy {
        RecyclerView(this).apply {
            layoutManager = LinearLayoutManager(this@EcardPreviousActivity)
            layoutParams = FrameLayout.LayoutParams(matchParent, matchParent)
        }
    }
    val itemManager by lazy { recyclerView.withItems(listOf()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.WHITE
        enableLightStatusBarMode(true)
        frameLayout {
            backgroundColor = Color.WHITE
            addView(recyclerView)
        }

        refreshData()
    }

    private fun refreshData() {
        launch(UI + QuietCoroutineExceptionHandler) {
            val history = EcardService.getEcardTransaction(day = EcardPref.ecardHistoryLength).awaitAndHandle {
                it.printStackTrace()
                itemManager.refreshAll {
                    firstSelectItem()
                    lightText("") {
                        horizontalPadding = dip(16)
                        text = ("<span style=\"color:#E70C57\";>拉取${EcardPref.ecardHistoryLength}天校园卡数据失败，这可能是因为您的拉取时长超越了校园卡有效期，" +
                                "此情况通常会发生在补办饭卡之后，查询期限超越补办期 \n 当然也可能是是饭卡的账号密码错了...</span>").spanned
                    }
                }
            }?.data?.transaction

            val balanceInHistory = EcardService.getEcardTransaction(day = EcardPref.ecardHistoryLength, type = 1).awaitAndHandle {
                it.printStackTrace()
            }?.data?.transaction

            val historyData = history ?: return@launch

            Toasty.success(this@EcardPreviousActivity, "拉取数据成功：${EcardPref.ecardHistoryLength}天").show()
            val allList = mutableListOf<TransactionInfo>().apply {
                addAll(historyData.onEach { it.isCost = true })
                balanceInHistory?.let { addAll(it.onEach { it.isCost = false }) }
                sortByDescending { it.date.toLong() }
            }

            itemManager.refreshAll {
                allList.forEachIndexed { index, transactionInfo ->
                    val nextTransactionInfo = allList.getOrNull(index + 1)
                    if (index == 0) {
                        firstSelectItem()
                        lightText(transactionInfo.date) // 增加日期分割符号
                    }
                    transactionItem(transactionInfo)
                    if (transactionInfo.date != nextTransactionInfo?.date) {
                        nextTransactionInfo?.let { lightText(it.date) } // 增加日期分割符号
                    }

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