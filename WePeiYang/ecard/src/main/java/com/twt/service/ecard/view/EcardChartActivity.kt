package com.twt.service.ecard.view

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Window
import android.widget.*
import com.twt.service.ecard.R
import com.twt.service.ecard.model.EcardService
import com.twt.service.ecard.model.TransactionInfo
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


class EcardChartActivity : AppCompatActivity() {

    var selectTime = 7
    lateinit var totalConsumeText: TextView
    lateinit var totalRechargeText: TextView
    lateinit var ecardChartView: EcardChartView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.ecard_activity_chart)
        enableLightStatusBarMode(true)
        window.statusBarColor = Color.parseColor("#ffeb86")
        val toolbar: Toolbar = findViewById(R.id.tb_chart)
        toolbar.apply {
            title = " "
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }
        totalConsumeText = findViewById(R.id.tv_chart_consume)
        totalRechargeText = findViewById(R.id.tv_chart_recharge)
        ecardChartView = findViewById(R.id.ecv_chart)
        val seekBar: SeekBar = findViewById(R.id.sb_chart_select)
        seekBar.max = 100
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                ecardChartView.distanceOfBegin = -progress.toDouble() / 100
            }
        })
        val spinner: Spinner = findViewById(R.id.spinner_history_select)
        val lengthList = listOf("7天", "15天", "30天", "60天", "120天")
        spinner.apply {
            adapter = ArrayAdapter(this.context, android.R.layout.simple_spinner_dropdown_item, lengthList)
            background.setColorFilter(Color.parseColor("#568FFF"), PorterDuff.Mode.SRC_ATOP)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) = Unit

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val newTime = lengthList[position].split("天")[0].toInt()

                    if (selectTime != newTime) {
                        selectTime = newTime
                        refreshDataOfChart()
                        seekBar.progress = 0
                    }
                }
            }
        }

        refreshDataOfChart()
    }

    private fun refreshDataOfChart() {
        launch(UI + QuietCoroutineExceptionHandler) {
            val transactionListWrapper = EcardService.getEcardTransaction(term = selectTime).awaitAndHandle {
                it.printStackTrace()
                Toasty.error(this@EcardChartActivity, "加载失败").show()
            }?.data

            val historyData = transactionListWrapper ?: return@launch
            Toasty.success(this@EcardChartActivity, "拉取数据成功：${selectTime}天").show()

            val totalConsume = historyData.consumption.fold(0f) { prev: Float, transactionInfo: TransactionInfo ->
                prev + transactionInfo.amount.toFloat()
            }
            val totalRecharge = historyData.recharge.fold(0f) { prev: Float, transactionInfo: TransactionInfo ->
                prev + transactionInfo.amount.toFloat()
            }

            totalConsumeText.text = "消费：${String.format("%.2f", totalConsume)}元"
            totalRechargeText.text = "充值：${String.format("%.2f", totalRecharge)}元"

            historyData.consumption.asSequence().map {
                EcardChartView.DataWithDetail(it.amount.toDouble(), "${it.date.substring(4, 6)}/${it.date.substring(6, 8)}")
            }.toList().let {
                val dataWithDetail = it.toMutableList()
                dataWithDetail.add(0, EcardChartView.DataWithDetail((dataWithDetail[0].data * 2 / 3), " "))
                dataWithDetail.add(EcardChartView.DataWithDetail((dataWithDetail.last().data * 4 / 3), " "))
                ecardChartView.dataWithDetail = dataWithDetail
            }
        }
    }

}
