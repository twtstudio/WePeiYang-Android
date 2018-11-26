package com.tjuwhy.yellowpages2.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.tjuwhy.yellowpages2.R
import com.tjuwhy.yellowpages2.service.search
import com.tjuwhy.yellowpages2.utils.SEARCH_CONTENT_KEY
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.ui.rec.withItems
import es.dmoral.toasty.Toasty

class SearchResultActivity : AppCompatActivity() {

    private lateinit var keyWord: String
    private lateinit var arrowBackIv: ImageView
    private lateinit var keyWordTv: TextView
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.yp2_activity_search_result)
        keyWordTv = findViewById(R.id.search_keyword)
        keyWord = intent.getStringExtra(SEARCH_CONTENT_KEY)
        keyWordTv.text = keyWord
        recyclerView = findViewById(R.id.search_result_rv)
        recyclerView.layoutManager = LinearLayoutManager(this)

        arrowBackIv = findViewById(R.id.search_result_arrow_back)
        search(keyWord) { refreshState, beanList ->
            when (refreshState) {
                is RefreshState.Success -> {
                    recyclerView.withItems(beanList!!.map { SearchResultItem(this, it, keyWord) })
                    if (beanList.isEmpty()) {
                        Toasty.info(this, "未找到相关电话", Toast.LENGTH_SHORT).show()
                    }
                }
                is RefreshState.Failure -> {
                    Toasty.error(this, "出现了一些问题${refreshState.throwable}")
                }
            }
        }

        arrowBackIv.setOnClickListener {
            onBackPressed()
        }
    }
}
