package com.yookiely.lostfond2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import com.example.lostfond2.R

class LostFoundActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lf2_activity_lost_found)
        this.supportActionBar?.hide()
    }
}
