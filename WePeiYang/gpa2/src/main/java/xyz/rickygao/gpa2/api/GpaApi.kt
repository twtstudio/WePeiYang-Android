package xyz.rickygao.gpa2.api

import retrofit2.http.GET
import rx.Observable


/**
 * Created by rickygao on 2017/11/9.
 */

data class Response<out T>(val error_code: Int, val message: String, val data: T)

data class GpaBean(val stat: Stat, val data: List<Term>, val updated_at: String, val session: String)

data class Stat(val years: List<Year>, val total: Total)

data class Year(val year: String, val score: Double, val gpa: Double, val credit: Double)

data class Total(val score: Double, val gpa: Double, val credit: Double)

data class Term(val term: String, val data: List<Course>, val name: String, val stat: TermStat)

data class Course(val no: String, val name: String, val type: Int, val credit: Double, val reset: Int, val score: Double, val evaluate: Evaluate)

data class Evaluate(val lesson_id: String, val term: String, val union_id: String, val course_id: String)

data class TermStat(val score: Double, val gpa: Double, val credit: Double)

interface GpaApi {
    @GET("gpa")
    fun getGpa(): Observable<Response<GpaBean>>
}