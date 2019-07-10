package com.avarye.mall.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import com.avarye.mall.Presenter
import com.avarye.mall.R
import com.avarye.mall.service.Goods
import com.avarye.mall.service.SchGoods
import com.avarye.mall.service.Utils
import com.bumptech.glide.Glide
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import es.dmoral.toasty.Toasty

import kotlinx.android.synthetic.main.mall_activity_main.*
import org.jetbrains.anko.support.v4.onRefresh

class MallActivity : AppCompatActivity() {
    var page = 1
    var totalPage = 1
    var key = ""
    var isSearching = false
    private lateinit var recView: RecyclerView
    private val itemManager = ItemManager()

    val presenter = Presenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mall_activity_main)
        window.statusBarColor = Color.parseColor("#FF9A36")
        setSupportActionBar(tb_mall_main)//???
        //TODO:menuButton
        //TODO:homeButton
        recView = mall_rv_main.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = ItemAdapter(itemManager)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (recView.computeVerticalScrollExtent() + recView.computeVerticalScrollOffset()
                            >= recView.computeVerticalScrollRange()
                            && page < totalPage) {
                        if (isSearching) {
                            presenter.search(key, ++page)
                        } else {
                            presenter.getLatest(++page)
                        }
                    }
                }
            })
        }

        mall_srl_main.setOnRefreshListener {
            Utils.clearGoods()
            Utils.clearSchGoods()
            page = 1
            totalPage = 1
            if (isSearching) {
                presenter.search(key, page)
            } else {
                presenter.getLatest(page)
            }

            mall_srl_main.isRefreshing = false
        }

        presenter.login()

        iv_mall_search.setOnClickListener {
            Utils.clearGoods()
            Utils.clearSchGoods()
            page = 1
            totalPage = 1
            key = et_mall_search.text.toString()
            if (key.isBlank()) {
                Toasty.info(this, "啥都莫得输入").show()
            } else {
                isSearching = true
                presenter.search(key, page)
            }
        }//搜索

//        menu.setOnClickListener { presenter.getMenu() }
//        home.setOnClickListener { refresh() }
//        mall_fab_mine.setOnClickListener { presenter.getMenu() }
    }

    fun bindLatest(mContext: Context, data: List<Goods>) {
        totalPage = data[0].page
        itemManager.autoRefresh {
            removeAll { it is RecItem }
            val items = mutableListOf<Item>().apply {
                for (i in 1 until data.size) {
                    recItem {
                        Glide.with(mContext)
                                .load("https://mall.twt.edu.cn/api.php/Upload/img_redirect?id=${data[i].id}")
                                .into(image)
                        name.text = data[i].name
                        price.text = data[i].price
                        campus.text = Utils.getCampus(data[i].campus)
                        card.setOnClickListener {
                            val intent = Intent(mContext, DetailActivity::class.java)
                            mContext.startActivity(intent)
                        }
                    }
                }
            }
            addAll(0, items)
        }
    }

    fun bindSearch(mContext: Context, data: List<SchGoods>) {//TODO:imgurl可能有多个
        totalPage = data[0].page
        itemManager.autoRefresh {
            removeAll { it is RecItem }
            val items = mutableListOf<Item>().apply {
                for (i in 1 until data.size) {
                    recItem {
                        Glide.with(mContext)
                                .load("https://mall.twt.edu.cn/api.php/Upload/img_redirect?id=${data[i].imgurl}")
                                .into(image)
                        name.text = data[i].name
                        price.text = data[i].price
                        campus.text = Utils.getCampus(data[i].campus)
                        card.setOnClickListener {
                            val intent = Intent(mContext, DetailActivity::class.java)
                            mContext.startActivity(intent)
                        }
                    }
                }
            }
            addAll(0, items)
        }
    }

    override fun onBackPressed() {
        if (isSearching) {
            Utils.clearGoods()
            Utils.clearSchGoods()
            page = 1
            totalPage = 1
            isSearching = false
            presenter.getLatest(page)
        } else {
            super.onBackPressed()
        }
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
