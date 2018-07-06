package com.twtstudio.service.dishesreviews.search.view

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.model.Food
import com.twtstudio.service.dishesreviews.search.model.SearchProvider
import com.twtstudio.service.dishesreviews.search.view.adapters.SearchResultAdapter

class SearchActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var etSearch: AutoCompleteTextView
    private lateinit var tvResult: TextView
    private var resultNo = 0
    private lateinit var rvResult: RecyclerView
    private var resultList: MutableList<Food> = mutableListOf()
    private lateinit var adapter: SearchResultAdapter
    private lateinit var arrayAdapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dishes_reviews_activity_search)
        toolbar = findViewById<Toolbar>(R.id.toolbar).apply {
            title = ""
        }
        setSupportActionBar(toolbar)
        getSupportActionBar()?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.dishes_reviews_ic_action_back)
        }
        etSearch = findViewById(R.id.et_search)
        tvResult = findViewById<TextView>(R.id.tv_result).apply {
            text = "搜索结果—共${resultNo}条"
        }
        arrayAdapter = ArrayAdapter<String>(this
                , android.R.layout.simple_dropdown_item_1line)
        etSearch.setAdapter(arrayAdapter)
        etSearch.setDropDownBackgroundResource(R.color.white)
        //TODO 数据量大了以后的性能问题
        //通过网络获取所有菜名
        SearchProvider.search(etSearch.text.toString()).bindNonNull(this@SearchActivity) {
            setFoodNameArray(it.searchResult) //初始化搜索建议
        }
        etSearch.setOnItemClickListener { adapterView, view, i, l ->
            search(etSearch.text.toString())
        }
        etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                etSearch.clearFocus();
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(etSearch.windowToken, 0)
                search(etSearch.text.toString())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        rvResult = findViewById<RecyclerView>(R.id.rv_result).apply {
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            this@SearchActivity.adapter = SearchResultAdapter(resultList, this@SearchActivity, this@SearchActivity)
            adapter = this@SearchActivity.adapter
        }


    }

    private fun setFoodNameArray(foodNameList: List<Food>) {
        arrayAdapter.clear()
        for (i in foodNameList.indices)
            arrayAdapter.add(foodNameList.get(i).food_name)
        arrayAdapter.notifyDataSetChanged()

    }

    private fun search(keyWord: String) {
        SearchProvider.search(keyWord).bindNonNull(this) {
            resultNo = it.searchResult.size
            tvResult.text = "搜索结果—共${resultNo}条"
            resultList.clear()
            resultList.addAll(it.searchResult)
            this@SearchActivity.adapter.notifyDataSetChanged()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dishes_reviews_menu_search_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
            R.id.menu_clear -> etSearch.setText("")
        }
        return super.onOptionsItemSelected(item)
    }

}