package com.twtstudio.retrox.tjulibrary.search

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.support.v7.widget.SearchView

import com.twtstudio.retrox.tjulibrary.R
import com.twtstudio.retrox.tjulibrary.tjulibservice.*

class SearchActivity : AppCompatActivity(), BookProvide.setBook {

    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var imageView: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var inputMethodManager: InputMethodManager
    private lateinit var searchBook: MutableList<SearchBook>
    private lateinit var getbook: JsoupService
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var layoutManager: StaggeredGridLayoutManager

    lateinit var keyword: String
    var page = 1
    var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)//隐藏actionbar，需在setContentView前面
        setContentView(R.layout.lib_activity_search)
        var toolbar = findViewById<Toolbar>(R.id.search_toolbar).also {
            it.setBackgroundColor(Color.parseColor("#e78fae"))

            setSupportActionBar(it)
        }
        window.statusBarColor = Color.parseColor("#e78fae")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        progressBar = findViewById(R.id.search_progressbar)
        imageView = findViewById(R.id.search_no_result)
        recyclerView = findViewById(R.id.search_recyclerview)
        searchView = findViewById(R.id.search_searview)//绑定所有控件


        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)给左上角添加返回小图标

        val tv: android.support.v7.widget.SearchView.SearchAutoComplete = searchView.findViewById(R.id.search_src_text)
        tv.setHintTextColor(Color.parseColor("#973d6a"))
        searchView.onActionViewExpanded()
        searchView.queryHint = "输入书名"
        toolbar.setNavigationOnClickListener { onBackPressed() }
        toolbar.setNavigationOnClickListener {
            hideInputKeyboard()
            finish()
        }
        progressBar.visibility = View.GONE
        imageView.visibility = View.GONE
        initValues()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                progressBar.visibility = View.VISIBLE
                searchBook.clear()
                keyword = query
                getbook.getSearch(keyword, page, this@SearchActivity)
                ++page
                hideInputKeyboard()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
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
                    getbook.getSearch(keyword, page, this@SearchActivity)
                }
            }
        })

    }

    private fun hideInputKeyboard() {
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager != null) {
            val v = this@SearchActivity.currentFocus ?: return
            inputMethodManager.hideSoftInputFromWindow(v.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    private fun initValues() {
        searchBook = mutableListOf()
        searchAdapter = SearchAdapter(searchBook, this)
        layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = searchAdapter
        getbook = JsoupService(this)
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

    override fun setSearchBook(booklist: ArrayList<SearchBook>) {
        this.searchBook.addAll(booklist)
        searchAdapter.searchBook = this.searchBook
        searchAdapter.notifyDataSetChanged()
        if (searchBook.size === 0 && page == 1) {
            imageView.visibility = View.VISIBLE
        } else {
            imageView.visibility = View.GONE
        }
        progressBar.visibility = View.GONE
        isLoading = false
    }
}