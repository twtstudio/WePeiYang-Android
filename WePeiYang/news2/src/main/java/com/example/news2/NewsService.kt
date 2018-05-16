package com.example.news2

import android.os.Parcel
import android.os.Parcelable
import com.twt.wepeiyang.commons.experimental.cache.*
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.*


interface NewsService {
    @GET("v1/news/1/page/1")
	fun getRecyclerViewData(): Deferred<CommonBody<List<RecyclerViewData>>>

	@GET("v1/app/index")
	fun getBannerData(): Deferred<CommonBody<BannerData>>

    companion object : NewsService by ServiceFactory()
}

val newsRecyclerViewLocalCache = Cache.hawk<List<RecyclerViewData>>("cache_news_recyclerView")
val newsRecyclerViewRemoteCache = Cache.from(NewsService.Companion::getRecyclerViewData).map(CommonBody<List<RecyclerViewData>>::data)
val newsRecyclerViewLiveData = RefreshableLiveData.use(newsRecyclerViewLocalCache, newsRecyclerViewRemoteCache)
val newsBannerLocalCache = Cache.hawk<BannerData>("cache_news_banner_my")
val newsBannerRemoteCache = Cache.from(NewsService.Companion::getBannerData).map(CommonBody<BannerData>::data)
val newsBannerLiveData = RefreshableLiveData.use(newsBannerLocalCache, newsBannerRemoteCache)

data class RecyclerViewData(
		val index: Int,
		val subject: String,
		val pic: String,
		val visitcount: String,
		val comments: Int,
		val summary: String
)


data class BannerData(
		val carousel: List<Carousel>,
		val news: News,
		val service: Service
)

data class News(
		val campus: List<Campu>,
		val annoucements: List<Annoucement>,
		val jobs: List<Any>
)

data class Campu(
		val index: String,
		val subject: String,
		val pic: String,
		val addat: String,
		val visitcount: String
)

data class Annoucement(
		val index: String,
		val subject: String,
		val addat: String,
		val gonggao: String,
		val brief: String
)

data class Carousel(
		val index: String,
		val pic: String,
		val subject: String
)

data class Service(
		val lost: List<Any>,
		val found: List<Any>
)

