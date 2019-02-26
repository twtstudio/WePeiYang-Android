package com.twt.service.job.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.twt.service.job.R
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.withItems
import  com.twt.service.job.service.*
import com.twt.wepeiyang.commons.ui.rec.ItemManager

class JobSearchHistoryFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.job_search_fragment, container, false)
        recyclerView = view.findViewById(R.id.job_search_rv_history)
        val linearLayout: LinearLayout = view.findViewById(R.id.job_search_no_res)
        linearLayout.visibility = View.GONE
        recyclerView.layoutManager = LinearLayoutManager(activity)
        if (searchHistory.size > 0) {
            searchHistory()
        }
        return view
    }

    fun searchHistory() {
        val sh = searchHistory.reversed()
        recyclerView.withItems {
            repeat(sh.size) { i ->
                addSH(sh[i], this@JobSearchHistoryFragment) {
                    (activity as JobSearchActivity).search(sh[i])
                }
            }
            addClear {
                searchHistory.clear()
                recyclerView.withItems(mutableListOf())
            }
        }

    }
}