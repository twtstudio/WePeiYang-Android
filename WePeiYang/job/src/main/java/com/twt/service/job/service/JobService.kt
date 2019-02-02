package com.twt.service.job.service

import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

internal const val Job_BASE_URL = "http://job.api.twtstudio.com/"
interface JobService {

    // 获取就业的公告 或 工作动态的标题，点击量，发布时间
    @GET("${Job_BASE_URL}api/index")
    // type:0为招聘信息，1为招聘会
    fun getNotioces(@Query("type")type:Int,@Query("page")page:Int): Deferred<CommonBody<General>>

    // type:获取工作动态或者公告的某一条的数据详情
    @GET("${Job_BASE_URL}api/notice/detail")
    fun getNoticeDetail(@Query("id")id:Int,@Query("type")type: Int): Deferred<CommonBody<List<Notice>>>

    // 0为招聘信息，1为招聘会
    @GET("${Job_BASE_URL}api/recruit/index")
    fun getRecruits(@Query("type")type:Int,@Query("page")page:Int):Deferred<CommonBody<General>>

    @GET("${Job_BASE_URL}api/recruit/detail")
    fun getRecruitDetail(@Query("id")id:Int,@Query("type")type: Int):Deferred<CommonBody<List<Recruit>>>

    @GET("${Job_BASE_URL}api/recruit/search")
    fun search(@Query("title")title:String) : Deferred<CommonBody<SearchData>>

    companion object : JobService by ServiceFactory()
}