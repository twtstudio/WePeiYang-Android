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
import android.widget.Toast
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import es.dmoral.toasty.Toasty

/**
 * Created by retrox on 2016/12/12.
 */
class NewsTwoFragment : Fragment() {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_news, container, false).apply {
            val adapter = NewsAdapter(context, this@NewsTwoFragment)
            swipeRefreshLayout = findViewById(R.id.news_swipeRefreshLayout)
            val recyclerView: RecyclerView = findViewById(R.id.news_recyclerview)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)

            newsListLiveData.bindNonNull(this@NewsTwoFragment) {
                Log.e("Test", it.toString())
                adapter.apply {
                    list.clear()
                    list.addAll(it)
                    notifyDataSetChanged()
                }
            }
            swipeRefreshLayout.setOnRefreshListener {
                newsListLiveData.refresh(CacheIndicator.REMOTE) {
                    when (it) {
                        is RefreshState.Success -> swipeRefreshLayout.isRefreshing = false
                        is RefreshState.Failure -> {
                            Toasty.error(context, "发生错误", Toast.LENGTH_SHORT).show()
                            swipeRefreshLayout.isRefreshing = false
                        }
                    }
                }
            }
        }
        return view
    }


    companion object {
        fun newInstance(): NewsTwoFragment = NewsTwoFragment().apply { arguments = Bundle() }
    }
}

