package com.twtstudio.retrox.tjulibrary.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Window
import com.twt.wepeiyang.commons.experimental.extensions.fitSystemWindowWithStatusBar
import com.twtstudio.retrox.tjulibrary.R

class DetialAcitvity : AppCompatActivity() {

    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        val indexOfBook = bundle.getString("indexOfBook")
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_detial)
        toolbar = findViewById<android.support.v7.widget.Toolbar>(R.id.toolbar).also {
            fitSystemWindowWithStatusBar(it)
            setSupportActionBar(it)
        }
        setSupportActionBar(toolbar)
        toolbar.title = "图书馆"
    }
}