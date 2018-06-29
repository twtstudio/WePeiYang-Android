package com.example.news2


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull

/**
 * Created by retrox on 2016/12/12.
 */
class NewsTwoFragment : Fragment() {
    lateinit var swipeRefreshLayout :SwipeRefreshLayout
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_news, container, false).apply {
            val adapter = NewsAdapter(context, this@NewsTwoFragment)
            swipeRefreshLayout = findViewById(R.id.news_swipeRefreshLayout)
            val recyclerView: RecyclerView = findViewById(R.id.news_recyclerview)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)

            newsRecyclerViewLiveData.bindNonNull(this@NewsTwoFragment) {
                Log.e("Test", it.toString())
                adapter.apply {
                    list.clear()
                    list.addAll(it)
                    notifyDataSetChanged()
                }
            }
            swipeRefreshLayout.setOnRefreshListener(object :SwipeRefreshLayout.OnRefreshListener{
                override fun onRefresh() {
                    newsRecyclerViewLiveData.refresh(CacheIndicator.REMOTE)
                    swipeRefreshLayout.setRefreshing(false)
                }
            })
        }
        return view
    }


    companion object {
        fun newInstance(): NewsTwoFragment = NewsTwoFragment().apply { arguments = Bundle() }
    }
}

