package com.twt.service.schedule2.model.audit

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import com.twt.wepeiyang.commons.experimental.cache.*
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import kotlinx.coroutines.*
import retrofit2.http.*

interface AuditApi {
    @GET("v1/auditClass/audit")
    fun getMyAudit(@Query("user_number") userNumber: String = CommonPreferences.studentid): Deferred<CommonBody<List<AuditCourse>>>

    @GET("v1/auditClass/popular")
    fun getPopluarAudit(): Deferred<CommonBody<List<AuditPopluar>>>

    @GET("v1/auditClass/college")
    fun getAuditCollege(@Query("with_class") withClass: Int = 1): Deferred<CommonBody<List<AuditCollegeData>>>

    @GET("v1/auditClass/search")
    fun searchCourse(@Query("name") courseName: String,@Query("type") type:Int = 0): Deferred<CommonBody<List<AuditSearchCourse>>>

    @POST("v1/auditClass/audit")
    @FormUrlEncoded
    fun audit(@Field("user_number") userNumber: String, @Field("course_id") courseId: Int, @Field("info_ids") infoIds: String): Deferred<CommonBody<Any>>

    @DELETE("v1/auditClass/audit")
    fun cancelAudit(@Query("user_number") userNumber: String, @Query("ids") ids: String): Deferred<CommonBody<Any>>

    companion object : AuditApi by ServiceFactory()
}

private val auditPopluarLocal = Cache.hawk<List<AuditPopluar>>("schedule2_audit")
private val auditPopluarRemote = Cache.from(AuditApi.Companion::getPopluarAudit).map(CommonBody<List<AuditPopluar>>::data)
val auditPopluarLiveData = RefreshableLiveData.use(auditPopluarLocal, auditPopluarRemote)

val auditCourseLiveData = object : RefreshableLiveData<List<AuditCourse>, CacheIndicator>() {

    override fun observe(owner: LifecycleOwner, observer: Observer<List<AuditCourse>>) {
        super.observe(owner, observer)
        AuditCourseManager.getAuditListLive().observe(owner, observer)
    }

    override fun refresh(vararg indicators: CacheIndicator, callback: suspend (RefreshState<CacheIndicator>) -> Unit) {
        if (indicators == CacheIndicator.REMOTE) {
            GlobalScope.async(Dispatchers.Default) {
                try {
                    AuditCourseManager.refreshAuditClasstable()
                    callback(RefreshState.Success(CacheIndicator.REMOTE))
                } catch (e: Exception) {
                    e.printStackTrace()
                    callback(RefreshState.Failure(e))
                }
            }
        }
    }

    override fun onActive() {
        refresh(CacheIndicator.REMOTE)
    }

    override fun cancel() {
        // no need to impl
    }

}