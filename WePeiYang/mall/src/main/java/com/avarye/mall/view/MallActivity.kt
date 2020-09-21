package com.twt.service.mall.view

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.widget.EditText
import android.widget.ImageView
import com.twt.service.R
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences

import kotlinx.android.synthetic.main.mall_activity_main.*
import kotlinx.android.synthetic.main.mall_content_main.*
import kotlinx.android.synthetic.main.mall_item_header.*

class MallActivity2 : AppCompatActivity() {
    private val mallUrl = "https://mall.twt.edu.cn/api.php/Login/wpyLogin?model=2"
    private val headerKey = "Authorization"
    private val headerToken = "Bearer{${CommonPreferences.token}}"

    lateinit var editText : EditText
    lateinit var searchImg: ImageView
    lateinit var viewPager: ViewPager
    lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mall_activity_main)
//        setSupportActionBar(tb_main)
        login()
        init()

    }

    fun login() {
        val map = HashMap<String, String>()
        map[headerKey] = headerToken
    }

    fun init() {
        editText = et_mall_search
        searchImg = iv_mall_search
        viewPager = mall_vp_header
        recyclerView = mall_rv_main

        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    fun onSearch(){

    }

}



/*
fab_mine.setOnClickListener { view ->
    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
}
*/
