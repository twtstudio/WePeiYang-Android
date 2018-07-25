package com.example.lostfond2.waterfall

import com.example.lostfond2.service.LostFoundService
import com.example.lostfond2.service.MyListDataOrSearchBean
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.network.RetrofitProvider
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

class WaterfallPresenterImpl(var waterfallView: WaterfallContract.WaterfallView)
    : WaterfallContract.WaterfallPresenter {

    lateinit var lostFoundService: LostFoundService

    override fun setWaterfallData(waterfallBean: List<MyListDataOrSearchBean>) {
        waterfallView.setWaterfallData(waterfallBean)
    }

    override fun loadWaterfallData(lostOrFound: String, page: Int) {


        async(CommonPool + QuietCoroutineExceptionHandler) {
            val dataList = when (lostOrFound) {
                "lost" -> lostFoundService.getLost(page, 0).awaitAndHandle { it.printStackTrace() }?.data!!
                else -> lostFoundService.getFound(page, 0).awaitAndHandle { it.printStackTrace() }?.data!!
            }

            setWaterfallData(dataList)
        }
    }

    override fun loadWaterfallDataWithType(lostOrFound: String, page: Int, type: Int) {
        if (type == -1) {
            loadWaterfallData(lostOrFound, page)
        } else {
            async(CommonPool + QuietCoroutineExceptionHandler) {
                val dataList = when (lostOrFound) {
                    "lost" -> lostFoundService.getLost(page, type).awaitAndHandle { it.printStackTrace() }?.data!!
                    else -> lostFoundService.getFound(page, type).awaitAndHandle { it.printStackTrace() }?.data!!
                }

                setWaterfallData(dataList)
            }
        }
    }
}