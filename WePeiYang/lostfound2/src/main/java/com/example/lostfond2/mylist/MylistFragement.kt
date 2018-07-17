package com.example.lostfond2.mylist

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.example.lostfond2.R
import com.example.lostfond2.service.MyListDataOrSearchBean
import com.twt.wepeiyang.commons.experimental.network.CommonBody


class MylistFragement : Fragment(), MyListService.MyListView {


    val mylist_recyclerview: RecyclerView = view!!.findViewById(R.id.mylist_recyclerView)
    val mylist_progress: ProgressBar = view!!.findViewById(R.id.mylist_progress)
    val mylist_nodata: LinearLayout = view!!.findViewById(R.id.mylist_nodata)
    var isLoading = false
    var needClear = false

    lateinit var tableAdapter: MylistTableAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var mylistBean: MutableList<MyListDataOrSearchBean>
    lateinit var lostOrFound: String
    val mylistPresenter: MyListService.MylistPresenter = MylistPresenterImpl(this)
    var page = 1

    fun newInstance(type: String): MylistFragement {
        val args = Bundle()
        args.putString("index", type)
        val fragment = MylistFragement()
        fragment.arguments = args
        return fragment
    }


    override fun setMylistData(mylistBean: List<MyListDataOrSearchBean>) {
        if (needClear) {
            this.mylistBean.clear()
        }

//        this.mylistBean.message = mylistBean.message
        this.mylistBean.addAll(mylistBean)
//        tableAdapter.notifyDataSetChanged()
        mylist_progress.visibility = View.GONE
        if (this.mylistBean.size == 0 && page == 1) {
            mylist_nodata.visibility = View.VISIBLE
        } else {
            mylist_nodata.visibility = View.GONE
        }

        isLoading = false
        needClear = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.lf_fragment_mylist, container, false)
        var bundle = arguments
        lostOrFound = bundle!!.getString("index")
        initValues()


        mylist_recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
                if (!isLoading && totalCount < (lastVisibleItem + 2)) {
                    ++page
                    isLoading = true
                    MylistPresenterImpl(this@MylistFragement).loadMylistData(lostOrFound, page)
                }
            }

        })

        return view
    }


    override fun turnStatus(id: Int) {
        mylistPresenter.turnStatus(id)
    }

    override fun turnStatusSuccessCallBack() {
        needClear = true
        mylistPresenter.loadMylistData(lostOrFound, 1)

    }

    override fun onResume() {
        super.onResume()
        page = 1
//        mylistBean.data = ArrayList()
        tableAdapter.notifyDataSetChanged()
        mylistPresenter.loadMylistData(lostOrFound, page)
    }

    fun initValues() {
        mylist_nodata.visibility = View.GONE
        mylist_progress.visibility = View.VISIBLE
//        mylistBean = MylistBean()   ***蜜汁bug
//        mylistBean.data = ArrayList()//可能会有bug
        layoutManager = LinearLayoutManager(activity)
        mylist_recyclerview.layoutManager = layoutManager
        tableAdapter = MylistTableAdapter(mylistBean, activity, lostOrFound, this)
        mylist_recyclerview.adapter = tableAdapter
    }


}