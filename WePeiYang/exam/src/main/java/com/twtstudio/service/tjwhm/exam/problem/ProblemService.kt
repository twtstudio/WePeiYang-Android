package com.twtstudio.service.tjwhm.exam.problem

import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactoryForExam
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import okhttp3.MultipartBody
import retrofit2.http.*
import java.io.Serializable

interface ProblemService {
    @GET("remember/getAllId/{lesson_id}/{type}")
    fun getIDs(@Path("lesson_id") lessonID: String, @Path("type") type: String): Deferred<CommonBody<List<Int>>>

    @GET("remember/getQuesById/{lesson_id}/{type}/{problem_id}")
    fun getProblem(@Path("lesson_id") lessonID: String, @Path("type") type: String, @Path("problem_id") problemID: String): Deferred<CommonBody<ProblemBean>>

    @GET("exercise/getQues/{lesson_id}")
    fun getTestProblems(@Path("lesson_id") lessonID: String): Deferred<CommonBody<TestBean>>

    @POST("exercise/getScore/{lesson_id}/{time}")
    fun uploadResult(@Path("lesson_id") lessonID: String, @Path("time") time: String, @Body answerList: List<UpdateResultViewModel>): Deferred<CommonBody<ScoreBean>>

    @Multipart
    @POST("remember/mark")
    fun mark(@Part list: MutableList<MultipartBody.Part>) // : Deferred<CommonBody<Unit>>

    @Multipart
    @POST("remember/current_course/write")
    fun write(@Part list: MutableList<MultipartBody.Part>) // : Deferred<CommonBody<Unit>>

    @GET("student/exercise_result")
    fun getTestHistory(@Query("time") time: String): Deferred<CommonBody<ScoreBean>>

    companion object : ProblemService by ServiceFactoryForExam()
}

fun getIDs(lessonID: String, type: String, callback: suspend (RefreshState<CommonBody<List<Int>>>) -> Unit) =
        launch(UI) {
            ProblemService.getIDs(lessonID, type).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                if (it.error_code == 0 && it.data != null)
                    callback(RefreshState.Success(it))
            }
        }

fun getProblem(lessonID: String, type: String, problemID: String, callback: suspend (RefreshState<CommonBody<ProblemBean>>) -> Unit) =
        launch(UI) {
            ProblemService.getProblem(lessonID, type, problemID).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                if (it.error_code == 0 && it.data != null)
                    callback(RefreshState.Success(it))
            }
        }

fun getTestProblems(lessonID: String, callback: suspend (RefreshState<CommonBody<TestBean>>) -> Unit) =
        launch(UI) {
            ProblemService.getTestProblems(lessonID).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                callback(RefreshState.Success(it))
            }
        }

fun getScore(lessonID: String, time: String, answerList: List<UpdateResultViewModel>, callback: suspend (RefreshState<CommonBody<ScoreBean>>) -> Unit) =
        launch(UI) {
            ProblemService.uploadResult(lessonID, time, answerList).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                callback(RefreshState.Success(it))
            }
        }

fun mark(courseID: String, quesType: String, quesID: String, index: String) = launch(UI) {
    val list = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("course_id", courseID)
            .addFormDataPart("ques_type", quesType)
            .addFormDataPart("ques_id", quesID)
            .addFormDataPart("index", index)
            .build()
            .parts()
    ProblemService.mark(list)
}

fun write(courseID: String, quesType: String, index: String) = launch(UI) {
    val list = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("course_id", courseID)
            .addFormDataPart("ques_type", quesType)
            .addFormDataPart("index", index)
            .addFormDataPart("is_write", 1.toString())
            .build().parts()
    ProblemService.write(list)
}

fun getTestHistory(time: String, callback: suspend (RefreshState<CommonBody<ScoreBean>>) -> Unit) =
        launch(UI) {
            ProblemService.getTestHistory(time).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                callback(RefreshState.Success(it))
            }
        }

data class ProblemBean(
        val ques_id: Int,
        val class_id: String,
        val course_id: String,
        val ques_type: String,
        val content: String,
        val option: List<String>,
        val answer: String,
        val is_collected: Int,
        val is_mistake: Int,
        val error_option: String
)

data class TestBean(
        val time: Int,
        val timestamp: Int,
        val question: List<TestProblemBean>
) : Serializable

data class TestProblemBean(
        val ques_id: Int,
        val course_id: String,
        val ques_type: Int,
        val content: String,
        val option: List<String>,
        val is_collected: Int
) : Serializable

data class UpdateResultViewModel(
        val id: Int,
        val answer: String,
        val type: Int
)


data class ScoreBean(
        val correct_num: Int,
        val course_id: String,
        val error_num: Int,
        val not_done_num: Int,
        val result: List<ResultBean>,
        val score: Int,
        val time: String,
        val timestamp: Long
) : Serializable

data class ResultBean(
        val answer: String,
        val is_collected: Int,
        val is_done: Int,
        val is_true: Int,
        val ques_content: String,
        val ques_id: Int,
        val ques_option: List<String>,
        val ques_type: Int,
        val true_answer: String
) : Serializable
