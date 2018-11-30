package com.yookiely.lostfond2.search

import android.content.Context
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.lostfond2.R
import com.yookiely.lostfond2.service.MyListDataOrSearchBean
import com.yookiely.lostfond2.service.Utils
import com.yookiely.lostfond2.waterfall.WaterfallTableAdapter

class SearchFragment : Fragment(), SearchContract.SearchUIView {
    private lateinit var tableAdapter: WaterfallTableAdapter
    private val searchLayoutManager = GridLayoutManager(activity, 2)
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var searchNoRes: LinearLayout
    private lateinit var refreshLayout: SwipeRefreshLayout
    private var isLoading = false
    private var isRefresh = false
    private var beanList = mutableListOf<MyListDataOrSearchBean>()//存放网络请求的数据
    var lostOrFound = "lost"
    var page = 1
    var time = Utils.ALL_TIME
    private var isSubmit = false// 提交过了才能继续分类
    private var keyword: String? = null// 搜索关键字

    private val searchPresenter = SearchPresenterImpl(this)

    companion object {
        fun newInstance(type: String): SearchFragment {
            val args = Bundle()
            args.putString(Utils.INDEX_KEY, type)
            val fragment = SearchFragment()
            fragment.arguments = args

            return fragment
        }
    }

    fun setTimeAndLoad(time: Int) {
        // 可根据时间分类搜索结果
        this.time = time
        if (isSubmit && keyword != null) {
            loadSearhDataWithTime(this.time)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.lf_fragment_waterfall, container, false)
        val searchRefresh = view.findViewById<SwipeRefreshLayout>(R.id.sr_waterfall_refresh)
        searchNoRes = view.findViewById(R.id.ll_waterfall_no_res)
        searchRecyclerView = view.findViewById(R.id.rv_waterfall_homepage)
        refreshLayout = view.findViewById(R.id.sr_waterfall_refresh)

        init()
        val bundle = arguments
        lostOrFound = bundle!!.getString(Utils.INDEX_KEY)
        searchRefresh.setOnRefreshListener(this::refresh)

        searchRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalCount = searchLayoutManager.itemCount
                val lastPositions = searchLayoutManager.findLastCompletelyVisibleItemPosition()

                if (!isLoading && (totalCount > lastPositions + 1)) {
                    page++
                    isLoading = true
                    searchPresenter.loadWaterfallDataWithTime(lostOrFound, keyword!!, page, time)
                }
            }
        })

        return view
    }

    private fun init() {
        searchNoRes.visibility = View.GONE
        searchRecyclerView.layoutManager = searchLayoutManager
        tableAdapter = WaterfallTableAdapter(beanList, this.activity!!, lostOrFound)
        searchRecyclerView.adapter = tableAdapter
        loadSearhDataWithTime(this.time)
    }

    private fun refresh() {
        isLoading = true
        isRefresh = true
        page = 1
        searchPresenter.loadWaterfallDataWithTime(lostOrFound, keyword!!, page, this.time)
    }

    override fun setSearchData(waterfallBean: List<MyListDataOrSearchBean>) {
        // 将获得数据处理之后给adapter

        if (isRefresh) {
            beanList.clear()
        }
        val dataBean: MutableList<MyListDataOrSearchBean> = mutableListOf()
        for (i in waterfallBean) {
            // 1是找到
            if (lostOrFound == "found" && i.type == 1) {
                dataBean.add(i)
            } else {
                if (i.type == 0 && lostOrFound == "lost") {
                    dataBean.add(i)
                }
            }
        }
        beanList.addAll(dataBean)
        tableAdapter.notifyDataSetChanged()
        refreshLayout.isRefreshing = false
        isLoading = false
        isRefresh = false
        searchNoRes.visibility = if (dataBean.size > 0) View.GONE else View.VISIBLE
    }

    override fun loadSearhDataWithTime(time: Int) {
        this.time = time
        page = 1
        isRefresh = true
        searchPresenter.loadWaterfallDataWithTime(lostOrFound, keyword!!, page, this.time)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val activity = context as SearchActivity
        this.keyword = activity.getKey()
        this.isSubmit = true
    }
}