package com.yookiely.lostfond2.search

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.*
import android.support.v7.widget.ListPopupWindow
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.lostfond2.R
import com.yookiely.lostfond2.service.MyListDataOrSearchBean
import com.yookiely.lostfond2.waterfall.WaterfallTableAdapter
import org.jetbrains.anko.db.*

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
    private lateinit var popupWindow: ListPopupWindow

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

        database.use {
            createTable(HistoryRecordContract.TABLE_NAME,true,
                    Pair(HistoryRecordContract.ID, INTEGER + PRIMARY_KEY + AUTOINCREMENT),
                    Pair(HistoryRecordContract.CONTENT, TEXT))
        }
        val db = database.writableDatabase

        val tv: SearchView.SearchAutoComplete = searchView.findViewById(R.id.search_src_text)
        tv.setTextColor(Color.WHITE)
        tv.setHintTextColor(Color.WHITE)
        searchView.onActionViewExpanded()
        searchView.queryHint = "输入卡号/地点/物件名称"
        //searchView.setIconifiedByDefault(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        toolbar.setNavigationOnClickListener { view ->
            hideInputKeyboard()
            finish()
        }
        search_progress.visibility = View.GONE
        linearLayour.visibility = View.GONE
        initValues()

        searchView.setOnQueryTextFocusChangeListener { view, b ->
            if (b){
                val view = searchView
                showListPopupWindow(view,db)
                popupWindow.show()
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                search_progress.visibility = View.VISIBLE
                waterfallBean.clear()
                keyword = query

                if(keyword != ""){
                    database(keyword,db)
                }

                searchPresenter.loadSearchData(keyword, page)
                hideInputKeyboard()
                popupWindow.dismiss()
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
        searchView.clearFocus()
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

    private fun database(query:String,db:SQLiteDatabase){
        //对搜索历史的数据库处理
        var cursor = db.query("myTable", null,null,null,null,null,null)//cursor为游标

        if (cursor!= null){
            while (cursor.moveToNext()){
                val number = cursor.getColumnIndex("content")
                if(cursor.getString(number) == query){
                    db.delete("myTable","content=?", arrayOf(query))
                }
            }
        }

        val values = ContentValues()
        values.put("content",query)
        //times为存入的数据条数
        db.insert("myTable",null ,values) //插入数据
        cursor = db.query("myTable", null,null,null,null,null,null)//cursor为游标
        val times = cursor.count

        if(times>5){
            //超过5条，则删掉最后一条
            cursor.moveToFirst()
            val timeOfID = cursor.getColumnIndex("_id")
            var minID = cursor.getInt(timeOfID)
            db.delete("myTable","_id = {userID}", "userID" to minID)//arrayOf(min.toString()))
        }
    }

    private fun showListPopupWindow(view :View,db: SQLiteDatabase){
        var cursor = db.query("myTable", null,null,null,null,null,null)//cursor为游标
        if (cursor!=null){
            var historyRecord = cursor.parseList(object: RowParser<String>{
                override fun parseRow(columns: Array<Any?>): String {
                    return columns.get(cursor.getColumnIndex("content")) as String
                }
            }).reversed()
            popupWindow = ListPopupWindow(this)
            popupWindow.apply {
                //设置适配器
                setAdapter(ArrayAdapter<String>(applicationContext,R.layout.popupwindow_item,historyRecord))
                anchorView = view
                width = 855
                isModal = false//内部封装的是focused，设置成false才能是popupwindow不自动获取焦点
                setOnItemClickListener { parent, view, position, id ->
                    searchView.setQuery(historyRecord[position],true)
                }
                setDropDownGravity(Gravity.START)
                horizontalOffset = 28
                verticalOffset = 0
                setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@SearchActivity,R.color.white_color)))
                setOnDismissListener {
                    searchView.clearFocus()
                }
            }
        }
    }
}