package com.twtstudio.service.dishesreviews.evaluate

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.TextView
import com.twtstudio.service.dishesreviews.R

class EvaluateActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var tvTitle: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dishes_reviews_activity_evaluate)
        toolbar = findViewById<Toolbar>(R.id.toolbar).apply {
            title = ""
        }
        setSupportActionBar(toolbar)
        setDisplayHomeAsUpEnabled(true)
        tvTitle = findViewById<TextView>(R.id.tv_toolbar_title).apply {
            text = "点评"
        }
    }

    private fun setDisplayHomeAsUpEnabled(showHomeAsUp: Boolean) {
        getSupportActionBar()?.apply {
            setDisplayHomeAsUpEnabled(showHomeAsUp)
            setHomeAsUpIndicator(R.drawable.dishes_reviews_ic_action_back)
        }
    }
}