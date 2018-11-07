package com.yookiely.lostfond2.search

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.*
import android.support.v7.widget.Toolbar
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.lostfond2.R
import com.orhanobut.hawk.Hawk
import com.yookiely.lostfond2.waterfall.WaterfallPagerAdapter

class SearchActivity : AppCompatActivity() {

    private var inputMethodManager: InputMethodManager? = null
    private lateinit var toolbar: Toolbar
    private lateinit var lostFragment: SearchFragment
    private lateinit var foundFragment: SearchFragment
    private lateinit var searchPager: ViewPager
    private lateinit var chooseTimeRecyclerView: RecyclerView//弹窗，选择时间的rv
    lateinit var chooseTimePopupWindow: PopupWindow
    private var layoutManagerForChooseTime = LinearLayoutManager(this@SearchActivity)
    private lateinit var searchTableLayout: TabLayout
    private lateinit var imageViewGrey: ImageView
    private lateinit var imageViewBlue: ImageView
    private lateinit var searchType: RelativeLayout
    private lateinit var textView: TextView

    lateinit var keyword: String
    var page = 1
    var campus: Int = 1
    private var time = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)//隐藏actionbar，需在setContentView前面
        setContentView(R.layout.lf2_activity_search)
        toolbar = findViewById(R.id.search_toolbar)
        textView = findViewById(R.id.lf2_search_result_tv)
        searchTableLayout = findViewById(R.id.search_tabLayout)
        imageViewBlue = findViewById(R.id.search_type_blue)
        imageViewGrey = findViewById(R.id.search_type_grey)
        searchType = findViewById(R.id.search_type)
        // listView = findViewById(R.id.lf2_search_hr_lv)
        lostFragment = SearchFragment.newInstance("lost")
        foundFragment = SearchFragment.newInstance("found")
        campus = Hawk.get("campus")//1 北洋园 ，2 卫津路

        val waterfallPagerAdapter = WaterfallPagerAdapter(supportFragmentManager)
        val popupWindowView = LayoutInflater.from(this).inflate(R.layout.lf2_popupwindow_search, null, false)
        waterfallPagerAdapter.add(foundFragment, "捡到")
        waterfallPagerAdapter.add(lostFragment, "丢失")
        searchPager = findViewById(R.id.search_pager_content)
        searchPager.adapter = waterfallPagerAdapter
        searchTableLayout.apply {
            setupWithViewPager(searchPager)
            tabGravity = TabLayout.GRAVITY_FILL
            setSelectedTabIndicatorColor(Color.parseColor("#00a1e9"))
        }
        imageViewBlue.visibility = View.GONE

        //将上一个activity的数据取出来用
        val bundle = intent.extras
        keyword = bundle.getString("query")
        textView.text = keyword
        lostFragment.setKeyword(keyword)
        foundFragment.setKeyword(keyword)
        hideInputKeyboard()
        onDetachedFromWindow()

//        val tv: SearchView.SearchAutoComplete = searchView.findViewById(R.id.search_src_text)
//        tv.setTextColor(Color.WHITE)
//        tv.setHintTextColor(Color.parseColor("#a3d3f4"))
//        searchView.onActionViewExpanded()
//        searchView.queryHint = "输入卡号/地点/物件名称"
        //searchView.setIconifiedByDefault(false)
        // supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        toolbar.setNavigationOnClickListener {
            hideInputKeyboard()
            finish()
            onDetachedFromWindow()
        }
        textView.setOnClickListener {
            hideInputKeyboard()
            val intent = Intent()
            intent.setClass(this@SearchActivity, SearchInitActivity::class.java)
            startActivity(intent)
        }


//        searchView.clearFocus()
//        searchView.setOnQueryTextFocusChangeListener { view, b ->
//            if (b) {
//                showListPopupWindow(view, db)//初始化弹窗
//                popupWindow.show()
//            }
//        }

//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                keyword = query
//                lostFragment.setKeyword(keyword)
//                foundFragment.setKeyword(keyword)
//
//                if(keyword != ""){
//                    database(keyword,db)
//                }
//                hideInputKeyboard()
//                onDetachedFromWindow()
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                return false
//            }
//        })

        searchType.setOnClickListener {
            if (imageViewGrey.visibility == View.VISIBLE) run {
                imageViewBlue.visibility = View.VISIBLE
                imageViewGrey.visibility = View.GONE

                chooseTimePopupWindow = PopupWindow(popupWindowView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
                chooseTimePopupWindow.apply {
                    setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@SearchActivity, R.color.white_color)))
                    isOutsideTouchable = true
                    isTouchable = true
                    showAsDropDown(it)
                    setTouchInterceptor { _, event ->
                        if (event?.action == MotionEvent.ACTION_DOWN) {
                            imageViewGrey.visibility = View.VISIBLE
                            imageViewBlue.visibility = View.GONE
                        }
                        false
                    }
                }

                chooseTimeRecyclerView = popupWindowView.findViewById(R.id.search_type_recyclerview)
                chooseTimeRecyclerView.apply {
                    adapter = SearchChooseTimeAdapter(this@SearchActivity, time, chooseTimePopupWindow)
                    layoutManager = layoutManagerForChooseTime
                }

            } else if (imageViewBlue.visibility == View.VISIBLE) {
                imageViewGrey.visibility = View.VISIBLE
                imageViewBlue.visibility = View.GONE
            }
        }

        //searchView.clearFocus()
    }

    private fun hideInputKeyboard() {
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputMethodManager != null) {
            val v = this@SearchActivity.currentFocus ?: return
            inputMethodManager!!.hideSoftInputFromWindow(v.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS)
            //searchView.clearFocus()
        }
    }


//    private fun showListPopupWindow(view :View,db: SQLiteDatabase){
//        popupWindow = ListPopupWindow(this)
//        val cursor = db.query("myTable", null, null, null, null, null, null)//cursor为游标
//
//        if (cursor!=null){
//            canshow = true
//            val historyRecord = cursor.parseList(object : RowParser<String> {
//                override fun parseRow(columns: Array<Any?>): String {
//                    return columns[cursor.getColumnIndex("content")] as String
//                }
//            }).reversed()
//            cursor.close()
//            popupWindow.apply {
//                //设置适配器
//                setAdapter(ArrayAdapter<String>(applicationContext, R.layout.lf2_search_hr_rv_item, historyRecord))
//                anchorView = view
//                width = 855
//                isModal = false//内部封装的是focused，设置成false才能是popupwindow不自动获取焦点
//                setOnItemClickListener { _, _, position, _ ->
//                    searchView.setQuery(historyRecord[position],true)
//                }
//                setDropDownGravity(Gravity.START)
//                horizontalOffset = 28
//                verticalOffset = 0
//                setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@SearchActivity,R.color.white_color)))
//                setOnDismissListener {
//                    searchView.clearFocus()
//                }
//            }
//        }
//    }

    fun changeTime(time: Int) {
        this.time = time
        lostFragment.setTimeAndLoad(this.time)
        foundFragment.setTimeAndLoad(this.time)
    }
}