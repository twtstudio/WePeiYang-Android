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
    fun getClassList(@Path("classId") classId: String): Deferred<CommonBody<List<LessonBean>>>

    @GET("search/{key}")
    fun findClassList(@Path("key") key: String): Deferred<CommonBody<List<LessonBean>>>

    companion object : ListService by ServiceFactoryForExam()
}

fun getList(classId: String, callback: suspend (RefreshState<CommonBody<List<LessonBean>>>) -> Unit) =
        launch(UI) {
            ListService.getClassList(classId).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                callback(RefreshState.Success(it))
            }
        }

fun findClass(key: String, callback: suspend (RefreshState<CommonBody<List<LessonBean>>>) -> Unit) =
        launch(UI) {
            ListService.findClassList(key).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                callback(RefreshState.Success(it))
            }
        }


data class LessonBean(
        val course_id: Int,
        val course_name: String
)


