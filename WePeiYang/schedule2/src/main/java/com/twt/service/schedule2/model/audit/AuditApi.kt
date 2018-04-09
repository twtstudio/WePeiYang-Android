package com.twt.service.schedule2.model.audit

import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET

interface AuditApi {
    @GET
    fun getMyAudit(userNumber: String = CommonPreferences.studentid): Deferred<CommonBody<List<AuditCourse>>>

    companion object : AuditApi by ServiceFactory()
}