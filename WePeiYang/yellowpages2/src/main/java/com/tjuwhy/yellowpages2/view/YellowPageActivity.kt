package com.tjuwhy.yellowpages2.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import android.widget.Toast
import com.tjuwhy.yellowpages2.R
import com.tjuwhy.yellowpages2.service.*
import com.tjuwhy.yellowpages2.utils.ExpandableHelper
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.mta.mtaClick
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.coroutines.experimental.asReference

class YellowPageActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    private val groupArray = arrayOf("我的收藏", "校级部门", "院级部门", "其他部门")

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var itemManager: ItemManager
    lateinit var recyclerView: RecyclerView
    lateinit var searchIcon: ImageView
    private var groupCount = 0
    private var isRefreshing = false

    private val groupData = groupArray.map { GroupData(it, groupCount++) }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.yp2_activity_yellow_page)

        toolbar = findViewById(R.id.toolbar)
        searchIcon = findViewById(R.id.yellow_page_search)
        recyclerView = findViewById(R.id.phone_rv)
        swipeRefreshLayout = findViewById(R.id.yellow_page_srl)

        itemManager = ItemManager()
        recyclerView.adapter = ItemAdapter(itemManager)
        recyclerView.layoutManager = LinearLayoutManager(this)
        load()

        swipeRefreshLayout.setOnRefreshListener {
            isRefreshing = true
            load()
            isRefreshing = false
            swipeRefreshLayout.isRefreshing = false
        }

        searchIcon.setOnClickListener {
            mtaClick("yellowpage2_黄页搜索小图标searchIcon")
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun load() {
        val activity = this@YellowPageActivity.asReference()
        if (!isRefreshing) {
            Toasty.info(this, "正在加载", Toast.LENGTH_SHORT).show()
        }
        getUserCollection()
        getPhone { refreshState ->
            when (refreshState) {
                is RefreshState.Success -> {
                    val childData = mutableListOf<Array<SubData>>()
                    childData.add(YellowPagePreference.collectionList)
                    childData.addAll(YellowPagePreference.subArray)
                    ExpandableHelper(this, recyclerView, groupData.toTypedArray(), childData.toTypedArray())
                }
                is RefreshState.Failure -> {
                    if (YellowPagePreference.subArray.isNotEmpty()) {
                        val childData = mutableListOf<Array<SubData>>()
                        childData.add(YellowPagePreference.collectionList)
                        childData.addAll(YellowPagePreference.subArray)
                        ExpandableHelper(this, recyclerView, groupData.toTypedArray(), childData.toTypedArray())
                    }
                    Toasty.error(activity(), refreshState.throwable.toString()).show()
                }
            }
        }
    }
}
