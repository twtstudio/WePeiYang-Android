package com.example.lostfond2.mylist


import com.example.lostfond2.service.LostFoundService
import com.example.lostfond2.service.MyListDataOrSearchBean
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class MylistPresenterImpl(mylistView: MyListService.MyListView) : MyListService.MylistPresenter {
    private var mylistView: MyListService.MyListView = mylistView
//    lateinit var mylistApi: LostFoundService

    override fun setMylistData(mylistBean: List<MyListDataOrSearchBean>) {
        mylistView.setMylistData(mylistBean)
    }

    override fun loadMylistData(lostOrFound: String, page: Int) {
        launch(UI + QuietCoroutineExceptionHandler) {
            val mylist: CommonBody<List<MyListDataOrSearchBean>> = LostFoundService.getMyList(lostOrFound, page).await()
            if (mylist.error_code == -1) {
                setMylistData(mylist.data!!)
            }
        }


    }

    override fun turnStatus(id: Int) {
        launch(UI + QuietCoroutineExceptionHandler) {
            val mylist: CommonBody<String> = LostFoundService.turnStatus(id.toString()).await()
            if (mylist.error_code == -1) {
                turnStatuSuccessCallBack(mylist.data!!)
            }
        }
    }


    override fun turnStatuSuccessCallBack(callbackBean: String) {
        mylistView.turnStatusSuccessCallBack()
    }

}