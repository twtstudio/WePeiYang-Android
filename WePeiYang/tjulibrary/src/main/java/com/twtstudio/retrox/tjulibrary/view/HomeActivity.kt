package com.twtstudio.retrox.tjulibrary.view

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.Window

import com.twtstudio.retrox.tjulibrary.R
import com.twtstudio.retrox.tjulibrary.search.SearchActivity

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.lb_activity_home)

        var toolbar = findViewById<Toolbar>(R.id.toolbar).also {
            title = "图书馆"
            it.setBackgroundColor(Color.parseColor("#e78fae"))

            setSupportActionBar(it)
        }
        window.statusBarColor = Color.parseColor("#e78fae")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }


        val mybook_pager: ViewPager = findViewById(R.id.booklist_pager)
        val mybook_tabLayout: TabLayout = findViewById(R.id.lb_home_table)
        var myhomePagerAdapter = HomeLibraryPagerAdapter(supportFragmentManager)
        myhomePagerAdapter.add(HomeLibraryFragement(), "已借阅")
        myhomePagerAdapter.add(RankFragment(), "借阅统计")
        myhomePagerAdapter.add(ReadFragment(),"阅读")
        mybook_pager.adapter = myhomePagerAdapter
        mybook_tabLayout.setupWithViewPager(mybook_pager)
        mybook_tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        mybook_tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#A73870"))


    }


//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.lb_menu, menu)
//        return true
//    }
//
//
//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        val itemId = item!!.itemId
//        val intent = Intent()
//
//        when (itemId) {
//            R.id.lib_search -> {
//                intent.setClass(this@HomeActivity, SearchActivity::class.java)
//            }
//
//
//        }
//
//        startActivity(intent)
//        return true
//    }

}
