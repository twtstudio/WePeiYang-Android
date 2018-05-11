package com.twtstudio.service.dishesreviews.dish.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.TextView
import com.twtstudio.service.dishesreviews.R

class DishActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var tvTitle: TextView
    private lateinit var rvLabel: RecyclerView
    private lateinit var rvComment: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dishes_reviews_activity_dish)
        toolbar = findViewById<Toolbar>(R.id.toolbar).apply {
            title = ""
        }
        setSupportActionBar(toolbar)
        getSupportActionBar()?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.dishes_reviews_ic_action_back)
        }
        tvTitle = findViewById<TextView>(R.id.tv_toolbar_title).apply {

        }
        rvLabel = findViewById<RecyclerView>(R.id.rv_labels).apply {
            layoutManager = GridLayoutManager(this@DishActivity, 2)
        }
        rvComment = findViewById<RecyclerView>(R.id.rv_comment).apply {
            layoutManager = LinearLayoutManager(this@DishActivity, LinearLayoutManager.VERTICAL, false)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}