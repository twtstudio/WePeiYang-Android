package com.avarye.mall.view

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import com.avarye.mall.Presenter
import com.avarye.mall.R
import com.avarye.mall.service.Goods
import com.twt.wepeiyang.commons.ui.rec.withItems
import es.dmoral.toasty.Toasty

import kotlinx.android.synthetic.main.mall_activity_main.*
import org.jetbrains.anko.toast

class MallActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout//TODO:等等看看

    private var presenter = Presenter(this)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mall_activity_main)
        window.statusBarColor = Color.parseColor("#FF9A36")
        setSupportActionBar(tb_mall_main)//???

        //TODO:menuButton
        //TODO:homeButton
        recyclerView = mall_rv_main
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
//        mall_vp_header.adapter = BannerAdapter(this, listOf())

        presenter.getLatest(1)//拿最新数据之前先进行了登陆

        iv_mall_search.setOnClickListener {
            //搜索
            val key = et_mall_search.text.toString()
            presenter.search(key, 1)
        }

//        menu.setOnClickListener { presenter.getMenu() }
//        home.setOnClickListener { refresh() }

        mall_fab_mine.setOnClickListener { presenter.getMenu() }

    }

    private fun adaptRec(context: Context, info: List<Goods>) {
        recyclerView.withItems {
            for (it in 1 until presenter.infoGoods!!.size) {
                add(RecItem(context, presenter.infoGoods!![it]))
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
//        Toasty.
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
