package com.example.lostfond2.search

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.*
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.example.lostfond2.R
import com.example.lostfond2.service.MyListDataOrSearchBean
import com.example.lostfond2.waterfall.WaterfallTableAdapter


class SearchActivity : AppCompatActivity(), SearchContract.SearchUIView {
    private lateinit var inputMethodManager: InputMethodManager
    private lateinit var waterfallTableAdapter: WaterfallTableAdapter
    private lateinit var waterfallBean: MutableList<MyListDataOrSearchBean>
    private lateinit var layoutManager: StaggeredGridLayoutManager
    private lateinit var searchPresenter: SearchContract.SearchPresenter
    private lateinit var searchView: SearchView
    private lateinit var toolbar: Toolbar
    private lateinit var search_progress: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayour: LinearLayout

    lateinit var keyword: String
    var page = 1
    var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)//隐藏actionbar，需在setContentView前面
        setContentView(R.layout.activity_search)
        toolbar = findViewById(R.id.search_toolbar)
        setSupportActionBar(toolbar)
        search_progress = findViewById(R.id.search_progress)
        linearLayour = findViewById(R.id.search_no_res)//绑定的是activity_search的linearlayout
        recyclerView = findViewById(R.id.search_recyclerView)
        searchView = findViewById(R.id.search_searview)

        var x = 1
        val tv: SearchView.SearchAutoComplete = searchView.findViewById(R.id.search_src_text)
        tv.setTextColor(Color.WHITE)
        tv.setHintTextColor(Color.WHITE)
        searchView.onActionViewExpanded()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        toolbar.setNavigationOnClickListener { view ->
            hideInputKeyboard()
            finish()
        }
        search_progress.visibility = View.GONE
        linearLayour.visibility = View.GONE
        initValues()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                search_progress.visibility = View.VISIBLE
                waterfallBean.clear()
                keyword = query

                searchPresenter.loadSearchData(keyword, page)
                hideInputKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalCount = layoutManager.itemCount
                val lastPositions = IntArray(layoutManager.spanCount)
                layoutManager.findLastCompletelyVisibleItemPositions(lastPositions)

                val lastPosition = findMax(lastPositions)
                if (!isLoading && totalCount < lastPosition + 2 && lastPosition != -1) {
                    ++page
                    isLoading = true
                    searchPresenter.loadSearchData(keyword, page)

                }
            }
        })

    }




    private fun initValues() {
        waterfallBean = mutableListOf()
        waterfallTableAdapter = WaterfallTableAdapter(waterfallBean, this, "unknown")
        layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recyclerView.setLayoutManager(layoutManager)
        recyclerView.setAdapter(waterfallTableAdapter)
        searchPresenter = SearchPresenterImpl(this)//传啥进去？
    }

    private fun hideInputKeyboard() {
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager != null) {
            val v = this@SearchActivity.currentFocus
            if (v == null) return
            inputMethodManager.hideSoftInputFromWindow(v.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS)
            searchView.clearFocus()
        }

    }

    fun findMax(array: IntArray): Int {
        var maxNumber = array[0]
        val it = array.iterator()
        while (it.hasNext()) {
            val currentNumber = it.nextInt()
            if (currentNumber > maxNumber)
                maxNumber = currentNumber
        }
        return maxNumber
    }

    override fun setSearchData(waterfallBean: List<MyListDataOrSearchBean>) {
        this.waterfallBean.addAll(waterfallBean)
        waterfallTableAdapter.waterFallBean = this.waterfallBean
        waterfallTableAdapter.notifyDataSetChanged()
        if (waterfallBean.size === 0 && page == 1) {
            linearLayour.setVisibility(View.VISIBLE)
        } else {
            linearLayour.setVisibility(View.GONE)
        }
        search_progress.visibility = View.GONE
        isLoading = false
    }
}