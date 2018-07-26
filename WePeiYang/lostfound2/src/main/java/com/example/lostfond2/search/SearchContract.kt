package com.example.lostfond2.search

import com.example.lostfond2.service.MyListDataOrSearchBean

interface SearchContract {
    interface SearchUIView {
        fun setSearchData(waterfallBean: MutableList<MyListDataOrSearchBean>)
    }

    interface SearchPresenter {
        fun loadSearchData(keyword: String, page: Int)
        fun setSearchData(waterfallBean: MutableList<MyListDataOrSearchBean>)
    }

}