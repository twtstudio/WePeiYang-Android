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
import com.avarye.mall.service.saleLiveData
import com.bumptech.glide.Glide
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.mall_fragment_latest_sale.view.*

class MallSaleFragment : Fragment() {
    private var page = 1
    private var totalPage = 1
    private var isLoading = false
    private val itemManager = ItemManager()
    private val viewModel = ViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.mall_fragment_latest_sale, container, false)

        view.srl_main_sale.apply {
            //下拉刷新加载监听
            setOnRefreshListener {
                if (!isLoading) {
                    isLoading = true
                    resetPage()
                    itemManager.removeAll { it is RecItem }
                    //redo
                    viewModel.getLatestSale(page)
                    isRefreshing = false
                    Toasty.info(CommonContext.application, "已刷新").show()
                }
                isLoading = false
            }
        }

        view.rv_main_sale.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = ItemAdapter(itemManager)
            //加载下一页监听
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!canScrollVertically(1) && page < totalPage && !isLoading) {
                        isLoading = true
                        //more
                        viewModel.getLatestSale(++page)
                    }
                    isLoading = false
                }
            })
        }
        viewModel.login()
        bindSale()
        return view
    }

    private fun bindSale() {
        saleLiveData.bindNonNull(this) { list ->
            //            MallManager.addSale(list)
//            val data = MallManager.getSale()
            totalPage = list[0].page
            val items = mutableListOf<Item>().apply {
                for (i in 1 until list.size) {
                    recItem {
                        Glide.with(this@MallSaleFragment)
                                .load("https://mall.twt.edu.cn/api.php/Upload/img_redirect?id=${list[i].imgurl}")
                                .into(image)
                        name.text = list[i].name
                        price.text = list[i].price
                        locate.text = MallManager.dealText(list[i].location)
                        card.setOnClickListener {
                            val intent = Intent(this@MallSaleFragment.context, DetailActivity::class.java).putExtra("id", list[i].id)
                            this@MallSaleFragment.startActivity(intent)
                        }
                    }
                }
            }
            itemManager.addAll(items)
        }

    }

    private fun resetPage() {
        page = 1
        totalPage = 1
    }
}