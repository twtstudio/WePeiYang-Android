package com.twt.service.schedule2.model.total

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.twt.service.schedule2.extensions.RefreshCallback
import com.twt.service.schedule2.model.AbsClasstableProvider
import com.twt.service.schedule2.model.Classtable
import com.twt.service.schedule2.model.CommonClassTable
import com.twt.service.schedule2.model.MergedClassTableProvider
import com.twt.service.schedule2.model.audit.AuditCourseManager
import com.twt.service.schedule2.model.custom.CustomCourseManager
import com.twt.service.schedule2.model.duplicate.DuplicateCourseManager
import com.twt.service.schedule2.model.exam.ExamTableLocalAdapter
import com.twt.service.schedule2.model.school.TjuCourseApi
import com.twt.service.schedule2.model.school.refresh
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import kotlinx.coroutines.*
import xyz.rickygao.gpa2.spider.utils.SpiderCookieManager

object TotalCourseManager {

    /**
     * 做一个内存缓存
     */
    private val mergedClassTableProvider: MutableLiveData<MergedClassTableProvider> = object : MutableLiveData<MergedClassTableProvider>() {
        override fun onActive() {
            super.onActive()
            invalidate()
        }
    }

    fun invalidate() = getTotalCourseManager() //只刷新课程表（比如说修改了课程表的格式）

    /**
     * 如果什么都不刷新的话 并且有缓存 那就直接返回内存缓存
     * @param refreshCustom 刷新自定义课程，建议在添加自定义课程之后 做一次刷新
     */
    fun getTotalCourseManager(
            refreshTju: Boolean = false,
            refreshAudit: Boolean = false,
            refreshCustom: Boolean = false,
            refreshCallback: RefreshCallback = {} /* 指示刷新状态 内部使用弱引用 */
    ): LiveData<MergedClassTableProvider> {

        if (!refreshTju && !refreshAudit && !refreshCustom && (mergedClassTableProvider.value != null)) {
            val valueToRefresh = mergedClassTableProvider.value
            mergedClassTableProvider.value = valueToRefresh
            refreshCallback.invoke(RefreshState.Success(CacheIndicator.LOCAL))
            return mergedClassTableProvider
        }

        GlobalScope.async(Dispatchers.Main + QuietCoroutineExceptionHandler) {

            refreshCallback.invoke(RefreshState.Refreshing())

            //TODO(这一步有点问题，看看这里为什么会传 true ，进行强制刷新）
//            val examTableDeferred = ExamTableLocalAdapter.getExamMap(true)
            val examTableDeferred = ExamTableLocalAdapter.getExamMap()


            Log.d("testitemdisplaytime", "network process 1")

            // 如果获取课程表信息失败,则返回一个空列表
            // 这里的问题是：加入网络请求错误，也会显示刷新成功。
            val tjuClassTableProvider: Deferred<AbsClasstableProvider> = async(Dispatchers.Default) {
                try {
                    val classTable = TjuCourseApi.refresh(refreshTju)
                    CommonClassTable(classTable)
                } catch (e: IllegalStateException) {
                    throw e
                }
            }

//            val tjuClassTableProvider: Deferred<AbsClasstableProvider> = async(Dispatchers.Main) {
//                try {
//                    val classTable = TjuCourseApi.refresh(refreshTju)
//                    CommonClassTable(classTable)
//                } catch (e: IllegalStateException) {
//                    Log.d("testitemd tjuClas:","testitemd tjuClassTableProvider: " + e.message)
//                    refreshCallback.invoke(RefreshState.Failure(e))
//                    CommonClassTable(Classtable(courses = mutableListOf()))
//                }
//            }

            val auditClasstableProvider: Deferred<AbsClasstableProvider> = async(Dispatchers.Default) {
                if (refreshAudit) {
                    try {
                        AuditCourseManager.refreshAuditClasstable() // 这里在网络请求失败的时候会抛出一个异常 需要捕获一下
                    } catch (e: Exception) {
                        Log.d("testitemd audit error", "auditClasstableProvider" + e.message)
                        e.printStackTrace()
                    }
                }
                AuditCourseManager.getAuditClasstableProvider()
            }

            val customCourseProvider: Deferred<AbsClasstableProvider> = async(Dispatchers.Default) {
                CustomCourseManager.getCustomClasstableProvider()
            }

            val duplicateCourseProvider = async(Dispatchers.Default) {
                DuplicateCourseManager.clearDuplicateCache()
                tjuClassTableProvider.await()
                DuplicateCourseManager.getDuplicateCourseProvider()
            }

            Log.d("testitemdisplaytime", "network process examTableDeferred finish")

            val finalClasstableProvider = MergedClassTableProvider(
                    tjuClassTableProvider.await(),
                    auditClasstableProvider.await(), // ok
                    customCourseProvider.await(),
                    duplicateCourseProvider.await()
            )

            Log.d("testitemdisplaytime", "finalClasstableProvider.totalCoursesList: " + finalClasstableProvider.totalCoursesList.toString())

            try {

                // 这一步耗时严重
                examTableDeferred.await()
                Log.d("testitemdisplaytime", "network process examTableDeferred finish")

            } catch (e: Exception) {
                e.printStackTrace()
            }

            mergedClassTableProvider.value = finalClasstableProvider
            if (refreshTju) {
                SpiderCookieManager.getLoginState {
                    when (it) {
                        is RefreshState.Failure -> {
                            throw it.throwable
                        }
                    }
                }
            }

            refreshCallback.invoke(RefreshState.Success(CacheIndicator.REMOTE))
        }.invokeOnCompletion {
            it?.printStackTrace()
            it?.apply {
                refreshCallback.invoke(RefreshState.Failure(this))
            }
        }

        return mergedClassTableProvider
    }
}