package com.yookiely.lostfond2.waterfall

import com.orhanobut.hawk.Hawk
import com.yookiely.lostfond2.service.LostFoundService
import com.yookiely.lostfond2.service.MyListDataOrSearchBean
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.network.RetrofitProvider
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class WaterfallPresenterImpl(var waterfallView: WaterfallContract.WaterfallView)
    : WaterfallContract.WaterfallPresenter {


    override fun setWaterfallData(waterfallBean: List<MyListDataOrSearchBean>) {
        waterfallView.setWaterfallData(waterfallBean)
    }

    override fun loadWaterfallData(lostOrFound: String, page: Int, time: Int) {
        if (Hawk.contains("campus")) {
            val campus: Int = Hawk.get("campus")
            launch(UI + QuietCoroutineExceptionHandler) {
                val dataList = when (lostOrFound) {
                    "lost" -> LostFoundService.getLost(campus, page, 0, time).await()
                    else -> LostFoundService.getFound(campus, page, 0, time).await()
                }

                if (dataList.error_code == -1) {
                    setWaterfallData(dataList.data!!)
                }
            }
        }
    }

    override fun loadWaterfallDataWithCondition(lostOrFound: String, page: Int, type: Int, time: Int) {
        if (type == -1) {
            loadWaterfallData(lostOrFound, page, time)
        } else {
            if (Hawk.contains("campus")) {
                val campus: Int = Hawk.get("campus")
                launch(UI + QuietCoroutineExceptionHandler) {
                    val dataList = when (lostOrFound) {
                        "lost" -> LostFoundService.getLost(campus, page, type, time).await()
                        else -> LostFoundService.getFound(campus, page, type, time).await()
                    }

                    if (dataList.error_code == -1) {
                        setWaterfallData(dataList.data!!)
                    }
                }
            }
        }
    }
}