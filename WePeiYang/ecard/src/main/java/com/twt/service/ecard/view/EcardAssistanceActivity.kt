package com.twt.service.ecard.view

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Window
import android.widget.ImageButton
import android.widget.TextView
import android.support.v7.widget.Toolbar
import com.twt.service.ecard.R
import com.twt.service.ecard.model.*
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.ui.rec.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class EcardAssistanceActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.ecard_activity_assistance)
        window.statusBarColor = Color.parseColor("#ffeb86")
        val toolbar: Toolbar = findViewById(R.id.tb_assistance)
        val titleOfToolbar: TextView = findViewById(R.id.tv_assistance_toolbar_title)
        titleOfToolbar.text = "校园卡"
        toolbar.apply {
            title = " "
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }
        val refreshButton: ImageButton = findViewById(R.id.ib_assistance_refresh)
        val titleOfAssistance: TextView = findViewById(R.id.tv_assistance_total_title)
        val assistance = intent.getStringExtra(EcardPref.ASSISTANCE_MARK)

        titleOfAssistance.text = when (assistance) {
            EcardPref.KEY_PROBLEM -> "补办校园卡流程说明"
            EcardPref.KEY_REISSUE -> "校园卡常见问题"
            else -> "加载失败，请退出重试"
        }

        recyclerView = findViewById(R.id.rv_assistance_content)
        recyclerView.layoutManager = LinearLayoutManager(this@EcardAssistanceActivity)
        val itemManager = recyclerView.withItems(mutableListOf())
        itemManager.refreshAll {
            lightText("正在加载数据")
        }

        refreshButton.setOnClickListener {
            loadRecyclerView(assistance, itemManager)
        }
        loadRecyclerView(assistance, itemManager)
    }

    private fun loadRecyclerView(assistance: String, itemManager: ItemManager) {
        launch(UI + QuietCoroutineExceptionHandler) {
            val list = EcardService.getFQA().awaitAndHandle {
                it.printStackTrace()

                itemManager.refreshAll {
                    lightText("数据加载失败，请稍后重试")
                }
            }

            val assistanceList = list ?: return@launch


            itemManager.refreshAll {
                when (assistance) {
                    EcardPref.KEY_REISSUE -> {
                        assistanceList.toMutableList().filter { it.id == 8 }.forEach {
                            ecardAssistanceItem(it)
                        }
                    }
                    EcardPref.KEY_PROBLEM -> {
                        assistanceList.toMutableList().filter { it.id != 8 }.forEach {
                            ecardAssistanceItem(it)
                        }
                    }
                }
            }
        }
    }
}
