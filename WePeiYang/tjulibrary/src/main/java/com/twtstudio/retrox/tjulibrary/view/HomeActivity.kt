package com.twtstudio.retrox.tjulibrary.view

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Window
import com.twtstudio.retrox.tjulibrary.R


class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.lib_activity_home)

        val toolbar = findViewById<Toolbar>(R.id.toolbar).also {
            title = "图书馆"
            it.setBackgroundColor(Color.parseColor("#e78fae"))

            setSupportActionBar(it)
        }
        window.statusBarColor = Color.parseColor("#e78fae")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }


        val mybookPager: ViewPager = findViewById(R.id.booklist_pager)
        val mybookTabLayout: TabLayout = findViewById(R.id.lb_home_table)
        val myhomePagerAdapter = HomeLibraryPagerAdapter(supportFragmentManager)
        myhomePagerAdapter.apply {
            add(HomeLibraryFragement(), "已借阅")
            add(RankFragment(), "借阅统计")
            add(ReadFragment(),"阅读")
        }
        mybookPager.adapter = myhomePagerAdapter
        mybookTabLayout.apply {
            setupWithViewPager(mybookPager)
            tabGravity = TabLayout.GRAVITY_FILL
            setSelectedTabIndicatorColor(Color.parseColor("#A73870"))
        }

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
