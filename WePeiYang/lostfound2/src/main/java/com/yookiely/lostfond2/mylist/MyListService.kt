package com.yookiely.lostfond2.mylist


import com.yookiely.lostfond2.service.MyListDataOrSearchBean


interface MyListService {

    interface MyListView {
        fun setMylistData(mylistBean: List<MyListDataOrSearchBean>)
        fun turnStatus(id: Int)
        fun turnStatusSuccessCallBack()
    }

    interface MylistPresenter {
        fun setMylistData(mylistBean: List<MyListDataOrSearchBean>)
        fun loadMylistData(lostOrFound: String, page: Int)
        fun turnStatus(id: Int)
        fun turnStatuSuccessCallBack(callbackBean: String)
    }
}

