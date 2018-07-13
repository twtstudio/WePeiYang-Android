package com.example.lostfond2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar

class LostFoundActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lost_found)
        this.supportActionBar?.hide()
    }
}
