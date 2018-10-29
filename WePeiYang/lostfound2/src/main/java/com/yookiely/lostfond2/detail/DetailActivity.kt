package com.yookiely.lostfond2.detail

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Window
import com.example.lostfond2.R

import com.yookiely.lostfond2.service.DetailData
import com.yookiely.lostfond2.service.LostFoundService
import com.yookiely.lostfond2.service.Utils
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.ui.rec.withItems
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.lf2_activity_detail)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val bundle: Bundle = intent.extras
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val id = bundle.getInt("id")
        val recyclerView: RecyclerView = findViewById(R.id.lf_detail_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val recyclerViewImg : RecyclerView = findViewById(R.id.img_recycler)
        val ms = LinearLayoutManager(this)
        ms.orientation = LinearLayoutManager.HORIZONTAL
        recyclerViewImg.layoutManager = ms

        launch(UI + QuietCoroutineExceptionHandler) {
            val mylist: CommonBody<DetailData> = LostFoundService.getDetailed(id).await()
            if (mylist.error_code == -1) {
                if (mylist.data!!.type == 1) {
                    toolbar.title = "   捡到物品"
                } else {
                    toolbar.title = "   丢失物品"
                }

                val mylist1 = mylist.data!!

                recyclerViewImg.withItems{
                    if (mylist1.picture != null) {
                        for (i in mylist1.picture) {
                            setImage(i, this@DetailActivity)
                        }
                    } else {
                        setImage("julao.jpg", this@DetailActivity)
                    }
                }
                recyclerView.withItems {

                    setTitle("基本信息")
                    if (mylist1.title != null) {
                        setDetail("标题", mylist1.title, false)
                    } else {
                        setDetail("标题", "", false)
                    }
                    if (mylist1.type != null) {
                        setDetail("类型", Utils.getType(mylist1.detail_type), false)
                    } else {
                        setDetail("类型", "", false)
                    }
                    if (mylist1.time != null) {
                        setDetail("时间", mylist1.time, false)
                    } else {
                        setDetail("时间", "", false)
                    }
                    if (mylist1.detail_type != 2) {
                        if (mylist1.place != null) {
                            setDetail("地点", mylist1.place, true)
                        } else {
                            setDetail("地点", "", true)
                        }
                    } else {
                        if (mylist1.place != null) {
                            setDetail("地点", mylist1.place, false)
                        } else {
                            setDetail("地点", "", false)
                        }
                    }
                    if (mylist1.detail_type == 2) {
                        if (mylist1.card_name != "null" && mylist1.card_name != null) {
                            setDetail("失主姓名", mylist1.card_name, false)
                        } else {
                            if (Utils.getType(mylist1.detail_type) == "饭卡") {
                                setDetail("失主姓名", "", false)
                            }
                        }
                    }
                    if (mylist1.detail_type == 2) {
                        if (mylist1.card_number != "0" && mylist1.card_number != null) {
                            setDetail("卡号", mylist1.card_number, false)
                        } else {
                            if (Utils.getType(mylist1.detail_type) == "饭卡") {
                                setDetail("卡号", "", false)
                            }
                        }
                    }
                    if (mylist1.type != null && mylist1.type == Utils.TYPE_OF_FOUND) {
                        if (mylist1.recapture_place != null) {
                            setDetail("领取站点", mylist1.recapture_place + Utils.getExit(mylist1.recapture_entrance), true)
                        } else {
                            setDetail("领取站点", "无", true)
                        }
                    }

                    setTitle("联系信息")
                    if (mylist1.name != null) {
                        setDetail("姓名", mylist1.name, false)
                    }
                    if (mylist1.phone != null) {
                        setDetail("电话", mylist1.phone, true)
                    }


                    setTitle("附加信息")
                    if (mylist1.item_description != null ) {
                        setOther("附言", mylist1.item_description)
                    } else {
                        setOther("附言", "无")
                    }
                    setTitle("")

                }
            }
        }
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}