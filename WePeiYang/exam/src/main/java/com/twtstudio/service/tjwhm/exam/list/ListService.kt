package com.twtstudio.service.tjwhm.exam.list

import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.network.ServiceFactoryForExam
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import retrofit2.http.GET
import retrofit2.http.Path

interface ListService {
    @GET("class/{classId}")
    fun getClassList(@Path("classId") classId: String): Deferred<ListViewModel>

    companion object : ListService by ServiceFactoryForExam()
}

fun getList(classId: String, callback: suspend (RefreshState<ListViewModel>) -> Unit) {
    launch(UI) {
        ListService.getClassList(classId).awaitAndHandle {
            callback(RefreshState.Failure(it))
        }?.let {
            callback(RefreshState.Success(it))
        }
    }
}

data class ListViewModel(
        val status: Int,
        val message: String,
        val date: List<LessonData>
)

data class LessonData(
        val id: Int,
        val course_name: String
)


