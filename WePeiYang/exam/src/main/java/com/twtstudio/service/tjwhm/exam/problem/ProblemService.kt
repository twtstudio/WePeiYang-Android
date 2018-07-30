package com.twtstudio.service.tjwhm.exam.problem

import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.network.ServiceFactoryForExam
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.Serializable

interface ProblemService {
    @GET("remember/getAllId/{class_id}/{type}")
    fun getIDs(@Path("class_id") classId: String, @Path("type") type: String): Deferred<IdsViewModel>

    @GET("remember/getQuesById/{class_id}/{type}/{problem_id}")
    fun getProblem(@Path("class_id") classId: String, @Path("type") type: String, @Path("problem_id") problemID: String): Deferred<ProblemViewModel>

    @GET("exercise/getQues/{class_id}")
    fun getTestProblems(@Path("class_id") classId: String): Deferred<TestViewModel>

    companion object : ProblemService by ServiceFactoryForExam()
}

fun getIDs(classId: String, type: String, callback: suspend (RefreshState<IdsViewModel>) -> Unit) =
        launch(UI) {
            ProblemService.getIDs(classId, type).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                callback(RefreshState.Success(it))
            }
        }

fun getProblem(classId: String, type: String, problemID: String, callback: suspend (RefreshState<ProblemViewModel>) -> Unit) =
        launch(UI) {
            ProblemService.getProblem(classId, type, problemID).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                callback(RefreshState.Success(it))
            }
        }

fun getTestProblems(classId: String, callback: suspend (RefreshState<TestViewModel>) -> Unit) =
        launch(UI) {
            ProblemService.getTestProblems(classId).awaitAndHandle {
                callback(RefreshState.Failure(it))
            }?.let {
                callback(RefreshState.Success(it))
            }
        }

data class IdsViewModel(
        val status: Int,
        val message: String,
        val ques: List<Que>
)

data class Que(
        val id: Int
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
        val answer: String
)


data class TestViewModel(
        val status: Int,
        val message: String,
        val time: Int,
        val data: List<TestOneProblemData>
)

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
)

data class Result(
        val ques_id: Int,
        val ques_type: Int,
        val is_true: Int,
        val answer: String,
        val true_answer: String
)


data class UpdateResultViewModel(
        val id: Int,
        val answer: String,
        val type: Int
)