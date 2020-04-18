package xyz.rickygao.gpa2.service

import android.os.Parcel
import android.os.Parcelable
import com.twt.wepeiyang.commons.experimental.cache.*
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import kotlinx.coroutines.*
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import xyz.rickygao.gpa2.spider.GpaSpider


/**
 * Created by rickygao on 2017/11/9.
 */
interface GpaService {

    @GET("v1/gpa")
    fun get(): Deferred<CommonBody<GpaBean>>

    @FormUrlEncoded
    @POST("v1/gpa/evaluate")
    fun evaluate(@FieldMap params: Map<String, String>): Deferred<CommonBody<String>>

    companion object : GpaService by ServiceFactory()
}

val GpaLocalCache = Cache.hawk<GpaBean>("GPA")
val GpaRemoteCache = Cache.from { GpaSpider.getGpa("3017218142", "Why_1103") }.map(GpaSpider::parseHtml)
val GpaLiveData = RefreshableLiveData.use(GpaLocalCache, GpaRemoteCache)

internal fun postEvaluate(evaluate: Evaluate, q1: Int, q2: Int, q3: Int, q4: Int, q5: Int, note: String, callback: suspend (String) -> (Unit)) {
    GlobalScope.launch(Dispatchers.Main) {
        val params = mapOf(
                "token" to GpaLiveData.value?.session.orEmpty(),
                "lesson_id" to evaluate.lesson_id,
                "union_id" to evaluate.union_id,
                "course_id" to evaluate.course_id,
                "term" to evaluate.term,
                "q1" to q1.toString(),
                "q2" to q2.toString(),
                "q3" to q3.toString(),
                "q4" to q4.toString(),
                "q5" to q5.toString(),
                "note" to note
        )

//                    .apply {
//                    put("t", Calendar.getInstance().timeInMillis.toString())
//                    val paramsString = JniUtils.getInstance().appKey +
//                            entries.map { it.key + it.value }.reduce(String::plus) +
//                            JniUtils.getInstance().appSecret
//                    val sign = String(Hex.encodeHex(DigestUtils.sha1(paramsString))).toUpperCase()
//                    put("sign", sign)
//                    put("app_key", ServiceFactory.APP_KEY)
//            }

        GpaService.evaluate(params).awaitAndHandle { callback(it.message.orEmpty()) }?.let { callback(it.message) }
    }
}

data class GpaBean(
        val stat: Stat,
        val data: List<Term>,
        val updated_at: String,
        val session: String
)

data class Stat(
        val years: List<Year>?,
        val total: Total
)

data class Year(
        val year: String,
        val score: Double,
        val gpa: Double,
        val credit: Double
)

data class Total(
        var score: Double,
        var gpa: Double,
        var credit: Double
)

data class Term(
        val term: String,
        val data: List<Course>,
        var name: String,
        val stat: TermStat
)

data class Course(
        val no: String,
        val name: String,
        val type: Int,
        val credit: Double,
        val reset: Int,
        val score: Double,
        val gpa: Double,
        val evaluate: Evaluate?
)

// Parcelable helps?
data class Evaluate(
        val lesson_id: String,
        val term: String,
        val union_id: String,
        val course_id: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) = parcel.run {
        writeString(lesson_id)
        writeString(term)
        writeString(union_id)
        writeString(course_id)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Evaluate> {
        override fun createFromParcel(parcel: Parcel): Evaluate = Evaluate(parcel)
        override fun newArray(size: Int): Array<Evaluate?> = arrayOfNulls(size)
    }
}

data class TermStat(
        val score: Double,
        val gpa: Double,
        val credit: Double
)