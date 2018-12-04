package com.yookiely.lostfond2.mylist

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import com.example.lostfond2.R
import com.yookiely.lostfond2.service.MyListDataOrSearchBean
import com.yookiely.lostfond2.service.Utils
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.support.v4.toast


class MyListFragment : Fragment(), MyListService.MyListView {

    private lateinit var myListRecyclerView: RecyclerView
    private lateinit var myListProgressBar: ProgressBar
    private lateinit var myListNoData: LinearLayout
    var isLoading = false
    private var needClear = false
    private lateinit var tableAdapter: MyListTableAdapter
    lateinit var myListLayoutManager: LinearLayoutManager
    private var myListBean: MutableList<MyListDataOrSearchBean> = mutableListOf()// 可能会有bug
    lateinit var lostOrFound: String
    private val myListPresenter: MyListService.MyListPresenter = MyListPresenterImpl(this)
    var page = 1

    companion object {
        fun newInstance(type: String): MyListFragment {
            val args = Bundle()
            args.putString(Utils.INDEX_KEY, type)
            val fragment = MyListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setMyListData(myListBean: List<MyListDataOrSearchBean>) {
        if (page==1 || needClear ) this.myListBean.clear()
        this.myListBean.addAll(myListBean)
        tableAdapter.notifyDataSetChanged()
        myListProgressBar.visibility = View.GONE
        myListNoData.visibility = if (tableAdapter.myListBean.size == 0 && page == 1) View.VISIBLE else View.GONE
        isLoading = false
        needClear = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.lf2_fragment_mylist, container, false)
        myListRecyclerView = view.findViewById(R.id.rv_mylist_recyclerView)
        myListProgressBar = view.findViewById(R.id.pb_mylist_progress)
        myListNoData = view.findViewById(R.id.ll_mylist_nodata)
        val bundle = arguments
        if (bundle == null ){
            Toasty.error(this.context!!,"没有拿到数据",Toast.LENGTH_SHORT,true).show()
        }else{
            lostOrFound = bundle.getString(Utils.INDEX_KEY)
        }
        initValues()

        myListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalCount = myListLayoutManager.itemCount
                val lastVisibleItem = myListLayoutManager.findLastCompletelyVisibleItemPosition()
                if (!isLoading && totalCount < (lastVisibleItem + 2) && totalCount > 4) {
                    ++page
                    isLoading = true
                    MyListPresenterImpl(this@MyListFragment).loadMyListData(lostOrFound, page)
                }
            }

        })

        return view
    }

    override fun onResume() {
        super.onResume()
        if (Utils.needRefresh){
            page = 1
            myListPresenter.loadMyListData(lostOrFound,page)
            if (lostOrFound == "lost") Utils.needRefresh = false
        }
    }

    override fun turnStatus(id: Int) {
        myListPresenter.turnStatus(id)
    }

    override fun turnStatusSuccessCallBack() {
        needClear = true
        myListPresenter.loadMyListData(lostOrFound, 1)
    }

    private fun initValues() {
        myListNoData.visibility = View.GONE
        myListProgressBar.visibility = View.VISIBLE

        myListLayoutManager = object : LinearLayoutManager(activity) {
            override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        myListLayoutManager.orientation = LinearLayoutManager.VERTICAL
        tableAdapter = MyListTableAdapter(myListBean, activity, lostOrFound, this)
        myListRecyclerView.apply {
            layoutManager = myListLayoutManager
            adapter = tableAdapter
        }
        myListPresenter.loadMyListData(lostOrFound, page)
    }
}
