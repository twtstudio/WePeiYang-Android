package com.yookiely.lostfond2.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.PopupWindow
import com.bumptech.glide.Glide
import android.view.LayoutInflater
import android.view.Window
import android.widget.Toast
import com.example.lostfond2.R

import com.yookiely.lostfond2.service.DetailData
import com.yookiely.lostfond2.service.LostFoundService
import com.yookiely.lostfond2.service.Utils
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.lf2_activity_water_fall.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class DetailActivity : AppCompatActivity() {
    private val FOUND = 1
    private val LOST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.lf2_activity_detail)
        window.statusBarColor = resources.getColor(R.color.statusBarColor)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        val bundle: Bundle = intent.extras
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val id = bundle.getInt("id")
        val recyclerView: RecyclerView = findViewById(R.id.rv_detail)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val popupWindowView = LayoutInflater.from(applicationContext).inflate(R.layout.lf2_detail_popupwindow, null, false)
        val popupWindow = PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        val banner: Banner = findViewById(R.id.br_detail_banner)

        launch(UI + QuietCoroutineExceptionHandler) {
            val myList: CommonBody<DetailData>? = LostFoundService.getDetailed(id).awaitAndHandle {
                it.printStackTrace()
            }
            if (myList != null && myList.error_code == -1) {
                if (myList.data!!.type == FOUND) {
                    toolbar.title = "   捡到物品"
                } else {
                    toolbar.title = "   丢失物品"
                }

                val myListDetailData = myList.data!!
                val images = ArrayList<String>()
                if (myListDetailData.picture != null) {
                    for (temp in myListDetailData.picture) {
                        val tempImage = Utils.getPicUrl(temp)
                        images.add(tempImage)
                    }
                }

                banner.apply {
                    setImageLoader(GlideImagineLoader())
                    setImages(images)

                    setBannerStyle(BannerConfig.NUM_INDICATOR)
                    isAutoPlay(false)
                    //点击出现大图
                    setOnBannerListener { position ->
                        popupWindow.apply {
                            isFocusable = true

                            Glide.with(context)
                                    .load(images[position])
                                    .error(R.drawable.lf_detail_np)
                                    .into(popupWindowView.findViewById(R.id.lf2_detail_popupwindow))

                            isOutsideTouchable = true
                            popupWindow.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@DetailActivity, R.color.white_color)))
                            bgAlpha(0.5f)
                            showAsDropDown(toolbar, Gravity.CENTER, 0, 0)
                        }
                        popupWindow.setOnDismissListener {
                            // popupWindow隐藏时恢复屏幕正常透明度
                            bgAlpha(1f)
                        }
                        // 点击布局文件（也可以理解为点击大图）
                    }
                    start()
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
                    if (myListDetailData.detail_type == LOST) {
                        if (myListDetailData.card_name != "null" && myListDetailData.card_name != null) {
                            setDetail("失主姓名", myListDetailData.card_name, false)
                        } else {
                            if (Utils.getType(myListDetailData.detail_type) == "饭卡") {
                                setDetail("失主姓名", "", false)
                            }
                        }
                    }
                    if (myListDetailData.detail_type == LOST) {
                        if (myListDetailData.card_number != "0" && myListDetailData.card_number != null) {
                            setDetail("卡号", myListDetailData.card_number, false)
                        } else {
                            if (Utils.getType(myListDetailData.detail_type) == "饭卡") {
                                setDetail("卡号", "", false)
                            }
                        }
                    }
                    if (myListDetailData.type != null && myListDetailData.type == Utils.TYPE_OF_FOUND) {
                        if (myListDetailData.recapture_place != null && myListDetailData.recapture_place != "无") {
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
                }
            } else {
                Toasty.error(this@DetailActivity, "你网络崩啦，拿不到数据啦", Toast.LENGTH_LONG, true).show()
//                onBackPressed()
            }
        }
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // 修改屏幕背景色
    private fun bgAlpha(bgAlpha: Float) {
        val lp = window.attributes
        lp.alpha = bgAlpha // 0.0-1.0
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.attributes = lp
    }
}