package com.twtstudio.service.tjwhm.exam.user

import com.twt.wepeiyang.commons.experimental.cache.*
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.network.ServiceFactoryForExam
import com.twtstudio.service.tjwhm.exam.commons.BaseBean
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import okhttp3.MultipartBody
import retrofit2.http.*

interface UserService {
    @GET("student")
    fun getUserInfo(): Deferred<ExamUserViewModel>

    @GET("special/getQues/{star_or_wrong}")
    fun getCollections(@Path("star_or_wrong") starOrWrong: String): Deferred<StarViewModel>

    @Multipart
    @POST("special/addQues/{star_or_wrong}")
    fun addCollection(@Path("star_or_wrong") starOrWrong: String, @Part list: MutableList<MultipartBody.Part>): Deferred<BaseBean<Nothing>>

    @Multipart
    @POST("special/deleteQues/{star_or_wrong}")
    fun deleteCollection(@Path("star_or_wrong") starOrWrong: String, @Part list: MutableList<MultipartBody.Part>): Deferred<BaseBean<Nothing>>

    companion object : UserService by ServiceFactoryForExam()
}

val examUserLocalCache = Cache.hawk<ExamUserViewModel>("ExamUser")
val examUserRemoteCache = Cache.from(UserService.Companion::getUserInfo)
val examUserLiveData = RefreshableLiveData.use(examUserLocalCache, examUserRemoteCache)

fun getCollections(starOrWrong: String, callback: suspend (RefreshState<StarViewModel>) -> Unit) =
        launch(UI) {
            UserService.getCollections(starOrWrong).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                callback(RefreshState.Success(it))
            }
        }

fun addCollection(starOrWrong: String, list: MutableList<MultipartBody.Part>, callback: suspend (RefreshState<BaseBean<Nothing>>) -> Unit) =
        launch(UI) {
            UserService.addCollection(starOrWrong, list).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                callback(RefreshState.Success(it))
            }
        }

fun deleteCollection(starOrWrong: String, list: MutableList<MultipartBody.Part>, callback: suspend (RefreshState<BaseBean<Nothing>>) -> Unit) =
        launch(UI) {
            UserService.deleteCollection(starOrWrong, list).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                callback(RefreshState.Success(it))
            }
        }

data class ExamUserViewModel(
        val status: Int,
        val message: String,
        val data: Data
)

data class Data(
        val id: Int,
        val twt_name: String,
        val user_number: String,
        val type: String,
        val avatar_url: String,
        val title: Title,
        val ques_message: QuesMessage,
        val history: History
)

data class QuesMessage(
        val done_number: String,
        val error_number: String,
        val remember_course_number: Int,
        val remember_number: String,
        val collect_number: String
)

data class Title(
        val title_name: String
)

data class History(
        val status: Int,
        val history: List<OneHistoryData>
)

data class OneHistoryData(
        val type: Int,
        val date: String,
        val course_id: String,
        val ques_type: String,
        val score: Int,
        val course_name: String
)


data class StarViewModel(
        val status: Int,
        val tid: String,
        val ques: List<Que>
)

data class Que(
        val id: Int,
        val class_id: String,
        val course_id: String,
        val type: String,
        val content: String,
        val option: List<String>,
        val answer: String,
        val is_collected: Int,
        val is_mistake: Int,
        val message: String,
        val error_option: String
)
