package com.avarye.mall.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avarye.mall.R
import com.avarye.mall.detail.DetailActivity
import com.avarye.mall.service.MallManager
import com.avarye.mall.service.ViewModel
import com.avarye.mall.service.needLiveData
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.mall_fragment_latest_need.view.*

class MallNeedFragment : Fragment() {
    private var page = 1
    private var totalPage = 1
    private var isLoading = false
    private val itemManager = ItemManager()
    private val viewModel = ViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.mall_fragment_latest_need, container, false)
        view.srl_main_need.apply {
            setColorSchemeResources(R.color.mallColorMain)
            //下拉刷新加载监听
            setOnRefreshListener {
                if (!isLoading) {
                    isLoading = true
                    resetPage()
                    itemManager.removeAll { it is RecItem }
                    //redo
                    viewModel.getLatestNeed(page)
                    isRefreshing = false
                    Toasty.success(context, "已刷新").show()
                }
                isLoading = false
            }
        }

        view.rv_main_need.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = ItemAdapter(itemManager)
            //加载下一页监听
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!canScrollVertically(1) && page < totalPage) {
                        isLoading = true
                        //more
                        viewModel.getLatestNeed(++page)
                    }
                    isLoading = false
                }
            })
        }
        viewModel.getLatestNeed(page)
        bindNeed()
        return view
    }

    private fun bindNeed() {
        needLiveData.bindNonNull(this) { list ->
            totalPage = list[0].page
            val items = mutableListOf<Item>().apply {
                for (i in 1 until list.size) {
                    recItem {
                        name.text = list[i].name
                        price.text = list[i].price
                        locate.text = MallManager.dealText(list[i].location)
                        card.setOnClickListener {
                            val intent = Intent(this@MallNeedFragment.context, DetailActivity::class.java).putExtra("id", list[i].id)
                            this@MallNeedFragment.startActivity(intent)
                        }
                    }
                }
            }
            itemManager.removeAll { it is RecItem }
            itemManager.addAll(items)
        }
    }

    private fun resetPage() {
        page = 1
        totalPage = 1
    }
}