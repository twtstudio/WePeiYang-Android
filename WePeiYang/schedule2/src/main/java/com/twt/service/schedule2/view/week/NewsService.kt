package com.twt.service.schedule2.view.week

import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface NewsApi {
    @GET("v1/news/1/page/{page}")
    fun getNewsByPage(@Path("page") page: Int): Deferred<CommonBody<List<NewsBean>>>

    companion object : NewsApi by ServiceFactory()
}

data class NewsBean(val subject: String, val summary: String)
