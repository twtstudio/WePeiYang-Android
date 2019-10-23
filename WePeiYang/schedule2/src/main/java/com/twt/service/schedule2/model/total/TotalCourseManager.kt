package com.twt.service.schedule2.model.total

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
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
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

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
        GlobalScope.async(Dispatchers.Main) {

            refreshCallback.invoke(RefreshState.Refreshing())

            val examTableDeferred = ExamTableLocalAdapter.getExamMap(true)

            val tjuClassTableProvider: Deferred<AbsClasstableProvider> = async(Dispatchers.Default + QuietCoroutineExceptionHandler) {
                try {
                    val classTable = TjuCourseApi.refresh(refreshTju)
                    CommonClassTable(classTable)
                } catch (e: IllegalStateException) {
                    CommonClassTable(Classtable(courses = mutableListOf()))
                }
            }

            val auditClasstableProvider: Deferred<AbsClasstableProvider> = async(Dispatchers.Default) {
                if (refreshAudit) {
                    try {
                        AuditCourseManager.refreshAuditClasstable() // 这里在网络请求失败的时候会抛出一个异常 需要捕获一下
                    } catch (e: Exception) {
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

            val finalClasstableProvider = MergedClassTableProvider(
                    tjuClassTableProvider.await(),
                    auditClasstableProvider.await(),
                    customCourseProvider.await(),
                    duplicateCourseProvider.await()
            )
            try {
                examTableDeferred.await()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            refreshCallback.invoke(RefreshState.Success(CacheIndicator.REMOTE))
            mergedClassTableProvider.value = finalClasstableProvider
        }.invokeOnCompletion {
            it?.printStackTrace()
            it?.apply {
                refreshCallback.invoke(RefreshState.Failure(this))
            }
        }

        return mergedClassTableProvider
    }
}