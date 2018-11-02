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
        val detailRecyclerViewImg: RecyclerView = findViewById(R.id.img_recycler)
        val detailLayoutManager = LinearLayoutManager(this)
        detailLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        detailRecyclerViewImg.layoutManager = detailLayoutManager

        launch(UI + QuietCoroutineExceptionHandler) {
            val myList: CommonBody<DetailData> = LostFoundService.getDetailed(id).await()
            if (myList.error_code == -1) {
                if (myList.data!!.type == 1) {
                    toolbar.title = "   捡到物品"
                } else {
                    toolbar.title = "   丢失物品"
                }

                val myListDetailData = myList.data!!

                detailRecyclerViewImg.withItems {
                    if (myListDetailData.picture != null) {
                        for (i in myListDetailData.picture) {
                            setImage(i, this@DetailActivity)
                        }
                    } else {
                        setImage("julao.jpg", this@DetailActivity)
                    }
                }
                recyclerView.withItems {

                    //最后一个参数设置成true，则该detail下面灭有分割线
                    setTitle("基本信息")
                    if (myListDetailData.title != null) {
                        setDetail("标题", myListDetailData.title, false)
                    } else {
                        setDetail("标题", "", false)
                    }
                    if (myListDetailData.type != null) {
                        setDetail("类型", Utils.getType(myListDetailData.detail_type), false)
                    } else {
                        setDetail("类型", "", false)
                    }
                    if (myListDetailData.time != null) {
                        setDetail("时间", myListDetailData.time, false)
                    } else {
                        setDetail("时间", "", false)
                    }
                    if (myListDetailData.detail_type != 2 && myListDetailData.type != 1) {
                        if (myListDetailData.place != null) {
                            setDetail("地点", myListDetailData.place, true)
                        } else {
                            setDetail("地点", "", true)
                        }
                    } else {
                        if (myListDetailData.place != null) {
                            setDetail("地点", myListDetailData.place, false)
                        } else {
                            setDetail("地点", "", false)
                        }
                    }
                    if (myListDetailData.detail_type == 2) {
                        if (myListDetailData.card_name != "null" && myListDetailData.card_name != null) {
                            setDetail("失主姓名", myListDetailData.card_name, false)
                        } else {
                            if (Utils.getType(myListDetailData.detail_type) == "饭卡") {
                                setDetail("失主姓名", "", false)
                            }
                        }
                    }
                    if (myListDetailData.detail_type == 2) {
                        if (myListDetailData.card_number != "0" && myListDetailData.card_number != null) {
                            setDetail("卡号", myListDetailData.card_number, false)
                        } else {
                            if (Utils.getType(myListDetailData.detail_type) == "饭卡") {
                                setDetail("卡号", "", false)
                            }
                        }
                    }
                    if (myListDetailData.type != null && myListDetailData.type == Utils.TYPE_OF_FOUND) {
                        if (myListDetailData.recapture_place != null) {
                            setDetail("领取站点", Utils.getGarden(myListDetailData.recapture_place) + myListDetailData.recapture_place + Utils.getExit(myListDetailData.recapture_entrance), true)
                        } else {
                            setDetail("领取站点", "无", true)
                        }
                    }

                    setTitle("联系信息")
                    if (myListDetailData.name != null) {
                        setDetail("姓名", myListDetailData.name, false)
                    }
                    if (myListDetailData.phone != null) {
                        setDetail("电话", myListDetailData.phone, true)
                    }

                    setTitle("附加信息")
                    if (myListDetailData.item_description != null) {
                        setOther("附言", myListDetailData.item_description)
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