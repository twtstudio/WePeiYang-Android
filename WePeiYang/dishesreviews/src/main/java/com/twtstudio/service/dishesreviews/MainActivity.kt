package com.twtstudio.service.dishesreviews

import android.app.ActionBar
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import com.twtstudio.service.dishesreviews.extensions.CustomViewPager
import com.twtstudio.service.dishesreviews.extensions.DishPreferences
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
    private lateinit var popupWindow: PopupWindow
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dishes_reviews_activity_main)

        val popContentView = layoutInflater.inflate(R.layout.dishes_reviews_home_popup, null)
        popupWindow = PopupWindow(popContentView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        viewPager = findViewById(R.id.vp_main)
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
        tvTitle = findViewById<TextView>(R.id.tv_toolbar_title)
        setToolBarTitle(tvTitle)
        tvTitle.setOnClickListener {
            popUpWindow(popContentView)
        }
        bottomNavigationView.apply {
            setOnNavigationItemSelectedListener { item ->
                when (item.getItemId()) {
                    R.id.action_home -> {
                        viewPager.currentItem = item.order
                        setToolBarTitle(tvTitle)
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
                true
            }
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

    private fun popUpWindow(view: View) {
        if (popupWindow.isShowing)
            return
        popupWindow.apply {
            isFocusable = true
            setBackgroundDrawable(ColorDrawable(0x00000000))
            isOutsideTouchable = true
            showAsDropDown(toolbar)
        }

        val tvNewCampus = view.findViewById<TextView>(R.id.tv_new_campus).setOnClickListener {
            DishPreferences.isNewCampus = true
            setToolBarTitle(tvTitle)
            popupWindow.dismiss()
            viewPagerAdapter.notifyDataSetChanged()
        }
        val tvOldCampus = view.findViewById<TextView>(R.id.tv_old_campus).setOnClickListener {
            DishPreferences.isNewCampus = false
            setToolBarTitle(tvTitle)
            popupWindow.dismiss()
            viewPagerAdapter.notifyDataSetChanged()
        }
    }

    private fun setToolBarTitle(textView: TextView) {
        var campus: String
        if (DishPreferences.isNewCampus)
            campus = "北洋园校区"
        else
            campus = "卫津路校区"
        textView.text = getString(R.string.dish_module_name) + '-' + campus + "▼"
    }
}