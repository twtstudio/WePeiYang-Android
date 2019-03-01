package com.twt.service.ecard.view

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Window
import android.widget.SeekBar
import com.twt.service.ecard.R
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import org.jetbrains.anko.dip

class EcardChartActivity : AppCompatActivity() {

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
        val seekBar: SeekBar = findViewById(R.id.sb_chart_select)

    }
}
