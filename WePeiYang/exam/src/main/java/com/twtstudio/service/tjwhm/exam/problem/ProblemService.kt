package com.twtstudio.service.tjwhm.exam.problem

import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactoryForExam
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import retrofit2.http.*
import java.io.Serializable

interface ProblemService {
    @GET("course/{lesson_id}")
    fun getLessonInfo(@Path("lesson_id") lessonID: String): Deferred<LessonInfoViewModel>

    @GET("remember/getAllId/{lesson_id}/{type}")
    fun getIDs(@Path("lesson_id") lessonID: String, @Path("type") type: String): Deferred<CommonBody<List<Int>>>

    @GET("remember/getQuesById/{lesson_id}/{type}/{problem_id}")
    fun getProblem(@Path("lesson_id") lessonID: String, @Path("type") type: String, @Path("problem_id") problemID: String): Deferred<CommonBody<ProblemBean>>

    @GET("exercise/getQues/{lesson_id}")
    fun getTestProblems(@Path("lesson_id") lessonID: String): Deferred<TestViewModel>

    @POST("exercise/getScore/{lesson_id}/{time}")
    fun uploadResult(@Path("lesson_id") lessonID: String, @Path("time") time: String, @Body answerList: List<UpdateResultViewModel>): Deferred<ScoreViewModel>

    companion object : ProblemService by ServiceFactoryForExam()
}

fun getLessonInfo(lessonID: String, callback: suspend (RefreshState<LessonInfoViewModel>) -> Unit) =
        launch(UI) {
            ProblemService.getLessonInfo(lessonID).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                //                if (it.error_code == 0)
                callback(RefreshState.Success(it))
            }
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

fun getTestProblems(lessonID: String, callback: suspend (RefreshState<TestViewModel>) -> Unit) =
        launch(UI) {
            ProblemService.getTestProblems(lessonID).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                callback(RefreshState.Success(it))
            }
        }

fun getScore(lessonID: String, time: String, answerList: List<UpdateResultViewModel>, callback: suspend (RefreshState<ScoreViewModel>) -> Unit) =
        launch(UI) {
            ProblemService.uploadResult(lessonID, time, answerList).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                callback(RefreshState.Success(it))
            }
        }


data class LessonInfoViewModel(
        val status: Int,
        val message: String,
        val data: LessonInfoData
)

data class LessonInfoData(
        val id: Int,
        val course_name: String,
        val ques_num: String,
        val single_ques_num: String,
        val multi_ques_num: String,
        val decide_ques_num: String
)


data class ProblemBean(
        val ques_id: Int,
        val course_id: String,
        val ques_type: String,
        val content: String,
        val option: List<String>,
        val answer: String,
        val is_collected: Int,
        val is_mistake: Int
)

data class ProblemViewModel(
        val status: Int,
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


data class TestViewModel(
        val status: Int,
        val message: String,
        val time: Int,
        val data: List<TestOneProblemData>
) : Serializable

data class TestOneProblemData(
        val id: Int,
        val course_id: String,
        val type: Int,
        val content: String,
        val option: List<String>
) : Serializable


data class ScoreViewModel(
        val score: Int,
        val result: List<Result>
) : Serializable

data class Result(
        val ques_id: Int,
        val ques_type: Int,
        val is_true: Int,
        val answer: String,
        val true_answer: String
) : Serializable


data class UpdateResultViewModel(
        val id: Int,
        val answer: String,
        val type: Int
)