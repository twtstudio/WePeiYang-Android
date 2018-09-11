package com.twtstudio.retrox.tjulibrary.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class DetialAcitvity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        val indexOfBook = bundle.getString("indexOfBook")
        super.onCreate(savedInstanceState)
    }
}