package com.twtstudio.retrox.tjulibrary.home

import android.arch.lifecycle.ViewModel
import android.content.Context
import com.twt.wepeiyang.commons.experimental.cache.*
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import com.twtstudio.retrox.tjulibrary.provider.Book
import com.twtstudio.retrox.tjulibrary.provider.Info
import com.twtstudio.retrox.tjulibrary.provider.RenewResult
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.*
import retrofit2.http.GET
import retrofit2.http.Path

object LibraryViewModel : ViewModel() {
    interface Api {
        @GET("v1/library/user/info")
        fun getLibUserInfo(): Deferred<CommonBody<Info>>

        @GET("v1/library/renew/{barcode}")
        fun renewBook(@Path("barcode") barcode: String): Deferred<CommonBody<List<RenewResult>>>
    }

    val api: Api = ServiceFactory()

    private val libraryLocalCache = Cache.hawk<Info>("TJU_LIBRARY")
    private val libraryRemoteCache = Cache.from { api.getLibUserInfo() }.map { it.data }
    val infoLiveData = RefreshableLiveData.use(local = libraryLocalCache, remote = libraryRemoteCache)

    fun renewBook(context: Context, book: Book, callback: () -> Unit = {}) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Toasty.error(context, "续借失败").show()
            infoLiveData.refresh(CacheIndicator.REMOTE)
        }
        GlobalScope.launch(Dispatchers.Main + exceptionHandler) {
            val result = api.renewBook(book.barcode).await()
            Toasty.success(context, "尝试续借完成 以实际还书时间为主").show()
            infoLiveData.refresh(CacheIndicator.REMOTE)
        }
    }
}
