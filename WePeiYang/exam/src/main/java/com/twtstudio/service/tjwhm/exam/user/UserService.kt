package com.twtstudio.service.tjwhm.exam.user

import com.twt.wepeiyang.commons.experimental.cache.*
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.network.ServiceFactoryForExam
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("student")
    fun getUserInfo(): Deferred<ExamUserViewModel>

    @GET("special/getQues/{star_or_wrong}")
    fun getCollections(@Path("star_or_wrong") starOrWrong: String): Deferred<StarViewModel>

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
        val ques_type: String,
        val ques_id: String,
        val ques: Ques
)

data class Ques(
        val id: Int,
        val class_id: String,
        val course_id: String,
        val type: String,
        val content: String,
        val option: List<String>,
        val answer: String,
        val is_collected: Int,
        val is_mistake: Int
)