package com.twtstudio.retrox.tjulibrary.view

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Window

import com.twt.wepeiyang.commons.experimental.extensions.fitSystemWindowWithStatusBar
import com.twtstudio.retrox.tjulibrary.R

class HomeActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_home)
        toolbar = findViewById<android.support.v7.widget.Toolbar>(R.id.toolbar).also {
            fitSystemWindowWithStatusBar(it)
            setSupportActionBar(it)
        }
        setSupportActionBar(toolbar)
        toolbar.title = "图书馆"

        val mybook_pager : ViewPager = findViewById(R.id.booklist_pager)
        val mybook_tabLayout: TabLayout = findViewById(R.id.lb_home_table)
        //此处写代码
        mybook_tabLayout.setupWithViewPager(mybook_pager)
        mybook_tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        mybook_tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#A73870"))



    }
}
