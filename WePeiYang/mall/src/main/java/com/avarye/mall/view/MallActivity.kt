package com.avarye.mall.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
import kotlinx.android.synthetic.main.mall_item_toolbar.*
import org.jetbrains.anko.inputMethodManager
import org.jetbrains.anko.verticalLayout

class MallActivity : AppCompatActivity() {
    private var page = 1
    private var totalPage = 1
    private var key = ""
    private var isSearching = false
    var isLoading = false
    var down = 0

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
            //加载下一页监听
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
//                    if (recView.computeVerticalScrollExtent() + recView.computeVerticalScrollOffset()
//                            >= recView.computeVerticalScrollRange()
//                            && page < totalPage && dy < 0 && !isLoading) {
//                        isLoading = true
//                        if (isSearching) {
//                            presenter.search(key, ++page)
//                        } else {
//                            presenter.getLatest(++page)
//                        }
//                    }
//                    isLoading = false
                    if (canScrollVertically(1)) {
                        isLoading = true
                        if (isSearching) {
                            presenter.search(key, ++page)
                        } else {
                            presenter.getLatest(++page)
                        }
                    }
                    isLoading = false

                }
            })
        }

        //下拉刷新监听
        mall_srl_main.setOnRefreshListener {
            Utils.clearGoods()
            Utils.clearSchGoods()
            page = 1
            totalPage = 1
            itemManager.refreshAll {
                removeAll { it is RecItem }
            }
            et_mall_search.isCursorVisible = false
            if (isSearching) {
                presenter.search(key, page)
            } else {
                presenter.getLatest(page)
            }
            mall_srl_main.isRefreshing = false
        }

        presenter.login()//初始登录+最新

        //回车监听
        et_mall_search.apply {
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
        iv_mall_search.setOnClickListener {
            et_mall_search.isCursorVisible = false
            search()
        }

        //测试emm
        mall_fab_mine.setOnClickListener {
            val intent = Intent(this, SaleActivity::class.java)
            this.startActivity(intent)
        }
    }

    private fun search() {
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
    }

    fun bindLatest(mContext: Context, data: List<Goods>) {
        totalPage = data[0].page
        itemManager.refreshAll {
            retainAll { it is RecItem }
            val items = mutableListOf<Item>().apply {
                for (i in 1..data.lastIndex) {
                    recItem {
                        Glide.with(mContext)
                                .load("https://mall.twt.edu.cn/api.php/Upload/img_redirect?id=${data[i].id}")
                                .into(image)
                        name.text = data[i].name
                        price.text = data[i].price
                        locate.text = data[i].location
                        card.setOnClickListener {
                            val intent = Intent(mContext, DetailActivity::class.java)
                                    .putExtra("name", data[i].name)
                                    .putExtra("seller", data[i].username)
                                    .putExtra("price", data[i].price)
                                    .putExtra("location", data[i].location)
                                    .putExtra("img", data[i].id)
                                    .putExtra("gid", data[i].id)
                                    .putExtra("desc", data[i].gdesc)
                                    .putExtra("time", data[i].ctime)
                                    .putExtra("bargain", data[i].bargain.toString())
                            mContext.startActivity(intent)
                        }
                    }
                }
            }
            addAll(0, items)
        }
    }

    fun bindSearch(mContext: Context, data: List<SchGoods>) {//TODO:imgurl可能有多个?
        itemManager.refreshAll {
            retainAll { it is RecItem }
            if (data[0].page == 0) {
                Toasty.info(this@MallActivity, "莫得结果TAT").show()
            }
            totalPage = data[0].page
            val items = mutableListOf<Item>().apply {
                for (i in 1 until data.size) {
                    recItem {
                        Glide.with(mContext)
                                .load("https://mall.twt.edu.cn/api.php/Upload/img_redirect?id=${data[i].imgurl}")
                                .into(image)
                        name.text = data[i].name
                        price.text = data[i].price
                        locate.text = data[i].location
                        card.setOnClickListener {
                            val intent = Intent(mContext, DetailActivity::class.java)
                                    .putExtra("name", data[i].name)
                                    .putExtra("seller", data[i].username)
                                    .putExtra("price", data[i].price)
                                    .putExtra("location", data[i].location)
                                    .putExtra("img", data[i].imgurl)
                                    .putExtra("gid", data[i].id)
                                    .putExtra("desc", data[i].gdesc)
                                    .putExtra("time", data[i].ctime)
                                    .putExtra("bargain", data[i].bargain)
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
            et_mall_search.text = null
            et_mall_search.isCursorVisible = false
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
