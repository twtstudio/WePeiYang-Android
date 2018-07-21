package com.example.lostfond2.detail

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Window
import com.example.lostfond2.R
import com.example.lostfond2.service.LostFoundService
import com.example.lostfond2.service.Utils
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.ui.rec.withItems
import kotlinx.coroutines.experimental.async

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_detail)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = "物品详情"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val bundle : Bundle = intent.extras
        var id = bundle.getInt("id")
        var lostOrFound = bundle.getString("lostOrFound")
        val recyclerView: RecyclerView = findViewById(R.id.detail_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        async {
            val mylist = LostFoundService.getDetailed(id).awaitAndHandle { it.printStackTrace() }?.data
                ?: throw IllegalStateException("详情拉取失败")
            recyclerView.withItems{
                setImage(mylist.picture,this@DetailActivity)
                setTitle("基本信息")
                setDetail("标题",mylist.title)
                setDetail("类型",Utils.getType(mylist.type))
                setDetail("时间",mylist.time)
                setDetail("地点",mylist.place)
                setDetail("失主姓名",mylist.card_name)
                setDetail("卡号",mylist.card_number)
                setDetail("领取站点",mylist.place)
                setTitle("联系信息")
                setDetail("姓名",mylist.name)
                setDetail("电话",mylist.phone)
                setTitle("附加信息")
                setDetail("附言",mylist.item_description)


            }
        }




    }
}
