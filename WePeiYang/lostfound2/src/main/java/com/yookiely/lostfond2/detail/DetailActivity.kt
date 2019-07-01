package com.yookiely.lostfond2.detail

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Window
import android.widget.Toast
import com.example.lostfond2.R

import com.yookiely.lostfond2.service.DetailData
import com.yookiely.lostfond2.service.LostFoundService
import com.yookiely.lostfond2.service.Utils
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.mta.mtaBegin
import com.twt.wepeiyang.commons.mta.mtaClick
import com.twt.wepeiyang.commons.mta.mtaEnd
import com.twt.wepeiyang.commons.ui.rec.withItems
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.android.Main
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    val timeOfLost = "lostfound2_详情 点击丢失详情页平均停留时间"
    val timeOfFound = "lostfound2_详情 点击捡到详情页平均停留时间"
    public lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.lf2_activity_detail)
        window.statusBarColor = resources.getColor(R.color.statusBarColor)
        toolbar = findViewById(R.id.toolbar)

        val bundle: Bundle = intent.extras
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val id = bundle.getInt(Utils.ID_KEY)
        val recyclerView: RecyclerView = findViewById(R.id.rv_detail)
        recyclerView.layoutManager = LinearLayoutManager(this)
//        val popupWindowView = LayoutInflater.from(applicationContext).inflate(R.layout.lf2_detail_popupwindow, null, false)
//        val popupWindow = PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
//        val banner: Banner = findViewById(R.id.br_detail_banner)

        GlobalScope.launch (Dispatchers.Main + QuietCoroutineExceptionHandler) {
            val myList: CommonBody<DetailData>? = LostFoundService.getDetailed(id).awaitAndHandle {
                it.printStackTrace()
                Toasty.error(this@DetailActivity, "你网络崩啦，拿不到数据啦", Toast.LENGTH_LONG, true).show()
            }
            if (myList != null && myList.error_code == -1) {
                if (myList.data == null) {
                    toolbar.title = ""
                } else {
                    if (myList.data!!.type == Utils.FOUND) {
                        toolbar.title = "捡到物品"
                        mtaClick("lostfound2_详情 点击捡到详情页次数", this@DetailActivity)
                        mtaBegin(timeOfFound, this@DetailActivity)
                    } else {
                        toolbar.title = "丢失物品"
                        mtaClick("lostfound2_详情 点击丢失详情页次数", this@DetailActivity)
                        mtaBegin(timeOfLost, this@DetailActivity)
                    }
                    val myListDetailData = myList.data!!
                    val images = myListDetailData.picture.orEmpty().map(Utils::getPicUrl)

                    recyclerView.withItems {

                        imagines(images,this@DetailActivity)
                        // 最后一个参数设置成true，则该detail下面灭有分割线
                        title("基本信息")
                        detail("标题", myListDetailData.title.orEmpty(), false)
                        if (myListDetailData.type != null) {
                            detail("类型", Utils.getType(myListDetailData.detail_type), false)
                        } else {
                            detail("类型", "", false)
                        }
                        detail("时间", myListDetailData.time.orEmpty(), false)

                        if (Utils.getType(myListDetailData.detail_type) == "饭卡" || Utils.getType(myListDetailData.detail_type) == "身份证" || Utils.getType(myListDetailData.detail_type) == "银行卡") {
                            detail("地点", myListDetailData.place.orEmpty(), false)
                        } else {
                            if (myListDetailData.type == Utils.LOST) detail("地点", myListDetailData.place.orEmpty(), true)
                            else detail("地点", myListDetailData.place.orEmpty(), false)
                        }
                        if (Utils.getType(myListDetailData.detail_type) == "身份证" || Utils.getType(myListDetailData.detail_type) == "饭卡") {
                            if (myListDetailData.card_name != "null" && myListDetailData.card_name != null) {
                                detail("失主姓名", myListDetailData.card_name, false)
                            } else {
                                if (Utils.getType(myListDetailData.detail_type) == "饭卡") {
                                    detail("失主姓名", "", false)
                                }
                            }
                        }
                        if (Utils.getType(myListDetailData.detail_type) == "饭卡" || Utils.getType(myListDetailData.detail_type) == "身份证") {
                            if (myListDetailData.card_number != "0" && myListDetailData.card_number != null) {
                                if (myListDetailData.type == Utils.LOST) detail("卡号", myListDetailData.card_number, true)
                                else detail("卡号", myListDetailData.card_number, false)
                            } else {
                                if (myListDetailData.type == Utils.LOST) detail("卡号", "", true)
                                else detail("卡号", "", false)
                            }
                        }

                        if (Utils.getType(myListDetailData.detail_type) == "银行卡") {
                            if (myListDetailData.card_number != "0" && myListDetailData.card_number != null) {
                                if (myListDetailData.type == Utils.LOST) detail("卡号", numberHandle(myListDetailData.card_number), true)
                                else detail("卡号", numberHandle(myListDetailData.card_number), false)
                            } else {
                                if (Utils.getType(myListDetailData.detail_type) == "饭卡") {
                                    detail("卡号", "", false)
                                }
                            }
                        }
                        if (myListDetailData.type != null && myListDetailData.type == Utils.FOUND) {
                            if (myListDetailData.recapture_place != null && myListDetailData.recapture_place != "无") {
                                detail("领取站点", Utils.getGarden(myListDetailData.recapture_place) + myListDetailData.recapture_place + Utils.getExit(myListDetailData.recapture_entrance), true)
                            } else {
                                detail("领取站点", "无", true)
                            }
                        }

                        title("联系信息")
                        detail("姓名", myListDetailData.name.orEmpty(), false)
                        detail("电话", myListDetailData.phone.orEmpty(), true)

                        title("附加信息")
                        other("附言", myListDetailData.item_description.orEmpty())
                    }
                }
            } else {
                Toasty.error(this@DetailActivity, "你网络崩啦，拿不到数据啦", Toast.LENGTH_LONG, true).show()
//                onBackPressed()
            }
        }
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            when (toolbar.title.toString()) {
                "捡到物品" -> mtaEnd(timeOfFound, this@DetailActivity)
                "丢失物品" -> mtaEnd(timeOfLost, this@DetailActivity)
            }
            onBackPressed()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

//    // 修改屏幕背景色
//    private fun bgAlpha(bgAlpha: Float) {
//        val lp = window.attributes
//        lp.alpha = bgAlpha // 0.0-1.0
//        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
//        window.attributes = lp
//    }

    private fun numberHandle(cardNumber: String) : String{
        var result = ""
        for (i in 0..cardNumber.length){
            result += if (i in 0..6 || i in cardNumber.length-6..cardNumber.length) cardNumber[i]
            else "X"
        }
        return result
    }
}
