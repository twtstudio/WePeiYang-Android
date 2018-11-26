package com.twtstudio.service.tjwhm.exam.user

import com.twt.wepeiyang.commons.experimental.cache.*
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import com.twtstudio.service.tjwhm.exam.commons.EXAM_BASE_URL
import com.twtstudio.service.tjwhm.exam.problem.ProblemBean
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import okhttp3.MultipartBody
import retrofit2.http.*

interface UserService {
    @GET("${EXAM_BASE_URL}student")
    fun getUserInfo(): Deferred<CommonBody<UserBean>>

    @GET("${EXAM_BASE_URL}student/history")
    fun getHistory(): Deferred<CommonBody<List<OneHistoryBean>>>

    @GET("${EXAM_BASE_URL}special/getQues/{star_or_wrong}")
    fun getCollections(@Path("star_or_wrong") starOrWrong: String): Deferred<CommonBody<List<ProblemBean>>>

    @Multipart
    @POST("${EXAM_BASE_URL}special/addQues/{star_or_wrong}")
    fun addCollection(@Path("star_or_wrong") starOrWrong: String, @Part list: MutableList<MultipartBody.Part>): Deferred<CommonBody<Nothing>>

    @Multipart
    @POST("${EXAM_BASE_URL}special/deleteQues/{star_or_wrong}")
    fun deleteCollection(@Path("star_or_wrong") starOrWrong: String, @Part list: MutableList<MultipartBody.Part>): Deferred<CommonBody<Nothing>>

    companion object : UserService by ServiceFactory()
}

val examUserLocalCache = Cache.hawk<UserBean>("ExamUser")
val examUserRemoteCache = Cache.from(UserService.Companion::getUserInfo).map(CommonBody<UserBean>::data)
val examUserLiveData = RefreshableLiveData.use(examUserLocalCache, examUserRemoteCache)

val examUserHistoryLocalCache = Cache.hawk<List<OneHistoryBean>>("ExamUserHistory")
val examUserHistoryRemoteCache = Cache.from(UserService.Companion::getHistory).map(CommonBody<List<OneHistoryBean>>::data)
val examUserHistoryLiveData = RefreshableLiveData.use(examUserHistoryLocalCache, examUserHistoryRemoteCache)

fun getCollections(starOrWrong: String, callback: suspend (RefreshState<CommonBody<List<ProblemBean>>>) -> Unit) =
        launch(UI) {
            UserService.getCollections(starOrWrong).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                callback(RefreshState.Success(it))
            }
        }

fun addCollection(starOrWrong: String, list: MutableList<MultipartBody.Part>, callback: suspend (RefreshState<CommonBody<Nothing>>) -> Unit) =
        launch(UI) {
            UserService.addCollection(starOrWrong, list).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                if (it.error_code == 0)
                    callback(RefreshState.Success(it))
            }
        }

fun deleteCollection(starOrWrong: String, list: MutableList<MultipartBody.Part>, callback: suspend (RefreshState<CommonBody<Nothing>>) -> Unit) =
        launch(UI) {
            UserService.deleteCollection(starOrWrong, list).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                if (it.error_code == 0)
                    callback(RefreshState.Success(it))
            }
        }


data class UserBean(
        val id: Int,
        val twt_name: String,
        val user_number: String,
        val avatar_url: String,
        val title_name: String,
        val done_count: String,
        val error_count: String,
        val course_count: Int,
        val collect_count: String,
        val current_course_id: String?,
        val current_course_name: String?,
        val current_course_done_count: Int?,
        val current_ques_type: String?,
        val current_course_ques_count: String?,
        val current_course_index: Int?,
        val current_course_write_string: String?,
        val current_course_error_option: Any,
        val latest_course_name: String,
        val latest_course_timestamp: Int,
        val qSelect: List<QSelect>
)

data class QSelect(
        val course_id: Int,
        val course_name: String
)

data class OneHistoryBean(
        val type: String,
        val course_id: String,
        val class_id: String,
        val course_name: String,
        val ques_type: String,
        val ques_count: String,
        val done_count: String,
        val done_index: String,
        val timestamp: String,
        val time: String,
        val score: String
)
