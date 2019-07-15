package com.avarye.mall.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.avarye.mall.MainPresenter
import com.avarye.mall.R
import com.avarye.mall.service.MallManager
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.mall_activity_main.*
import kotlinx.android.synthetic.main.mall_item_toolbar.*
import org.jetbrains.anko.inputMethodManager

class MallActivity : AppCompatActivity() {

    lateinit var tabLayout: TabLayout
    lateinit var mallViewpager: ViewPager
    private val pagerAdapter = MallPagerAdapter(supportFragmentManager)
    lateinit var current: Fragment
    private var key = ""
    private var isSearch = false

    private val presenter = MainPresenter(this)
    lateinit var sale: MallSaleFragment
    lateinit var need: MallNeedFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mall_activity_main)
        window.statusBarColor = Color.parseColor("#FF9A36")
        setSupportActionBar(tb_main)//???
        //TODO:menuButton
        //TODO:homeButton

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

        sale.setDo {
            presenter.getLatestNeed(it)
        }

        need.setDo {
            presenter.getLatestNeed(it)
        }

        presenter.login()

        current = pagerAdapter.getCurrent()

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

        //搜索
        iv_search.setOnClickListener {
            et_search.isCursorVisible = false
            search()
        }

        //测试emm
        fab_mine.setOnClickListener {
            val intent = Intent(this, SaleActivity::class.java)
            this.startActivity(intent)
        }
    }

    private fun search() {
        MallManager.clearGoods()
        MallManager.clearNeed()
        key = et_search.text.toString()
        if (key.isBlank()) {
            Toasty.info(this, "啥都莫得输入").show()
        } else {
            isSearch = true
//            presenter.search(key, )
            val intent = Intent(this, SaleActivity::class.java)
            this.startActivity(intent)
        }
    }


    override fun onBackPressed() {
        if (isSearch) {
            MallManager.clearGoods()
            MallManager.clearNeed()
            sale.get().resetPage()
            need.get().resetPage()
            et_search.text = null
            et_search.isCursorVisible = false
            isSearch = false
            presenter.getLatestSale(1)
        } else {
            super.onBackPressed()
        }
    }
}