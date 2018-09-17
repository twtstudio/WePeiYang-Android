package com.example.lostfond2.detail

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Window
import com.example.lostfond2.R
import com.example.lostfond2.service.DetailData
import com.example.lostfond2.service.LostFoundService
import com.example.lostfond2.service.Utils
import com.tencent.bugly.proguard.x
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import com.twt.wepeiyang.commons.ui.rec.withItems
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_detail)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = "物品详情"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        val bundle: Bundle = intent.extras
        val id = bundle.getInt("id")
        val recyclerView: RecyclerView = findViewById(R.id.detail_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val recyclerViewImg : RecyclerView = findViewById(R.id.img_recycler)
        val ms = LinearLayoutManager(this)
        ms.orientation = LinearLayoutManager.HORIZONTAL
        recyclerViewImg.layoutManager = ms

        launch(UI + QuietCoroutineExceptionHandler) {
            val mylist: CommonBody<DetailData> = LostFoundService.getDetailed(id).await()
            if (mylist.error_code == -1) {
                val mylist1 = mylist.data!!

                recyclerViewImg.withItems{
                    if (mylist1.picture != null) {
                        var piclist : List<String> =  mylist1.picture.split(",")
                        for (i in piclist){
                            setImage(i, this@DetailActivity)
                        }
                    } else {
                        setImage("julao.jpg", this@DetailActivity)
                    }
                }
                recyclerView.withItems {

                    setTitle("基本信息")
                    if (mylist1.title != null) {
                        setDetail("标题", mylist1.title)
                    }
                    if (mylist1.type != null) {
                        setDetail("类型", Utils.getType(mylist1.detail_type))
                    }
                    if (mylist1.time != null) {
                        setDetail("时间", mylist1.time)
                    }
                    if (mylist1.place != null) {
                        setDetail("地点", mylist1.place)
                    }
                    if (mylist1.card_name != "null" &&  mylist1.card_name != null) {
                        setDetail("失主姓名", mylist1.card_name)
                    }
                    if (mylist1.card_number != "0" && mylist1.card_number != null  ) {
                        setDetail("卡号", mylist1.card_number)
                    }
                    if (mylist1.recapture_place != null) {
                        setDetail("领取站点", mylist1.recapture_place+Utils.getExit(mylist1.recapture_entrance))
                    }
                    setTitle("联系信息")
                    if (mylist1.name != null) {
                        setDetail("姓名", mylist1.name)
                    }
                    if (mylist1.phone != null) {
                        setDetail("电话", mylist1.phone)
                    }


                    setTitle("附加信息")
                    if (mylist1.item_description != null ) {
                        setOther("附言", mylist1.item_description)
                    }

                }
            }


        }

    }
}
