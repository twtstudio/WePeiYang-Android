package com.twtstudio.service.dishesreviews.search.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.TextView
import com.twtstudio.service.dishesreviews.R

class SearchActivity : AppCompatActivity(){
    private lateinit var toolbar: Toolbar
    private lateinit var searchView: SearchView
    private lateinit var tvResult: TextView
    private var resultNo = 0
    private lateinit var keyWord: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dishes_reviews_activity_search)
        toolbar=findViewById<Toolbar>(R.id.toolbar).apply {
            title = ""
        }
        setSupportActionBar(toolbar)
        getSupportActionBar()?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.dishes_reviews_ic_action_back)
        }
        searchView=findViewById<SearchView>(R.id.sv).apply {
            setIconified(false)
            setFocusableInTouchMode(true)
            setOnCloseListener {  true} //禁止关闭搜索框
//            setOnQueryTextListener(object:SearchView.OnQueryTextListener{
//                override fun onQueryTextSubmit(query: String?): Boolean {
//                    return false
//                }
//
//                override fun onQueryTextChange(newText: String?): Boolean {
//                    return false
//                }
//            })
//            keyWord
        }
        tvResult = findViewById<TextView>(R.id.tv_result).apply {
            text = "搜索结果—共${resultNo}条"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}