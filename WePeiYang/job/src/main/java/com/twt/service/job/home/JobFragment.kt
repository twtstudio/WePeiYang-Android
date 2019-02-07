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
import com.orhanobut.hawk.Hawk
import com.twt.service.job.R
import com.twt.service.job.service.*
import com.twt.wepeiyang.commons.ui.rec.withItems
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.experimental.delay

class JobFragment : Fragment(), JobHomeContract.JobHomeView {

    private lateinit var rootView: View
    private lateinit var kind: String// 记录是四种类型中的哪一种
    private var type: Int = 0
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var linearLayoutManager: LinearLayoutManager
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
        arguments?.apply {
            kind = getString(ARG_KIND)
            type = funs.getType(kind)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.job_fragment_home, container, false)
        initView()
        loadData(page)
        refresh()
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

    private fun loadData(page : Int) {
        if (page > Hawk.get<Int>(kind)) {
            cannotLoad()
        } else {
            homePresenterImp.getGeneral(kind, page)
        }
        isLoad = false
    }

    private fun refresh() {
        swipeRefreshLayout.setOnRefreshListener {
            loadData(1)// 得单独传1，刷新永远是第一页，其他地方的 page 就要用全局的，统一多上拉一次，page就+1
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
                if (lastVisibleItem + 3 >= totalCount && !isLoad) {
                    isLoad = true
                    Toasty.error(context!!, "begin", Toast.LENGTH_LONG, true).show()
                    loadData(++page)
                    Toasty.error(context!!, "end", Toast.LENGTH_LONG, true).show()
                }
            }
        })
    }

    override fun showHomeFair(commonBean: List<HomeDataL>) {
        recyclerView.withItems {
            repeat(commonBean.size) { i ->
                if (i == 0) fair(commonBean[i], true)
                else fair(commonBean[i], false)
            }
        }
    }

    override fun showThree(dataRBean: List<HomeDataR>) {
        recyclerView.withItems {
            repeat(dataRBean.size) { i ->
                if (i == 0) {
                    three(dataRBean[i], true)
                } else {
                    three(dataRBean[i], false)
                }
            }
        }
    }

    override fun loadMoreFair(commonBean: List<HomeDataL>) {
        recyclerView.withItems {
            repeat(commonBean.size) { i ->
                fair(commonBean[i], false)
            }
        }
    }

    override fun loadMoreOther(dataRBean: List<HomeDataR>) {
        recyclerView.withItems {
            repeat(dataRBean.size) { i ->
                three(dataRBean[i], false)
            }
        }
    }

    override fun onError(msg: String) {
        Toasty.error(context!!, msg, Toast.LENGTH_LONG, true).show()
    }

    override fun onNull() {
        Toasty.error(context!!, "meiyou", Toast.LENGTH_LONG, true).show()
    }

    override fun cannotLoad() {
        Toasty.error(context!!, "daodi", Toast.LENGTH_LONG, true).show()
    }
}