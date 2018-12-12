package com.yookiely.lostfond2.search

import android.util.Log
import android.widget.Toast
import com.orhanobut.hawk.Hawk
import com.yookiely.lostfond2.service.LostFoundService
import com.yookiely.lostfond2.service.MyListDataOrSearchBean
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.yookiely.lostfond2.service.Utils
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class SearchPresenterImpl(private val searchUIView: SearchContract.SearchUIView) : SearchContract.SearchPresenter {

    override fun loadWaterfallDataWithTime(lostOrFound: String, keyword: String, page: Int, time: Int) {
        if (Utils.campus != null) {
            val campus = Utils.campus
            try {
                launch(UI + QuietCoroutineExceptionHandler) {
                    val dataList = LostFoundService.getSearch(keyword, campus!!, time, page).awaitAndHandle {
                        it.printStackTrace()
                    }
                    if (dataList != null && dataList.error_code == -1) {
                        if (dataList.data == null || dataList.data!!.isEmpty()) {
                            val searchBean = emptyList<MyListDataOrSearchBean>()
                            setWaterfallData(searchBean)
                        } else {
                            setWaterfallData(dataList.data!!)
                        }
                    } else {
                        val searchFragment = searchUIView as SearchFragment
                        Toasty.error(searchFragment.context!!, "你网络崩啦，拿不到数据啦", Toast.LENGTH_LONG, true).show()
                    }
                }
            } catch (e: Throwable) {
                val searchBean = emptyList<MyListDataOrSearchBean>()
                setWaterfallData(searchBean)
            }
        }
    }

    override fun setWaterfallData(waterfallBean: List<MyListDataOrSearchBean>) {
        searchUIView.setSearchData(waterfallBean)
    }
}