package com.example.lostfond2.search

import com.example.lostfond2.service.LostFoundService
import com.example.lostfond2.service.MyListDataOrSearchBean
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import kotlinx.coroutines.experimental.async

class SearchPresenterImpl(val searchUIView: SearchContract.SearchUIView) : SearchContract.SearchPresenter {
    override fun loadSearchData(keyword: String, page: Int) {
        async {
            val mysearch = LostFoundService.getSearch(keyword, page).awaitAndHandle { it.printStackTrace() }?.data
                    ?: throw IllegalStateException("列表拉取失败")
            setSearchData(mysearch)
        }

    }

    override fun setSearchData(waterfallBean: List<MyListDataOrSearchBean>) {
        searchUIView.setSearchData(waterfallBean)
    }
}