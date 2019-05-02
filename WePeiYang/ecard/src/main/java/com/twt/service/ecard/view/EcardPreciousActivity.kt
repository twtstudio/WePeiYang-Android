package com.twt.service.ecard.view

import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Window
import android.widget.*
import com.twt.service.ecard.R
import com.twt.service.ecard.model.EcardPref
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import com.twt.wepeiyang.commons.mta.mtaClick
import kotlinx.android.synthetic.main.ecard_activity_precious.*

class EcardPreciousActivity : AppCompatActivity() {
    private lateinit var chartFragment: EcardPreciousFragment
    private lateinit var preFragment: EcardPreciousFragment
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

        preFragment = EcardPreciousFragment.newInstance(EcardPref.PRE_LIST)
        chartFragment = EcardPreciousFragment.newInstance(EcardPref.PRE_CHART)
        ecardPagerAdapter = EcardPagerAdapter(supportFragmentManager).apply {
            add(preFragment)
            add(chartFragment)
        }
        vp_pre_pager.apply {
            adapter = ecardPagerAdapter
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageSelected(position: Int) {
                    val ecardPreviousFragment = (adapter as? EcardPagerAdapter)?.getItem(position) as? EcardPreciousFragment
                    when (ecardPreviousFragment?.typeOfPrecious) {
                        EcardPref.PRE_LIST -> {
                            iv_pre_loading_left.setImageResource(R.drawable.ecard_round_loading)
                            iv_pre_loading_right.setImageResource(R.drawable.ecard_round)
                        }
                        EcardPref.PRE_CHART -> {
                            iv_pre_loading_left.setImageResource(R.drawable.ecard_round)
                            iv_pre_loading_right.setImageResource(R.drawable.ecard_round_loading)
                        }
                    }
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

                override fun onPageScrollStateChanged(state: Int) = Unit

            })
        }

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
        val currentFragment = ecardPagerAdapter.getCurrentFragment() as? EcardPreciousFragment
        when (currentFragment?.typeOfPrecious) {
            EcardPref.PRE_LIST -> preFragment.refreshDataForHistory()
            EcardPref.PRE_CHART -> chartFragment.refreshDataForChart()
        }
    }
}