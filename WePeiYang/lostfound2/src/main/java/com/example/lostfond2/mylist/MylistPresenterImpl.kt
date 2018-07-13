package com.example.lostfond2.mylist

import com.example.lostfond2.service.LostFoundService
import com.example.lostfond2.service.MyListDataOrSearchBean
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.network.RetrofitProvider

class MylistPresenterImpl(mylistView: MyListService.MyListView) : MyListService.MylistPresenter {
    private var mylistView: MyListService.MyListView = mylistView
    lateinit var mylistApi: LostFoundService

    override fun setMylistData(mylistBean: CommonBody<List<MyListDataOrSearchBean>>) {
        mylistView.setMylistData(mylistBean)
    }

    override fun loadMylistData(lostOrFound: String, page: Int) {
        mylistApi = RetrofitProvider.getRetrofit().create(LostFoundService::class.java)
    }

    override fun turnStatus(id: Int) {
        mylistApi = RetrofitProvider.getRetrofit().create(LostFoundService::class.java)
    }

    override fun turnStatuSuccessCallBack(callbackBean: CommonBody<List<MyListDataOrSearchBean>>) {
        mylistView.turnStatusSuccessCallBack()
    }

}