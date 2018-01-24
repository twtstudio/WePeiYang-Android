package xyz.rickygao.gpa2.api

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by rickygao on 2017/11/14.
 */
data class Response<out T>(
        val error_code: Int,
        val message: String,
        val data: T?
)

data class GpaBean(
        val stat: Stat,
        val data: List<Term>,
        val updated_at: String,
        val session: String
)

data class Stat(
        val years: List<Year>,
        val total: Total
)

data class Year(
        val year: String,
        val score: Double,
        val gpa: Double,
        val credit: Double
)

data class Total(
        val score: Double,
        val gpa: Double,
        val credit: Double
)

data class Term(
        val term: String,
        val data: List<Course>,
        val name: String,
        val stat: TermStat
)

data class Course(
        val no: String,
        val name: String,
        val type: Int,
        val credit: Double,
        val reset: Int,
        val score: Double,
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