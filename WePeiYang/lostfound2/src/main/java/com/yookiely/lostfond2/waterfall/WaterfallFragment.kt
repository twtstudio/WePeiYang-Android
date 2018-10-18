package com.yookiely.lostfond2.waterfall

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.lostfond2.R
import com.orhanobut.hawk.Hawk
import com.yookiely.lostfond2.service.MyListDataOrSearchBean
import kotlinx.android.synthetic.main.lf_fragment_waterfall.*

class WaterfallFragment : Fragment(), WaterfallContract.WaterfallView {

    private lateinit var tableAdapter: WaterfallTableAdapter
    private val layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
    private var isLoading = false
    private var isRefresh = false
    private var campus = 0
    var beanList = ArrayList<MyListDataOrSearchBean>()
    var lostOrFound = "lost"
    var type = -1
    var page = 1
    var time = 5
    private val waterfallPresenter = WaterfallPresenterImpl(this)

    companion object {
        fun newInstance(type: String): WaterfallFragment {
            val args = Bundle()
            args.putString("index", type)
            val fragment = WaterfallFragment()
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.lf_fragment_waterfall, container, false)
        val waterfall_refresh = view.findViewById<SwipeRefreshLayout>(R.id.waterfall_refresh)
        val waterfall_recyclerView = view.findViewById<RecyclerView>(R.id.waterfall_recyclerView)
        val waterfall_no_res = view.findViewById<LinearLayout>(R.id.waterfall_no_res)

        if (Hawk.contains("campus")) {
            campus = Hawk.get("campus")
        }
        waterfall_recyclerView.layoutManager = layoutManager
        waterfall_no_res.visibility = View.GONE
        val bundle = arguments
        lostOrFound = bundle!!.getString("index")
        tableAdapter = WaterfallTableAdapter(beanList, this.activity!!, lostOrFound)
        waterfall_recyclerView.adapter = tableAdapter
        waterfall_refresh.setOnRefreshListener(this::refresh)

        waterfall_recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalCount = layoutManager.itemCount
                val lastPositions = IntArray(layoutManager.spanCount)
                layoutManager.findLastCompletelyVisibleItemPositions(lastPositions)
                val lastPosition = lastPositions.max()

                if (!isLoading && (totalCount < lastPosition!! + 2) && lastPosition != -1) {
                    page++
                    isLoading = true

                    waterfallPresenter.apply {
                        if (type == -1) {
                            loadWaterfallData(lostOrFound, page, time)
                        } else {
                            loadWaterfallDataWithCondition(lostOrFound, page, type, time)
                        }
                    }
                }
            }
        })

        return view
    }

    override fun setWaterfallData(newBeanList: List<MyListDataOrSearchBean>) {
        waterfall_no_res.apply {
            visibility = if (newBeanList.isEmpty() && page == 1) {
                View.VISIBLE
            } else {
                View.GONE
            }

            if (isRefresh) {
                beanList.clear()
            }

            beanList.addAll(newBeanList)
            tableAdapter.notifyDataSetChanged()
            waterfall_refresh.isRefreshing = false
            isLoading = false
            isRefresh = false
        }
    }

    override fun loadWaterfallDataWithCondition(type: Int, time: Int) {
        this.type = type
        this.time = time
        page = 1
        isRefresh = true
        waterfallPresenter.loadWaterfallDataWithCondition(lostOrFound, page, this.type, this.time)
    }

    override fun onResume() {
        super.onResume()

        if (campus != Hawk.get("campus")) {
            this.type = -1 //全部物品
            this.time = 5 // 全部时间
            campus = Hawk.get("campus")
            refresh()
        }
    }
    private fun refresh() {
        isLoading = true
        isRefresh = true
        page = 1
        waterfallPresenter.loadWaterfallDataWithCondition(lostOrFound, page, this.type, this.time)
    }
}