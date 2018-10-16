package com.yookiely.lostfond2.search

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.*
import android.support.v7.widget.ListPopupWindow
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.lostfond2.R
import com.orhanobut.hawk.Hawk
import com.yookiely.lostfond2.waterfall.WaterfallPagerAdapter
import kotlinx.android.synthetic.main.lf2_activity_search.*
import org.jetbrains.anko.db.*

class SearchActivity : AppCompatActivity() {

    private lateinit var inputMethodManager: InputMethodManager
    private lateinit var searchView: SearchView
    private lateinit var toolbar: Toolbar
    private lateinit var popupWindow: ListPopupWindow
    private lateinit var lostFragment: SearchFragment
    private lateinit var foundFragment: SearchFragment
    private lateinit var search_pager_vp: ViewPager
    private lateinit var chooseTimeRecyclerView: RecyclerView//弹窗，选择时间的rv
    lateinit var chooseTimePopupWindow: PopupWindow
    private var layoutManagerForChooseTime = LinearLayoutManager(this@SearchActivity)

    lateinit var keyword: String
    var page = 1
    var campus: Int = 1
    private var time = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)//隐藏actionbar，需在setContentView前面
        setContentView(R.layout.lf2_activity_search)
        toolbar = findViewById(R.id.search_toolbar)
        setSupportActionBar(toolbar)
        searchView = findViewById(R.id.search_searview)
        lostFragment = SearchFragment.Companion.newInstance("lost")
        foundFragment = SearchFragment.Companion.newInstance("found")
        campus = Hawk.get("campus")//1 北洋园 ，2 卫津路

        val waterfallPagerAdapter = WaterfallPagerAdapter(supportFragmentManager)
        val popupwindowView = LayoutInflater.from(this).inflate(R.layout.lf_popupwindow_search, null, false)
        waterfallPagerAdapter.add(foundFragment, "捡到")
        waterfallPagerAdapter.add(lostFragment, "丢失")
        search_pager_vp = findViewById(R.id.search_pager_content)
        search_pager_vp.adapter = waterfallPagerAdapter
        search_tabLayout.apply {
            setupWithViewPager(search_pager_vp)
            tabGravity = TabLayout.GRAVITY_FILL
            setSelectedTabIndicatorColor(Color.parseColor("#00a1e9"))
        }
        search_type_blue.visibility = View.GONE

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
            onDetachedFromWindow()
        }

        searchView.setOnQueryTextFocusChangeListener { view, b ->
            if (b){
                val view = searchView
                showListPopupWindow(view, db)//初始化弹窗
                popupWindow.show()
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                keyword = query
                lostFragment.setKeyword(keyword)
                foundFragment.setKeyword(keyword)

                if(keyword != ""){
                    database(keyword,db)
                }

                hideInputKeyboard()
                onDetachedFromWindow()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        search_type.setOnClickListener {
            if (search_type_grey.visibility == View.VISIBLE) run {
                search_type_blue.visibility = View.VISIBLE
                search_type_grey.visibility = View.GONE

                chooseTimePopupWindow = PopupWindow(popupwindowView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
                chooseTimePopupWindow.apply {
                    setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@SearchActivity, R.color.white_color)))
                    isOutsideTouchable = true
                    isTouchable = true
                    showAsDropDown(it)
                    setTouchInterceptor(View.OnTouchListener { v, event ->
                        if (event?.action == MotionEvent.ACTION_DOWN) {
                            search_type_grey.visibility = View.VISIBLE
                            search_type_blue.visibility = View.GONE
                        }
                        false
                    })
                }

                chooseTimeRecyclerView = popupwindowView.findViewById(R.id.search_type_recyclerview)
                chooseTimeRecyclerView.adapter = SearchChooseTimeAdapter(this, time, chooseTimePopupWindow)
                chooseTimeRecyclerView.layoutManager = layoutManagerForChooseTime

            } else if (search_type_blue.visibility == View.VISIBLE) {
                search_type_grey.visibility = View.VISIBLE
                search_type_blue.visibility = View.GONE
            }
        }

        searchView.clearFocus()
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
                setAdapter(ArrayAdapter<String>(applicationContext, R.layout.lf_popupwindow_item, historyRecord))
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

    fun changeTime(time: Int) {
        this.time = time
        lostFragment.setTimeAndLoad(this.time)
        foundFragment.setTimeAndLoad(this.time)
    }
}