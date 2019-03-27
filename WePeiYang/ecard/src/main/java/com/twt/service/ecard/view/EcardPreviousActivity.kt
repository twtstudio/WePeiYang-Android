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
import android.widget.Spinner
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
import kotlinx.android.synthetic.main.ecard_activity_precious.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.dip
import org.jetbrains.anko.horizontalPadding
import org.jetbrains.anko.startActivity

class EcardPreviousActivity : AppCompatActivity() {
    private lateinit var chartFragment: EcardPreviousFragment
    private lateinit var preFragment: EcardPreviousFragment
    private val listOfTime = mutableListOf(7, 30, 60, 90)
    private lateinit var ecardPagerAdapter: EcardPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.ecard_activity_precious)
        enableLightStatusBarMode(true)
        window.statusBarColor = Color.parseColor("#ffeb86")
        val toolbar: Toolbar = findViewById(R.id.tb_pre)
        val titleOfToolbar: TextView = findViewById(R.id.tv_pre_title)
        val refreshOfToolbar: ImageView = findViewById(R.id.iv_pre_refresh)
        titleOfToolbar.text = "流水查账"
        toolbar.apply {
            title = " "
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }
        EcardPref.ecardHistorySpinnerIndex = 0
        setSelection()

        preFragment = EcardPreviousFragment.newInstance(EcardPref.PRE_LIST)
        chartFragment = EcardPreviousFragment.newInstance(EcardPref.PRE_CHART)
        ecardPagerAdapter = EcardPagerAdapter(supportFragmentManager).apply {
            add(preFragment)
            add(chartFragment)
        }
        vp_pre_pager.adapter = ecardPagerAdapter

        when (EcardPref.ecardHistorySpinnerIndex) {
            0 -> iv_pre_lselect.setImageResource(R.drawable.ecard_notime_left)
            3 -> iv_pre_rselect.setImageResource(R.drawable.ecard_notime_right)
        }

        iv_pre_lselect.setOnClickListener {
            if (EcardPref.ecardHistorySpinnerIndex >= 2) {
                --EcardPref.ecardHistorySpinnerIndex
                setSelection()
                refresh()
            } else if (EcardPref.ecardHistorySpinnerIndex == 1) {
                --EcardPref.ecardHistorySpinnerIndex
                iv_pre_lselect.setImageResource(R.drawable.ecard_notime_left)
                setSelection()
                refresh()
            }
            iv_pre_rselect.setImageResource(R.drawable.ecard_time_right)

        }

        iv_pre_rselect.setOnClickListener {
            if (EcardPref.ecardHistorySpinnerIndex <= 1) {
                ++EcardPref.ecardHistorySpinnerIndex
                setSelection()
                refresh()
            } else if (EcardPref.ecardHistorySpinnerIndex == 2) {
                ++EcardPref.ecardHistorySpinnerIndex
                iv_pre_rselect.setImageResource(R.drawable.ecard_notime_right)
                setSelection()
                refresh()
            }
            iv_pre_lselect.setImageResource(R.drawable.ecard_time_left)

        }

        refreshOfToolbar.setOnClickListener {
            refresh()
        }
    }

    private fun setSelection() {
        EcardPref.ecardHistoryLength = listOfTime[EcardPref.ecardHistorySpinnerIndex]
        tv_pre_select.text = "最近${EcardPref.ecardHistoryLength}天"
        mtaClick("ecard_用户设置流水查询时长_${EcardPref.ecardHistoryLength}")
    }

    private fun refresh() {
        val currentFragment = ecardPagerAdapter.getCurrentFragment() as? EcardPreviousFragment
        when (currentFragment?.typeOfPrecious) {
            EcardPref.PRE_LIST -> preFragment.refreshDataForHistory()
            EcardPref.PRE_CHART -> chartFragment.refreshDataForChart()
        }
    }

}