package com.yookiely.lostfond2.waterfall

import android.util.Log
import com.orhanobut.hawk.Hawk
import com.yookiely.lostfond2.service.LostFoundService
import com.yookiely.lostfond2.service.MyListDataOrSearchBean
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.network.RetrofitProvider
import com.yookiely.lostfond2.service.Utils
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
        if (Utils.campus != null) {
            launch(UI + QuietCoroutineExceptionHandler) {
                when (lostOrFound) {
                    "lost" -> LostFoundService.getLost(Utils.campus!!, page, 0, time).await()
                    else -> LostFoundService.getFound(Utils.campus!!, page, 0, time).await()
                }.let {
                    if (it.error_code == -1) {
                        setWaterfallData(it.data!!)
                    }
                }
            }
        }
    }

    override fun loadWaterfallDataWithCondition(lostOrFound: String, page: Int, type: Int, time: Int) {
        if (type == -1) {
            loadWaterfallData(lostOrFound, page, time)
        } else {
            if (Utils.campus != null) {
                launch(UI + QuietCoroutineExceptionHandler) {
                    when (lostOrFound) {
                        "lost" -> LostFoundService.getLost(Utils.campus!!, page, type, time).await()
                        else -> LostFoundService.getFound(Utils.campus!!, page, type, time).await()
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