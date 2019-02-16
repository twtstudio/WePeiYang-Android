package com.twt.service.ecard.view

import android.arch.lifecycle.Observer
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.view.Window
import android.widget.ImageButton
import android.widget.TextView
import com.twt.service.ecard.R
import com.twt.service.ecard.model.LiveEcardManager
import com.twt.service.ecard.model.TransactionInfo
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.ui.rec.lightText
import com.twt.wepeiyang.commons.ui.rec.withItems

class EcardMainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.ecard_activity_main)
        window.statusBarColor = Color.parseColor("#ffeb86")
        val toolbar: Toolbar = findViewById(R.id.tb_main)
        val titleOfToolbar: TextView = findViewById(R.id.tv_main_title)
        titleOfToolbar.text = "校园卡"
        toolbar.apply {
            title = " "
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }

        recyclerView = findViewById(R.id.rv_main_main)
        recyclerView.layoutManager = LinearLayoutManager(this@EcardMainActivity)
        val itemManager = recyclerView.withItems(mutableListOf())
        itemManager.refreshAll {
            lightText("正在加载数据")
        }

        val refreshButton: ImageButton = findViewById(R.id.ib_main_refresh)

        refreshButton.setOnClickListener {
            LiveEcardManager.refreshEcardFullInfo()
        }

        LiveEcardManager.getEcardLiveData().observe(this, Observer {
            when (it) {
                is RefreshState.Success -> it.message.apply {
                    val ecardProfile = this.ecardProfile
                    val ecardTotalConsumption = this.totalCost
                    val transactionListWrapper = this.transactionListWrapper
                    val transactionList = mutableListOf<TransactionInfo>()
                    transactionList.apply {
                        addAll(transactionListWrapper.recharge)
                        addAll(transactionListWrapper.consumption)
                        sortWith(compareBy({ it.date }, { it.time }))
                        reverse()
                    }

                    itemManager.refreshAll {
                        ecardPersonInfoItem(ecardProfile, ecardTotalConsumption)

                    }
                }
            }
        })

    }
}
