package com.twt.service.schedule2.model.audit

import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface AuditApi {
    @GET("v1/auditClass/audit")
    fun getMyAudit(@Query("user_number") userNumber: String = CommonPreferences.studentid): Deferred<CommonBody<List<AuditCourse>>>

    @GET("v1/auditClass/popular")
    fun getPopluarAudit(): Deferred<CommonBody<List<AuditPopluar>>>



    companion object : AuditApi by ServiceFactory()
}