package com.twt.service.schedule2.model.exam

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async

object ExamTableLocalAdapter {
    private val examLocalMap = mutableMapOf<String, ExamTableBean>() // courseID -> ExamBean

    fun getExamMap(forceReload: Boolean = false): Deferred<Map<String, ExamTableBean>> = async(CommonPool) {
        examLocalMap.clear()
        val examCache = examTableCache.get().await()
        if (examCache == null || forceReload) {
            refreshData().await()?.forEach {
                examLocalMap[it.id] = it
            }
        } else {
            examCache.forEach {
                examLocalMap[it.id] = it
            }
        }
        examLocalMap
    }


    private fun refreshData() = async(CommonPool) {
        val data = ExamTableService.getTable().await().data
        data?.let {
            examTableCache.set(it)
        }
        return@async data
    }

}