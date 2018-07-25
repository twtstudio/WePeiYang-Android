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
import com.twt.wepeiyang.commons.ui.rec.withItems


class MylistFragement : Fragment(), MyListService.MyListView {


    lateinit var mylist_recyclerview: RecyclerView
    lateinit var mylist_progress: ProgressBar
    lateinit var mylist_nodata: LinearLayout
    var isLoading = false
    var needClear = false

    lateinit var tableAdapter: MylistTableAdapter
    lateinit var layoutManager: LinearLayoutManager
    var mylistBean: MutableList<MyListDataOrSearchBean> = ArrayList()//可能会有bug
    lateinit var lostOrFound: String
    val mylistPresenter: MyListService.MylistPresenter = MylistPresenterImpl(this)
    var page = 1

    companion object {
        fun newInstance(type: String): MylistFragement {
            val args = Bundle()
            args.putString("index", type)
            val fragment = MylistFragement()
            fragment.arguments = args
            return fragment
        }
    }




    override fun setMylistData(mylistBean: List<MyListDataOrSearchBean>) {
        if (needClear) {
            this.mylistBean.clear()
            tableAdapter.mylistBean.clear()
        }

//        this.mylistBean.message = mylistBean.message
        this.mylistBean.addAll(mylistBean)
        tableAdapter.mylistBean = (this.mylistBean)
        tableAdapter.notifyDataSetChanged()
        mylist_progress.visibility = View.GONE
        if (tableAdapter.mylistBean.size == 0 && page == 1) {
            mylist_nodata.visibility = View.VISIBLE
        } else {
            mylist_nodata.visibility = View.GONE
        }

        isLoading = false
        needClear = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.lf_fragment_mylist, container, false)
        mylist_recyclerview = view!!.findViewById(R.id.mylist_recyclerView)
        mylist_progress = view!!.findViewById(R.id.mylist_progress)
        mylist_nodata = view!!.findViewById(R.id.mylist_nodata)
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
        mylistBean = ArrayList()
        tableAdapter.notifyDataSetChanged()
        mylistPresenter.loadMylistData(lostOrFound, page)

    }

    fun initValues() {
        mylist_nodata.visibility = View.GONE
        mylist_progress.visibility = View.VISIBLE


        layoutManager = LinearLayoutManager(activity)
        mylist_recyclerview.layoutManager = layoutManager
        tableAdapter = MylistTableAdapter(mylistBean, activity, lostOrFound, this)
        mylist_recyclerview.adapter = tableAdapter

//        mylist_recyclerview.layoutManager = LinearLayoutManager(this)
//
//        mylist_recyclerview.withItems {
//            repeat(mylistBean.size) {
//                mylistload(activity, lostOrFound, mylistBean[it], this@MylistFragement)
//            }
//        }

    }


}