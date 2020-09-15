package com.twt.service.announcement

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.twt.service.announcement.detail.DetailActivity

class AnnoTestActivity : AppCompatActivity() {
    private lateinit var detailButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anno_test)
        detailButton = findViewById(R.id.annoDetailButton)
        detailButton.setOnClickListener {
            startActivity(Intent(this, DetailActivity::class.java))
        }
    }
}