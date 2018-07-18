package com.example.lostfond2.mylist

import com.example.lostfond2.service.InverseID
import com.example.lostfond2.service.LostFoundService
import com.example.lostfond2.service.MyListDataOrSearchBean
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

class MylistPresenterImpl(mylistView: MyListService.MyListView) : MyListService.MylistPresenter {
    private var mylistView: MyListService.MyListView = mylistView
//    lateinit var mylistApi: LostFoundService

    override fun setMylistData(mylistBean: List<MyListDataOrSearchBean>) {
        mylistView.setMylistData(mylistBean)
    }

    override fun loadMylistData(lostOrFound: String, page: Int) {
        async(CommonPool + QuietCoroutineExceptionHandler) {
            val mylist = LostFoundService.getMyList(lostOrFound, page).awaitAndHandle { it.printStackTrace() }?.data
                    ?: throw IllegalStateException("列表拉取失败")
            setMylistData(mylist)
        }


    }

    override fun turnStatus(id: Int) {
        async(CommonPool + QuietCoroutineExceptionHandler) {
            val mylist = LostFoundService.turnStatus(id.toString()).awaitAndHandle { it.printStackTrace() }?.data
                    ?: throw IllegalStateException("列表拉取失败")
            turnStatuSuccessCallBack(mylist)
        }
    }

    override fun turnStatuSuccessCallBack(callbackBean: InverseID) {
        mylistView.turnStatusSuccessCallBack()
    }

}