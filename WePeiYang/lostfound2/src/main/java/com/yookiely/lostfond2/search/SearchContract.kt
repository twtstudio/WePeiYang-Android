package com.yookiely.lostfond2.search

import com.yookiely.lostfond2.service.MyListDataOrSearchBean

interface SearchContract {
    interface SearchUIView {
        fun setSearchData(waterfallBean: List<MyListDataOrSearchBean>)
    }

    interface SearchPresenter {
        fun loadSearchData(keyword: String, page: Int)
        fun setSearchData(waterfallBean: List<MyListDataOrSearchBean>)
    }

}