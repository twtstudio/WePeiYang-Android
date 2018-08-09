package com.twtstudio.service.tjwhm.exam.user

import com.twt.wepeiyang.commons.experimental.cache.*
import com.twt.wepeiyang.commons.experimental.network.ServiceFactoryForExam
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET

interface UserService {
    @GET("student")
    fun getUserInfo(): Deferred<ExamUserViewModel>

    companion object : UserService by ServiceFactoryForExam()
}

val examUserLocalCache = Cache.hawk<ExamUserViewModel>("ExamUser")
val examUserRemoteCache = Cache.from(UserService.Companion::getUserInfo)
val examUserLiveData = RefreshableLiveData.use(examUserLocalCache, examUserRemoteCache)


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
        val score: Int
)