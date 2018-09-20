package com.yookiely.lostfond2.waterfall

import com.yookiely.lostfond2.service.MyListDataOrSearchBean

interface WaterfallContract {

    interface WaterfallView {
        fun setWaterfallData(waterfallBean: List<MyListDataOrSearchBean>)

        fun loadWaterfallDataWithType(type: Int)

        fun loadWaterfallDataWithTime(time: Int)
    }

    interface WaterfallPresenter {
        fun setWaterfallData(waterfallBean: List<MyListDataOrSearchBean>)

        fun loadWaterfallData(lostOrFound: String, page: Int)

        fun loadWaterfallDataWithType(lostOrFound: String, page: Int, type: Int)
    }
}