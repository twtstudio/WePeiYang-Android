package com.yookiely.lostfond2.mylist

import com.yookiely.lostfond2.service.MyListDataOrSearchBean

interface MyListService {

    interface MyListView {
        fun setMyListData(myListBean: List<MyListDataOrSearchBean>)
        fun turnStatus(id: Int)
        fun turnStatusSuccessCallBack()
    }

    interface MyListPresenter {
        fun setMyListData(myListBean: List<MyListDataOrSearchBean>)
        fun loadMyListData(lostOrFound: String, page: Int)
        fun turnStatus(id: Int)
        fun turnStatusSuccessCallBack(callbackBean: String)
    }
}
