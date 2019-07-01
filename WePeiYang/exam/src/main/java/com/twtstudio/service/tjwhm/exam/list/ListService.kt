package com.twtstudio.service.tjwhm.exam.list

import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import com.twtstudio.service.tjwhm.exam.commons.EXAM_BASE_URL
import kotlinx.coroutines.*
import kotlinx.coroutines.android.Main
import kotlinx.coroutines.android.UI
import retrofit2.http.GET
import retrofit2.http.Path

interface ListService {
    @GET("${EXAM_BASE_URL}class/{classId}")
    fun getLessonList(@Path("classId") classId: String): Deferred<CommonBody<List<LessonBean>>>

    @GET("${EXAM_BASE_URL}search/{key}")
    fun findLessonList(@Path("key") key: String): Deferred<CommonBody<List<LessonBean>>>

    @GET("${EXAM_BASE_URL}course/{lessonID}")
    fun getLessonInfo(@Path("lessonID") lessonID: String): Deferred<CommonBody<LessonInfoBean>>

    companion object : ListService by ServiceFactory()
}

fun getList(lessonID: String, callback: suspend (RefreshState<CommonBody<List<LessonBean>>>) -> Unit) =
        GlobalScope.launch(Dispatchers.Main) {
            ListService.getLessonList(lessonID).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                callback(RefreshState.Success(it))
            }
        }

fun findLesson(key: String, callback: suspend (RefreshState<CommonBody<List<LessonBean>>>) -> Unit) =
        GlobalScope.launch(Dispatchers.Main) {
            ListService.findLessonList(key).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                callback(RefreshState.Success(it))
            }
        }

fun getLessonInfo(lessonID: String, callback: suspend (RefreshState<CommonBody<LessonInfoBean>>) -> Unit) =
        GlobalScope.launch(Dispatchers.Main) {
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
