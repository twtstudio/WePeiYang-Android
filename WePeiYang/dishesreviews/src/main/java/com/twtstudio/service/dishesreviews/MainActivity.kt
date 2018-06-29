package com.twtstudio.service.dishesreviews

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twtstudio.retrox.auth.view.LoginActivity
import com.twtstudio.service.dishesreviews.extensions.CustomViewPager
import com.twtstudio.service.dishesreviews.search.view.SearchActivity


/**
 * Created by zhangyulong on 18-3-11.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var viewPager: CustomViewPager
    private lateinit var viewPagerAdapter: MainPagerAdapter
    private lateinit var toolbar: Toolbar
    private lateinit var tvTitle: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dishes_reviews_activity_main)
        if (!CommonPreferences.isLogin) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        viewPager = findViewById<CustomViewPager>(R.id.vp_main)
        viewPagerAdapter = MainPagerAdapter(supportFragmentManager)
        viewPager.apply {
            adapter = viewPagerAdapter
            setPagingEnabled(false)
        }
        toolbar = findViewById<Toolbar>(R.id.toolbar).apply {
            title = ""
        }
        setSupportActionBar(toolbar)
        setDisplayHomeAsUpEnabled(true)
        tvTitle = findViewById<TextView>(R.id.tv_toolbar_title).apply {
            text = getString(R.string.app_name)
        }

        bottomNavigationView.apply {
            setOnNavigationItemSelectedListener(
                    object : BottomNavigationView.OnNavigationItemSelectedListener {
                        override fun onNavigationItemSelected(item: MenuItem): Boolean {
                            when (item.getItemId()) {
                                R.id.action_home -> {
                                    viewPager.currentItem = item.order
                                    tvTitle.text = getString(R.string.app_name)
                                    setDisplayHomeAsUpEnabled(true)
                                }
//                                R.id.action_fast_comment -> {
//                                    menu.findItem(R.id.menu_search)?.setVisible(false)
//                                }
                                R.id.action_account -> {
                                    viewPager.currentItem = item.order
                                    tvTitle.text = getText(R.string.text_account)
                                    menu.findItem(R.id.menu_search)?.setVisible(false)
                                    setDisplayHomeAsUpEnabled(false)
                                }
                            }
                            invalidateOptionsMenu()
                            return true
                        }
                    })
            itemIconTintList = null //不设置IconTint
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dishes_reviews_menu_home_toolbar, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        when (viewPager.currentItem) {
            0 -> menu?.findItem(R.id.menu_search)?.setVisible(true)
            else -> menu?.findItem(R.id.menu_search)?.setVisible(false)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
            R.id.menu_search -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setDisplayHomeAsUpEnabled(showHomeAsUp: Boolean) {
        getSupportActionBar()?.apply {
            setDisplayHomeAsUpEnabled(showHomeAsUp)
            setHomeAsUpIndicator(R.drawable.dishes_reviews_ic_action_back)
        }
    }
}