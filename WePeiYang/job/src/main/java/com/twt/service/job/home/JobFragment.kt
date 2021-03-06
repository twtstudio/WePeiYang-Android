package com.twt.service.job.home

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.twt.service.job.R
import com.twt.service.job.service.*
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import com.twt.wepeiyang.commons.ui.rec.withItems
import es.dmoral.toasty.Toasty

class JobFragment : Fragment(), JobHomeContract.JobHomeView {

    private lateinit var rootView: View
    lateinit var kind: String private set// 记录是四种类型中的哪一种
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var itemManager: ItemManager
    private val homePresenterImp: JobHomePresenterImp = JobHomePresenterImp(this)
    private var page: Int = 1
    private var isLoad: Boolean = false

    companion object {
        fun newInstance(kind: String): JobFragment {
            val args = Bundle()
            args.putString(ARG_KIND, kind)
            val jobFragment = JobFragment()
            jobFragment.arguments = args
            return jobFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kind = arguments!!.getString(ARG_KIND)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.job_fragment_home, container, false)
        initView()
        loadData()
        update()
        loadMoreData()
        return rootView
    }

    private fun initView() {
        recyclerView = rootView.findViewById(R.id.job_rv_homepage)
        linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        swipeRefreshLayout = rootView.findViewById<SwipeRefreshLayout>(R.id.job_sr_refresh).apply {
            setColorSchemeColors(Color.parseColor("#64a388"))
        }
    }

    private fun update() {
        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            loadData()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun loadMoreData() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var lastVisibleItem: Int = 0
            var totalCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                totalCount = linearLayoutManager.itemCount
                if (lastVisibleItem + 1 == totalCount && !isLoad) {
                    isLoad = true
                    ++page
                    loadData()
                }
            }
        })
    }

    private fun loadData() {
        if (page > pagesOfMsg) {
            itemManager.add(BottomItem())
        } else {
            homePresenterImp.getGeneral(kind, page)
        }
        isLoad = false
    }

    fun toTop(){
        recyclerView.smoothScrollToPosition(0)
    }

    override fun showHomeFair(commonBean: List<HomeDataL>) {
        recyclerView.withItems {
            repeat(commonBean.size) { i ->
                fair(commonBean[i], i == 0, this@JobFragment)
            }
        }
        itemManager = (recyclerView.adapter as ItemAdapter).itemManager
    }

    override fun showThree(dataRBean: List<HomeDataR>) {
        recyclerView.withItems {
            repeat(dataRBean.size) { i ->
                three(dataRBean[i], i == 0, this@JobFragment)
            }
        }
        itemManager = (recyclerView.adapter as ItemAdapter).itemManager
    }

    override fun loadMoreFair(commonBean: List<HomeDataL>) {
        repeat(commonBean.size) { i ->
            itemManager.add(FairItem(commonBean[i], false, this@JobFragment))
        }
    }

    override fun loadMoreOther(dataRBean: List<HomeDataR>) {
        repeat(dataRBean.size) { i ->
            itemManager.add(ThreeItem(dataRBean[i], false, this@JobFragment))
        }
    }

    override fun onError(msg: String) {
        context?.let { Toasty.error(it, msg, Toast.LENGTH_LONG, true).show() }
    }

    override fun onNull() {
        context?.let { Toasty.error(it, "没有相关内容", Toast.LENGTH_LONG, true).show() }
    }
}