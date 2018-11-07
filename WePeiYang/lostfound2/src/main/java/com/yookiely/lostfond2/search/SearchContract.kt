package com.yookiely.lostfond2.search

import com.yookiely.lostfond2.service.MyListDataOrSearchBean

interface SearchContract {
    interface SearchUIView {
        fun setSearchData(waterfallBean: List<MyListDataOrSearchBean>)

        fun loadSearhDataWithTime(time: Int)
    }

    interface SearchPresenter {
        fun setWaterfallData(waterfallBean: List<MyListDataOrSearchBean>)

        fun loadWaterfallDataWithTime(lostOrFound: String, keyword: String, page: Int, time: Int)
    }
}