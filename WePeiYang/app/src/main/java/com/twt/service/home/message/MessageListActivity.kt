package com.twt.service.home.message

import android.app.ActionBar
import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.twt.service.R
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.withItems

class MessageListActivity : AppCompatActivity() {
    var items = mutableListOf<Item>()
    private lateinit var arrowBackIc: ImageView
    private lateinit var orderIc: ImageView
    private lateinit var toolbar: android.support.v7.widget.Toolbar
    lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    var numHistory: Int = 0
    private var isRefreshing = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)
        swipeRefreshLayout = findViewById(R.id.message_refresh)
        arrowBackIc = findViewById(R.id.message_info_back_arrow)
        orderIc = findViewById(R.id.message_order)
        toolbar =findViewById(R.id.message_tool)
        recyclerView = findViewById(R.id.message_rec)
        recyclerView.layoutManager = LinearLayoutManager(this)
        window.statusBarColor = Color.parseColor("#646887")
        load()
        orderIc.setOnClickListener {
            settingPopupWindow()
        }
        swipeRefreshLayout.setOnRefreshListener {
            isRefreshing = true
            load()
            isRefreshing = false
            swipeRefreshLayout.isRefreshing = false
        }
        arrowBackIc.setOnClickListener {
            onBackPressed()
        }
    }

    private fun settingPopupWindow() {
        val contentView = LayoutInflater.from(this).inflate(R.layout.item_message_know, null)
        val mPopupWindow = PopupWindow(contentView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true)
        mPopupWindow.contentView = contentView
        mPopupWindow.isFocusable=true
        val lp: WindowManager.LayoutParams = window.attributes
        lp.alpha = 0.7f
        window.attributes = lp
        mPopupWindow.setOnDismissListener {
            val lp: WindowManager.LayoutParams = window.attributes
            lp.alpha = 1f
            window.attributes = lp
        }
        val allRead = contentView.findViewById<TextView>(R.id.message_allknow)
        val orderSetting = contentView.findViewById<TextView>(R.id.message_setting)
        val rootView = LayoutInflater.from(this).inflate(R.layout.activity_message_list, null)
        mPopupWindow.showAsDropDown(toolbar, -10, 0,Gravity.END)
    }

    private fun changeId(info: List<Info>) {
        info.forEach { Info ->
            putId(Info.id) {
                Log.d("message_all_read", it)
            }
        }
    }

    private fun load() {
        items.clear()
        recyclerView.withItems(items)
        getRecordMessage(CommonPreferences.studentid) { refreshState, recordMessage ->
            when (refreshState) {
                is RefreshState.Success -> {
                    numHistory = recordMessage!!.info.size
//                    if (numHistory != MessagePreferences.messageNum) {
//                        items.add(AllKnowItem {
//                            changeId(recordMessage.info)
//                            MessagePreferences.messageNum = numHistory
//                            items.clear()
//                            recyclerView.withItems(items)
//                        })
//                    }
                    Log.d("message_record", recordMessage.info.toString())
                    recordMessage.info.forEach {
                        if (it.read == 0) {
                            items.add(MessageListItem(this, it) { item, _ ->
                                putId(it.id) { str ->
                                    Log.d("message_read", str)
                                }
                                items.remove(item)
                                recyclerView.withItems(items)
                            })
                        }
                    }
                    recyclerView.withItems(items)
                }
                is RefreshState.Failure -> {
                    Log.d("message_record2", "出了点小问题")
                }
            }
        }
    }
}
