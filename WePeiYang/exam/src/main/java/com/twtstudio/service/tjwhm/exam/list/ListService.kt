package com.twtstudio.service.tjwhm.exam.list

import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactoryForExam
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import retrofit2.http.GET
import retrofit2.http.Path

interface ListService {
    @GET("class/{classId}")
    fun getLessonList(@Path("classId") classId: String): Deferred<CommonBody<List<LessonBean>>>

    @GET("search/{key}")
    fun findLessonList(@Path("key") key: String): Deferred<CommonBody<List<LessonBean>>>

    @GET("course/{lessonID}")
    fun getLessonInfo(@Path("lessonID") lessonID: String): Deferred<CommonBody<LessonInfoBean>>

    companion object : ListService by ServiceFactoryForExam()
}

fun getList(lessonID: String, callback: suspend (RefreshState<CommonBody<List<LessonBean>>>) -> Unit) =
        launch(UI) {
            ListService.getLessonList(lessonID).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                callback(RefreshState.Success(it))
            }
        }

fun findLesson(key: String, callback: suspend (RefreshState<CommonBody<List<LessonBean>>>) -> Unit) =
        launch(UI) {
            ListService.findLessonList(key).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                callback(RefreshState.Success(it))
            }
        }

fun getLessonInfo(lessonID: String, callback: suspend (RefreshState<CommonBody<LessonInfoBean>>) -> Unit) =
        launch(UI) {
            ListService.getLessonInfo(lessonID).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                callback(RefreshState.Success(it))
            }
        }

data class LessonBean(
        val course_id: Int,
        val course_name: String
)


data class LessonInfoBean(
        val id: Int,
        val course_name: String,
        val ques_num: String,
        val single_num: String,
        val single_done_count: Int,
        val multi_done_count: Int,
        val decide_done_count: Int,
        val multi_num: String,
        val decide_num: String,
        val single_ques_index: Int,
        val multi_ques_index: Int,
        val decide_ques_index: Int
)

