package com.twt.service.job.home

interface JobHomeContract {
    interface JobHomeView{
        fun showJobFair()// 招聘会
        fun showOther()// 招聘信息、公告、动态
        fun onError()
        fun onNull()
    }

    interface JobHomePresenter{
        fun getGeneral(kind: String,page:Int)// 招聘信息--L ； 招聘会 -- M ； 公告&动态-- R
    }
}