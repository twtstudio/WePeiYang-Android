package com.avarye.mall.view

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.avarye.mall.detail.DetailActivity
import com.avarye.mall.service.LatestNeed
import com.avarye.mall.service.LatestSale
import com.avarye.mall.service.MallManager
import com.bumptech.glide.Glide
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import kotlinx.android.synthetic.main.mall_activity_main.view.*
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class MainComponent(itemView: View) {

    private val mContext: Context = itemView.context
    private var page = 1
    private var totalPage = 1
    private var isLoading = false

    private var recView = RecyclerView(mContext)
    private val itemManager = ItemManager()

    internal var redo: ((page: Int) -> Unit)? = null

    init {
        mContext.swipeRefreshLayout {
            //下拉刷新加载监听
            setOnRefreshListener {
                MallManager.clearGoods()
                MallManager.clearNeed()
                resetPage()
                itemManager.refreshAll {
                    removeAll { it is RecItem }
                }
                et_mall_search.isCursorVisible = false
                //redo
                redo?.invoke(page)
                isRefreshing = false
            }

        }
        recView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = ItemAdapter(itemManager)
            //加载下一页监听
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (canScrollVertically(1) && page < totalPage) {
                        isLoading = true
                        //more
                        redo?.invoke(++page)
                    }
                    isLoading = false
                }
            })
        }
    }

    fun bindSale(data: List<LatestSale>) {
        totalPage = data[0].page
        itemManager.refreshAll {
            retainAll { it is RecItem }
            val items = mutableListOf<Item>().apply {
                for (i in 1 until data.size) {
                    recItem {
                        Glide.with(mContext)
                                .load("https://mall.twt.edu.cn/api.php/Upload/img_redirect?id=${data[i].imgurl}")
                                .into(image)
                        name.text = data[i].name
                        price.text = data[i].price
                        locate.text = MallManager.dealText(data[i].location)
                        card.setOnClickListener {
                            val intent = Intent(mContext, DetailActivity::class.java).putExtra("id", data[i].id)
                            mContext.startActivity(intent)
                        }
                    }
                }
            }
            addAll(0, items)
        }
    }

    fun bindNeed(data: List<LatestNeed>) {
        totalPage = data[0].page
        itemManager.refreshAll {
            retainAll { it is RecItem }
            val items = mutableListOf<Item>().apply {
                for (i in 1 until data.size) {
                    recItem {
                        name.text = data[i].name
                        price.text = data[i].price
                        locate.text = MallManager.dealText(data[i].location)
                        card.setOnClickListener {
                            val intent = Intent(mContext, DetailActivity::class.java).putExtra("id", data[i].id)
                            mContext.startActivity(intent)
                        }
                    }
                }
            }
            addAll(0, items)
        }
    }

    fun resetPage() {
        page = 1
        totalPage = 1
    }
}

fun addMainItem(itemView: View, redo: ((page: Int) -> Unit)?) = MainComponent(itemView).apply { this.redo = redo }