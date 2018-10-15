package com.yookiely.lostfond2.waterfall

import com.yookiely.lostfond2.service.MyListDataOrSearchBean

interface WaterfallContract {

    interface WaterfallView {
        fun setWaterfallData(waterfallBean: List<MyListDataOrSearchBean>)

        fun loadWaterfallDataWithCondition(type: Int, time: Int)
    }

    interface WaterfallPresenter {
        fun setWaterfallData(waterfallBean: List<MyListDataOrSearchBean>)

        fun loadWaterfallData(lostOrFound: String, page: Int, time: Int)

        fun loadWaterfallDataWithCondition(lostOrFound: String, page: Int, type: Int, time: Int)
    }
}