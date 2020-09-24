package com.twt.service.schedule2.model.exam

import android.util.Log
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import kotlinx.coroutines.*

object ExamTableLocalAdapter {
    private val examLocalMap = mutableMapOf<String, ExamTableBean>() // courseID -> ExamBean


    //TODO(写了错误处理 awaitAndHandle 但是没有加上错误处理的协程 QuietCoroutineExceptionHandler)
    fun getExamMap(forceReload: Boolean = false): Deferred<Map<String, ExamTableBean>> = GlobalScope.async(Dispatchers.Default) {
        examLocalMap.clear()
        Log.d("testitemdisplaytime", "network process get exam map start")

        val examCache = examTableCache.get().await()

        // 这里先判断是否有考表缓存，如果有，就用缓存，没有的话就看是否需要强制刷新
        if (examCache == null || forceReload) {

            // refreshData 耗时严重
            refreshData().awaitAndHandle {
                it.printStackTrace()
                Log.d("testitemdisplaytime", it.message)

            }?.forEach {
                examLocalMap[it.id] = it
                Log.d("testitemdisplaytime", "network process 2")

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