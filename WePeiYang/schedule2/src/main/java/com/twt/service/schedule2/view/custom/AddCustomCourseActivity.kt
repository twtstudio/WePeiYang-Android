package com.twt.service.schedule2.view.custom

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.twt.service.schedule2.R
import kotlinx.android.synthetic.main.schedule_act_add_custom.*

class AddCustomCourseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.schedule_act_add_custom)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_schedule2,menu)
        return super.onCreateOptionsMenu(menu)
    }
}