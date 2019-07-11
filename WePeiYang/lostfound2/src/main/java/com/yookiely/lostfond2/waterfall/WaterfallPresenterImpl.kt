package com.yookiely.lostfond2.waterfall

import android.util.Log
import com.orhanobut.hawk.Hawk
import com.yookiely.lostfond2.service.LostFoundService
import com.yookiely.lostfond2.service.MyListDataOrSearchBean
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.network.RetrofitProvider
import com.yookiely.lostfond2.service.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WaterfallPresenterImpl(var waterfallView: WaterfallContract.WaterfallView)
    : WaterfallContract.WaterfallPresenter {


    override fun setWaterfallData(waterfallBean: List<MyListDataOrSearchBean>) {
        waterfallView.setWaterfallData(waterfallBean)
    }

    override fun loadWaterfallData(lostOrFound: String, page: Int, time: Int) {
        val campus = Utils.campus

        if (campus != null) {
            GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                when (lostOrFound) {
                    "lost" -> LostFoundService.getLost(campus, page, Utils.ALL_TYPE, time).await()
                    else -> LostFoundService.getFound(campus, page, Utils.ALL_TYPE, time).await()
                }.let {
                    if (it.error_code == -1) {
                        setWaterfallData(it.data!!)
                    }
                }
            }
        }
    }

    override fun loadWaterfallDataWithCondition(lostOrFound: String, page: Int, type: Int, time: Int) {
        val campus = Utils.campus

        if (type == -1) {
            loadWaterfallData(lostOrFound, page, time)
        } else {
            if (campus != null) {
                GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                    when (lostOrFound) {
                        "lost" -> LostFoundService.getLost(campus, page, type, time).await()
                        else -> LostFoundService.getFound(campus, page, type, time).await()
                    }.let {
                        if (it.error_code == -1) {
                            setWaterfallData(it.data!!)
                        }
                    }

                }
            }
        }
    }
}