package com.twt.service.schedule2.view.schedule

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.service.schedule2.R
import com.twt.service.schedule2.model.Course
import com.twt.wepeiyang.commons.experimental.CommonContext

/**
 * 使用GridLayoutManager (HORIZONTAL) span = 12
 */
class ScheduleAdapter(val context: Context) : RecyclerView.Adapter<ScheduleAdapter.CourseViewHolder>() {
    val courseList = mutableListOf<Course>()


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CourseViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.schedule_item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courseList[position]
        holder.bind(course)
    }

    fun refreshCourseList(courses: List<Course>) {
        courseList.removeAll { true }
        courseList.addAll(courses)
        notifyDataSetChanged()
    }

    /**
     * 传入二维列表
     */
    fun refreshCourseListFlat(courses: List<List<Course>>) {
        courseList.removeAll { true }
        courses.forEach {
            courseList.addAll(it)
        }
        notifyDataSetChanged()
    }


    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cv_item_course)
        val textView: TextView = itemView.findViewById(R.id.tv_item_course)

        fun bind(course: Course) {
            if (course.coursename == "空") {
                cardView.visibility = View.INVISIBLE
            } else {
                cardView.visibility = View.VISIBLE
                var text = ""
                if (!course.weekAvailable) {
                    text += "[这周不上]"
                }
                if (course.coursetype == "蹭课") {
                    text += "[蹭课]"
                }
                text += "${course.coursename}\n@${course.arrange[0].room} "
                if (course.next.size > 0) {
                    text += "\n Next: "
                    course.next.forEach {
                        text += it.coursename + "\n"
                    }
                }
                textView.text = text
                cardView.setCardBackgroundColor(CommonContext.application.resources.getColor(course.courseColor))
            }
        }
    }

}

class CourseSpanSizeLookup(val courses: List<Course>) : GridLayoutManager.SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int {
        if(courses[position].arrange.size == 0) {
            println(courses[position])
        }
        val span = courses[position].arrange[0].let { it.end - it.start +1 }
        return span
    }
}

