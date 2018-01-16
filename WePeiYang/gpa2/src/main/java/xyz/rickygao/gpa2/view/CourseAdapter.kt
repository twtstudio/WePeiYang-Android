package xyz.rickygao.gpa2.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import xyz.rickygao.gpa2.R

/**
 * Created by rickygao on 2017/11/14.
 */
class CourseAdapter(val inflater: LayoutInflater) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    companion object {
        const val SORT_DEFAULT = 0
        const val SORT_BY_SCORE_DESC = 1
        const val SORT_BY_CREDIT_DESC = 2
    }

    var courses: MutableList<Course>? = null
        set(value) {
            field = value
            ensureCourses()
        }

    var sortMode = SORT_DEFAULT
        set(value) {
            if (field == value) return
            field = value
            ensureCourses()
        }

    private fun ensureCourses() {
        when (sortMode) {
            SORT_BY_CREDIT_DESC ->
                courses?.sortByDescending(Course::credit)
            SORT_BY_SCORE_DESC ->
                courses?.sortByDescending(Course::score)
            else -> return
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CourseViewHolder {
        val itemView = inflater.inflate(R.layout.gpa2_item_course, parent, false)
        return CourseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CourseViewHolder?, position: Int) {
        holder?.course = courses?.get(position)
    }

    override fun getItemCount(): Int = courses?.size ?: 0

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTv = itemView.findViewById<TextView>(R.id.tv_course_name)
        private val typeTv = itemView.findViewById<TextView>(R.id.tv_course_type)
        private val creditTv = itemView.findViewById<TextView>(R.id.tv_course_credit)
        private val scoreTv = itemView.findViewById<TextView>(R.id.tv_course_score)

        var course: Course? = null
            set(value) {
                field = value
                nameTv.text = value?.name
                typeTv.text = when (value?.type) {
                    0 -> "必修"
                    else -> "未知"
                }
                creditTv.text = "学分：${value?.credit}"
                scoreTv.text = "成绩：${value?.score?.takeIf { it > 0 } ?: "未评价"}"
            }

    }

    data class Course(val name: String, val type: Int, val credit: Double, val score: Double)
}