package com.twt.service.job.home

import com.twt.service.job.service.*

interface JobHomeContract {
    interface JobHomeView {
        fun showHomeFair(commonBean: List<HomeDataL>)// 招聘会,没有置顶消息
        fun showThree(dataRBean: List<HomeDataR>) // 公告和动态
        fun onError(msg:String)
        fun onNull()
    }

    interface JobHomePresenter {
        fun getGeneral(kind: String, page: Int)// 招聘信息--L ； 招聘会 -- M ； 公告&动态-- R
    }
}