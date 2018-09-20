package com.yookiely.lostfond2.mylist

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Window
import com.example.lostfond2.R

class MyListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_my_list)
        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = "我的"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        val mylist_pager: ViewPager = findViewById(R.id.mylist_pager)

        val mylist_tabLayout: TabLayout = findViewById(R.id.mylist_tabLayout)
        val mylistPagerAdapter = MylistPagerAdapter(supportFragmentManager)
        mylistPagerAdapter.add(MylistFragement.newInstance("found"), "我捡到的")
        mylistPagerAdapter.add(MylistFragement.newInstance("lost"), "我丢失的")
        mylist_pager.adapter = mylistPagerAdapter
        mylist_tabLayout.setupWithViewPager(mylist_pager)
        mylist_tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        mylist_tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#00a1e9"))


    }
}
