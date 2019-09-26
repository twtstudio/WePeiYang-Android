package com.avarye.mall.main

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import com.avarye.mall.R
import com.avarye.mall.mine.MineActivity
import com.avarye.mall.service.MallManager
import com.avarye.mall.service.MallManager.bgAlpha
import com.avarye.mall.service.menuLiveData
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.mall_activity_main.*
import kotlinx.android.synthetic.main.mall_item_toolbar.*
import kotlinx.android.synthetic.main.mall_popup_menu.view.*
import org.jetbrains.anko.inputMethodManager
import org.jetbrains.anko.textColor

/**
 * 商城主界面
 */
class MallActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var mallViewpager: ViewPager
    private val pagerAdapter = MallPagerAdapter(supportFragmentManager)
    private lateinit var sale: MallSaleFragment
    private lateinit var need: MallNeedFragment
    private var key = ""
    private var mode = MallManager.W_SALE
    private lateinit var popWindow: PopupWindow
    private lateinit var menuViewAdapter: MenuViewAdapter

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mall_activity_main)
        window.statusBarColor = ContextCompat.getColor(this, R.color.mallColorMain)

        //toolbar
        tb_main.apply {
            title = getString(R.string.mallStringTitle)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }

        //menu
        val popupWindowView: View = LayoutInflater.from(this).inflate(R.layout.mall_popup_menu, null, false)
        menuLiveData.bindNonNull(this) { list ->
            menuViewAdapter = MenuViewAdapter(this, list)
            popupWindowView.apply {
                cv_menu_sale.setOnClickListener {
                    mode = MallManager.W_SALE
                    tv_menu_sale.textColor = ContextCompat.getColor(this@MallActivity, R.color.mallColorMain)
                    tv_menu_need.textColor = ContextCompat.getColor(this@MallActivity, R.color.mallColorTextLight)
                    tv_menu_sale.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    tv_menu_need.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                }
                cv_menu_need.setOnClickListener {
                    mode = MallManager.W_NEED
                    tv_menu_sale.textColor = ContextCompat.getColor(this@MallActivity, R.color.mallColorTextLight)
                    tv_menu_need.textColor = ContextCompat.getColor(this@MallActivity, R.color.mallColorMain)
                    tv_menu_sale.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                    tv_menu_need.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                }
                elv_menu.apply {
                    setGroupIndicator(null)
                    setAdapter(menuViewAdapter)
                    setOnGroupExpandListener {
                        for (i in 0 until adapter.count) {
                            if (it != i) {
                                this.collapseGroup(i)
                            }
                        }
                    }
                    setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
                        val intent = Intent(this@MallActivity, SearchActivity::class.java)
                                .putExtra(MallManager.KEY, list[groupPosition].smalllist[childPosition].id)
                                .putExtra(MallManager.TYPE, MallManager.SELECT)
                                .putExtra(MallManager.MODE, mode)
                        this@MallActivity.startActivity(intent)
                        true
                    }
                }
            }
            iv_menu.setOnClickListener {
                popWindow = PopupWindow(popupWindowView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
                popWindow.apply {
                    showAsDropDown(it, -400, 0)
                    isOutsideTouchable = true
                    isTouchable = true
                    isFocusable = true
                    bgAlpha(0.5f, this@MallActivity)
                    setOnDismissListener {
                        bgAlpha(1f, this@MallActivity)
                    }
                }
            }
        }

        //fragment
        sale = MallSaleFragment()
        need = MallNeedFragment()
        tabLayout = tl_main
        mallViewpager = vp_main
        pagerAdapter.apply {
            add(sale, getString(R.string.mallStringLatestSale))
            add(need, getString(R.string.mallStringLatestNeed))
        }
        mallViewpager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(mallViewpager)

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
            Toasty.info(this, "啥都妹有输入").show()
        } else {
            val intent = Intent(this, SearchActivity::class.java)
                    .putExtra(MallManager.KEY, key)
                    .putExtra(MallManager.TYPE, MallManager.SEARCH)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mall_menu_mine, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val intent = Intent(this, MineActivity::class.java)
        startActivity(intent)
        return true
    }
}