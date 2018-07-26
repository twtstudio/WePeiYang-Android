package com.example.lostfond2.search

import android.content.Context
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        search_progress = findViewById(R.id.search_progress)
        search_progress.visibility = View.GONE
        linearLayour = findViewById(R.id.search_no_res)//绑定的是activity_search的linearlayout
        linearLayour.visibility = View.GONE
        recyclerView = findViewById(R.id.search_searview)
        initValues()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.lf_waterfall_menu, menu)
        var searchItem: MenuItem = menu!!.findItem(R.id.waterfall_search)
        //通过MenuItem得到SearchView
        searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        searchView.setIconifiedByDefault(true)//搜索小图标位置在框外
        searchView.setSubmitButtonEnabled(true)
        //获得输入框
        val searchAutoComplete = searchView.findViewById(R.id.search_src_text) as SearchView.SearchAutoComplete
        searchView.setQueryHint("输入卡号/地点/物件名称");
        //设置输入框提示文字样式
        searchAutoComplete.setHintTextColor(getResources().getColor(android.R.color.darker_gray))
        searchAutoComplete.setTextColor(getResources().getColor(android.R.color.background_light))
        //设置搜索框有字时显示叉叉，无字时隐藏叉叉
        searchView.onActionViewExpanded()
        searchView.setIconified(true)

        //监听搜索框文字变化
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search_progress.visibility = View.VISIBLE
                waterfallBean.clear()
                keyword = query.toString()
                searchPresenter.loadSearchData(keyword, page)
                hideInputKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //可以做搜索提示功能
                return false
            }

        })


        return super.onCreateOptionsMenu(menu)

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

    public fun findMax(array: IntArray): Int {
        var maxNumber = array[0]
        val it = array.iterator()
        while (it.hasNext()) {
            val currentNumber = it.nextInt()
            if (currentNumber > maxNumber)
                maxNumber = currentNumber
        }
        return maxNumber
    }

    override fun setSearchData(waterfallBean: MutableList<MyListDataOrSearchBean>) {
        this.waterfallBean = waterfallBean
        waterfallTableAdapter.notifyDataSetChanged()
        if (waterfallBean.size === 0 && page == 1) {
            linearLayour.setVisibility(View.VISIBLE)
        } else {
            linearLayour.setVisibility(View.GONE)
        }
        search_progress.visibility = View.GONE
        isLoading = false
    }

    override fun onResume() {
        super.onResume()
    }
}