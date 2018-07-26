package com.example.lostfond2.mylist


import com.example.lostfond2.service.MyListDataOrSearchBean
import com.twt.wepeiyang.commons.experimental.network.CommonBody


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

