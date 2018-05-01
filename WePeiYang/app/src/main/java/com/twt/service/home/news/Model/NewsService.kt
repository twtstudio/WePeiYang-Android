package com.twt.service.home.news.Model

import android.os.Parcel
import android.os.Parcelable
import com.twt.wepeiyang.commons.experimental.cache.*
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.*
import xyz.rickygao.gpa2.service.GpaBean
import xyz.rickygao.gpa2.service.GpaService

interface NewsService {
    @GET("v1/news/1/page/1")
    fun get(): Deferred<CommonBody<List<Data>>>

    companion object : NewsService by ServiceFactory()
}

val newsLocalCache = Cache.hawk<List<Data>>("cache_news")
val newsRemoteCache = Cache.from(NewsService.Companion::get).map(CommonBody<List<Data>>::data)
val newsLiveData = RefreshableLiveData.use(newsLocalCache, newsRemoteCache)

data class Data(
		val index: Int,
		val subject: String,
		val pic: String,
		val visitcount: String,
		val comments: Int,
		val summary: String
)
