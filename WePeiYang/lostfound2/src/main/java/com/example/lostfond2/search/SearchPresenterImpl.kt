package com.example.lostfond2.search

import com.example.lostfond2.service.LostFoundService
import com.example.lostfond2.service.MyListDataOrSearchBean
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class SearchPresenterImpl(val searchUIView: SearchContract.SearchUIView) : SearchContract.SearchPresenter {
    override fun loadSearchData(keyword: String, page: Int) {
        launch(UI + QuietCoroutineExceptionHandler) {
            val searchList: CommonBody<List<MyListDataOrSearchBean>> = LostFoundService.getSearch(keyword, page).await()

            if (searchList.error_code == -1) {
                setSearchData(searchList.data!!)
            }
        }

    }

    override fun setSearchData(waterfallBean: List<MyListDataOrSearchBean>) {
        searchUIView.setSearchData(waterfallBean)
    }
}