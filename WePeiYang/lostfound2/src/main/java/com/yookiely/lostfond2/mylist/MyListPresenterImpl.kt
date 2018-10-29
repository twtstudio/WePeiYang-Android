package com.yookiely.lostfond2.mylist

import com.yookiely.lostfond2.service.LostFoundService
import com.yookiely.lostfond2.service.MyListDataOrSearchBean
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class MyListPresenterImpl(val myListView: MyListService.MyListView) : MyListService.MyListPresenter {
//    lateinit var mylistApi: LostFoundService

    override fun setMyListData(myListBean: List<MyListDataOrSearchBean>) {
        myListView.setMyListData(myListBean)
    }

    override fun loadMyListData(lostOrFound: String, page: Int) {
        launch(UI + QuietCoroutineExceptionHandler) {
            val myList: CommonBody<List<MyListDataOrSearchBean>> = LostFoundService.getMyList(lostOrFound, page).await()

            if (myList.error_code == -1) {
                setMyListData(myList.data!!)
            }
        }
    }

    override fun turnStatus(id: Int) {
        launch(UI + QuietCoroutineExceptionHandler) {
            val myList: CommonBody<String> = LostFoundService.turnStatus(id.toString()).await()

            if (myList.error_code == -1) {
                turnStatusSuccessCallBack(myList.data!!)
            }
        }
    }

    override fun turnStatusSuccessCallBack(callbackBean: String) {
        myListView.turnStatusSuccessCallBack()
    }
}