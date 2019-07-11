package com.twt.service.schedule2.model.exam

import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import kotlinx.coroutines.*

object ExamTableLocalAdapter {
    private val examLocalMap = mutableMapOf<String, ExamTableBean>() // courseID -> ExamBean

    fun getExamMap(forceReload: Boolean = false): Deferred<Map<String, ExamTableBean>> = GlobalScope.async(Dispatchers.Default) {
        examLocalMap.clear()
        val examCache = examTableCache.get().await()
        if (examCache == null || forceReload) {
            refreshData().awaitAndHandle {
                it.printStackTrace()
            }?.forEach {
                examLocalMap[it.id] = it
            }
        } else {
            examCache.forEach {
                examLocalMap[it.id] = it
            }
        }
        examLocalMap
    }

    fun getExamMapFromCache(): Deferred<Map<String, ExamTableBean>> = GlobalScope.async(Dispatchers.Default) {
        examLocalMap.clear()
        val examCache = examTableCache.get().await()
        examCache?.forEach {
            examLocalMap[it.id] = it
        }
        examLocalMap
    }

    private fun refreshData() = GlobalScope.async(Dispatchers.Default) {
        val data = ExamTableService.getTable().await().data
        data?.let {
            examTableCache.set(it)
        }
        return@async data
    }

}