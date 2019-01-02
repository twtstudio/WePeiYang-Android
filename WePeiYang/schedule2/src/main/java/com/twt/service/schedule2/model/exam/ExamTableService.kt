package com.twt.service.schedule2.model.exam

import com.twt.wepeiyang.commons.experimental.cache.Cache
import com.twt.wepeiyang.commons.experimental.cache.hawk
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.*

interface ExamTableService {

    @GET("v1/examtable")
    fun getTable(): Deferred<CommonBody<List<ExamTableBean>>>

    companion object : ExamTableService by ServiceFactory()
}

val examTableCache = Cache.hawk<List<ExamTableBean>>("exam_table_key")