package com.yookiely.lostfond2.search

import android.util.Log
import com.orhanobut.hawk.Hawk
import com.yookiely.lostfond2.service.LostFoundService
import com.yookiely.lostfond2.service.MyListDataOrSearchBean
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class SearchPresenterImpl(private val searchUIView: SearchContract.SearchUIView) : SearchContract.SearchPresenter {
    override fun loadWaterfallData(lostOrFound: String, keyword: String, page: Int, time: Int) {

        if (Hawk.contains("campus")) {
            val campus: Int = Hawk.get("campus")
            try {
                launch(UI + QuietCoroutineExceptionHandler) {
                    val dataList = LostFoundService.getSearch(keyword, campus, time, page).await()
                    Log.d("skhb", "ksbu")

                    if (dataList.error_code == -1) {
                        if (dataList.data == null) {
                            val searchBean = emptyList<MyListDataOrSearchBean>()
                            setWaterfallData(searchBean)
                        } else {
                            setWaterfallData(dataList.data!!)
                        }
                    }
                }
            } catch (e: Throwable) {
                val searchBean = emptyList<MyListDataOrSearchBean>()
                setWaterfallData(searchBean)
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