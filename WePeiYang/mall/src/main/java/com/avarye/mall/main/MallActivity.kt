package com.avarye.mall.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.avarye.mall.R
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.mall_activity_main.*
import kotlinx.android.synthetic.main.mall_item_toolbar.*
import org.jetbrains.anko.inputMethodManager

class MallActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var mallViewpager: ViewPager
    private val pagerAdapter = MallPagerAdapter(supportFragmentManager)
    private lateinit var sale: MallSaleFragment
    private lateinit var need: MallNeedFragment
    private var key = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mall_activity_main)
        window.statusBarColor = Color.parseColor("#FF9A36")

        //toolbar
        tb_main.apply {
            title = "天外天商城"
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }
        //TODO:menuButton
        //TODO:homeButton

        //fragment
        sale = MallSaleFragment()
        need = MallNeedFragment()
        tabLayout = tl_main
        mallViewpager = vp_main
        pagerAdapter.apply {
            add(sale, "最新商品")
            add(need, "最新求购")
        }
        mallViewpager.adapter = pagerAdapter
        tabLayout.apply {
            setupWithViewPager(mallViewpager)
        }

        //回车监听
        et_search.apply {
            setOnClickListener {
                isCursorVisible = true
            }
            setOnEditorActionListener { p0, p1, p2 ->
                if (p1 == EditorInfo.IME_ACTION_SEARCH || (p2 != null && p2.keyCode == KeyEvent.KEYCODE_ENTER)) {
                    inputMethodManager.hideSoftInputFromWindow(p0.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                    isCursorVisible = false
                    search()
                    true
                } else {
                    false
                }
            }
        }

        //搜索按钮
        iv_search.setOnClickListener {
            et_search.isCursorVisible = false
            search()
        }
    }

    private fun search() {
        key = et_search.text.toString()
        if (key.isBlank()) {
            Toasty.info(this, "啥都莫得输入").show()
        } else {
            val intent = Intent(this, SearchActivity::class.java)
                    .putExtra("key", key)
            this.startActivity(intent)
        }
    }

//    override fun onBackPressed() {
//        if (isSearch) {
//            MallManager.clearGoods()
//            MallManager.clearNeed()
//            sale.resetPage()
//            need.resetPage()
//            et_search.text = null
//            et_search.isCursorVisible = false
//            isSearch = false
//            presenter.getLatestSale(1)
//        } else {
//            super.onBackPressed()
//        }
//    }
}