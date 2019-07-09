package com.twt.service.mall.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.widget.EditText
import android.widget.ImageView
import com.twt.service.R
import com.twt.service.mall.Presenter
import com.twt.service.mall.service.Goods
import com.twt.service.mall.service.SchGoods
import com.twt.service.mall.service.Utils
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.ui.rec.withItems

import kotlinx.android.synthetic.main.mall_activity_main.*
import org.jetbrains.anko.toast

class MallActivity2 : AppCompatActivity() {
    private lateinit var editText: EditText
    lateinit var searchImg: ImageView
    lateinit var viewPager: ViewPager
    lateinit var recyclerView: RecyclerView
    lateinit var fabMine: FloatingActionButton
    lateinit var menu: ImageView
    lateinit var home: ImageView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private var presenter = Presenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mall_activity_main)
        window.statusBarColor = Color.parseColor("#FF9A36")
        setSupportActionBar(tb_mall_main)//???

        init()

        presenter.getLatest(1)//拿最新数据之前先进行了登陆
        searchImg.setOnClickListener {
            val key = editText.text.toString()
            presenter.search(key, 1)
        }
//        menu.setOnClickListener { presenter.getMenu() }
//        home.setOnClickListener { refresh() }
        fabMine.setOnClickListener { presenter.getMenu() }

    }

    private fun init() {
        editText = et_mall_search
        searchImg = iv_mall_search
        viewPager = mall_vp_header
        recyclerView = mall_rv_main
        fabMine = mall_fab_mine
        //TODO:menuButton
        //TODO:homeButton
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
//        swipeRefreshLayout.apply {
//            setOnRefreshListener {
//                presenter.getLatest(1)
//            }
//            setColorSchemeColors(0xFF9A36)
//        }
    }

    private fun adaptRec(context: Context, info: List<Goods>) {
        recyclerView.withItems {
            for (it in presenter.infoGoods!!) {
                add(RecItem(context, it))
            }
        }
    }
    //                addItem(this@MallActivity2, it)
//    private fun MutableList<Item>.addItem(context: Context, info: Goods) = add(RecItem(context, info))

    fun refreshView(info: List<Goods>) {//刷新回调
        adaptRec(this, info)
    }

    fun notify(message: String) {//显示信息
        toast(message)
    }
}

/*
    其他界面的一些方法
    private fun getMyInfo() {//么的UI
        presenter.getMyInfo()
    }
    private fun getMenu() {//么的UI
        presenter.getMenu()
    }
    private fun sale() { presenter.addSale() } //发布商品
    private fun need() { presenter.addNeed() } //发布需求
*/


