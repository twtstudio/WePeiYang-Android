package com.twt.service.job.service

import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

internal const val JOB_BASE_URL = "http://job.api.twtstudio.com/"

interface JobService {

    // 0为招聘信息，1为招聘会
    @GET("${JOB_BASE_URL}api/recruit/index")
    fun getRecruits(@Query("type") type: Int, @Query("page") page: Int, @Header("Connection") value: String = "close"): Deferred<CommonBody<GeneralL>>

    @GET("${JOB_BASE_URL}api/recruit/detail")
    fun getRecruitDetail(@Query("id") id: Int, @Query("type") type: Int, @Header("Connection") value: String = "close"): Deferred<CommonBody<Recruit>>

    // 获取就业的公告 或 工作动态的标题，点击量，发布时间
    @GET("${JOB_BASE_URL}api/notice/index")
    fun getNotices(@Query("type") type: Int, @Query("page") page: Int, @Header("Connection") value: String = "close"): Deferred<CommonBody<GeneralR>>

    // type: 获取工作动态或者公告的某一条的数据详情
    @GET("${JOB_BASE_URL}api/notice/detail")
    fun getNoticeDetail(@Query("id") id: Int, @Query("type") type: Int, @Header("Connection") value: String = "close"): Deferred<CommonBody<Notice>>

    @GET("${JOB_BASE_URL}api/recruit/theory_search")
    fun jobSearch(@Query("title") title: String, @Header("Connection") value: String = "close"): Deferred<CommonBody<SearchData>>

    companion object : JobService by ServiceFactory()
}
