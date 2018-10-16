package com.yookiely.lostfond2.search

import com.orhanobut.hawk.Hawk
import com.yookiely.lostfond2.service.LostFoundService
import com.yookiely.lostfond2.service.MyListDataOrSearchBean
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class SearchPresenterImpl(val searchUIView: SearchContract.SearchUIView) : SearchContract.SearchPresenter {
    override fun loadWaterfallData(lostOrFound: String, keyword: String, page: Int, time: Int) {

        if (Hawk.contains("campus")) {
            val campus: Int = Hawk.get("campus")
            launch(UI + QuietCoroutineExceptionHandler) {
                val dataList = LostFoundService.getSearch(keyword, campus, time, page).await()

                if (dataList.error_code == -1) {
                    setWaterfallData(dataList.data!!)
                }
            }
        }
    }

    override fun loadWaterfallDataWithTime(lostOrFound: String, keyword: String, page: Int, time: Int) {
        loadWaterfallData(lostOrFound, keyword, page, time)

    }

    override fun setWaterfallData(waterfallBean: List<MyListDataOrSearchBean>) {
        searchUIView.setSearchData(waterfallBean)
    }
}