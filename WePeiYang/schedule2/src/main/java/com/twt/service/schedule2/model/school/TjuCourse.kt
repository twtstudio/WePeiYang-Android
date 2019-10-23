package com.twt.service.schedule2.model.school

import android.content.Context
import com.twt.service.schedule2.model.Classtable
import com.twt.service.schedule2.model.SchedulePref
import com.twt.wepeiyang.commons.experimental.cache.Cache
import com.twt.wepeiyang.commons.experimental.cache.hawk
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.*
import retrofit2.http.GET
import java.lang.NumberFormatException

interface TjuCourseApi {

    @GET("v1/classtable")
    fun getClassTable(): Deferred<CommonBody<Classtable>>

    companion object : TjuCourseApi by ServiceFactory()

}

val classtableCacheKey = "schedule2_tju_classtable"
val tjuCourseCache = Cache.hawk<Classtable>(classtableCacheKey)

/**
 * 获取TJU课程表的逻辑
 * 有缓存的时候优先获取缓存 并且异步刷新
 * 强制刷新的时候 网络错误返回缓存 刷新成功则同时刷写缓存
 */
suspend fun TjuCourseApi.Companion.refresh(mustRefresh: Boolean = false): Classtable {
    val deferredClasstable = getClassTable()
    val handler: suspend (Throwable) -> Unit = { it.printStackTrace() }
    // 要么是必须刷新 要么是没有缓存
    if (mustRefresh || tjuCourseCache.get().await() == null) {
        // 刷新失败就拿缓存 缓存还没有就凉了
        val classtable: Classtable? = deferredClasstable.awaitAndHandle(handler)?.data
                ?: tjuCourseCache.get().await()
        try {
            classtable?.let {
                tjuCourseCache.set(it)
                SchedulePref.termStart = it.termStart
            }
        } catch(e: IllegalStateException) {
            e.printStackTrace()
        }
        return classtable ?: throw IllegalStateException("凉了啊...无法获取课程表,稍后再试（怕是办公网崩了）多刷新试试")
    } else {
        // 这种情况也要静默刷新一下 成功就刷
        try {
            GlobalScope.async(Dispatchers.Default) {
                val classtable = deferredClasstable.awaitAndHandle(handler)?.data
                classtable?.let {
                    tjuCourseCache.set(it)
                    SchedulePref.termStart = it.termStart
                }
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
        // 到这里的话 缓存不应该是空 如果真的是空我们只能抛出异常
        return tjuCourseCache.get().await() ?: throw IllegalStateException("课程表缓存系统问题。")
    }
}