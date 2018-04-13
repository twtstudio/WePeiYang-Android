package com.twt.service.schedule2.view.schedule

import android.app.Activity
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
import android.util.DisplayMetrics
import android.widget.FrameLayout
import com.haozhang.lib.SlantedTextView


/**
 * 使用GridLayoutManager (HORIZONTAL) span = 12
 */
class ScheduleAdapter(val context: Context) : RecyclerView.Adapter<ScheduleAdapter.CourseViewHolder>() {
    val courseList = mutableListOf<Course>()
    var firstColumnSize = 1
    val firstRowIndexList = mutableListOf<Int>()
    var displayType = ScheduleDisplayType.SEVENDAYS

    /**
     * 外部的点击事件统一传递
     */
    var clickListener: (Course) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.schedule_item_course, parent, false)
        if (viewType == ScheduleDisplayType.SEVENDAYS) {
            view.flatViewHolder(7,parent)
        } else {
            view.flatViewHolder(5,parent)
        }
        return CourseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courseList[position]
        holder.bind(course)
        holder.cardView.setOnClickListener {
            clickListener.invoke(course)
        }
    }

    /**
     * 传入二维列表
     */
    fun refreshCourseListFlat(courses: List<List<Course>>) {
        courseList.removeAll { true }
        if (courses.isNotEmpty()) {
            firstColumnSize = courses[0].size
        }
        courses.forEach {
            firstRowIndexList.add(courseList.size)
            courseList.addAll(it)
        }
        displayType = when (firstRowIndexList.size) {
            5 -> ScheduleDisplayType.FIVEDAYS
            7 -> ScheduleDisplayType.SEVENDAYS
            else -> ScheduleDisplayType.SEVENDAYS
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
                    text += "[非本周]"
                }
                if (course.coursetype == "蹭课") {
                    text += "[蹭课]"
                }
                if (course.ext == "重修") {
                    text += "[光荣重修！]"
                }
                course.statusMessage = String(text.toByteArray()) // 拷贝一个（不过按理说不拷贝也可以吧）

                text += "${course.coursename}\n@${course.arrange[0].room} "
                /**
                 * 渲染多节角标
                 */
                if (course.next.size > 0) {
                    val view = LayoutInflater.from(cardView.context).inflate(R.layout.schedule_item_course_slant,cardView,false)
                    val slantedTextView: SlantedTextView = view.findViewById(R.id.tv_course_slant)
                    cardView.addView(view)
                }
                /**
                 * 渲染Next列表里面的课程信息
                 */
                if (course.next.size > 0) {
                    course.next.forEach {
                        var tempText = ""
                        if (!it.weekAvailable) {
                            tempText += "[非本周]"
                        } else {
                            tempText += "[冲突]"
                        }
                        if (it.coursetype == "蹭课") {
                            tempText += "[蹭课]"
                        }
                        if (it.ext == "重修") {
                            tempText += "[光荣重修！]"
                        }
                        it.statusMessage = tempText
                    }
                }
                textView.text = text
                if (course.weekAvailable) {
                    textView.setTextColor(Color.WHITE)
                    cardView.setCardBackgroundColor(CommonContext.application.resources.getColor(course.courseColor))
                } else {
                    cardView.setCardBackgroundColor(CommonContext.application.resources.getColor(R.color.schedule_background_gray))
                    textView.setTextColor(CommonContext.application.resources.getColor(R.color.schedule_gray))
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return displayType
    }


    private fun getScreenWidth(): Int {
        val dm = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(dm)
        //宽度 dm.widthPixels
        //高度 dm.heightPixels
        return dm.widthPixels
    }

    private fun View.flatViewHolder(spanSize: Int, parent: ViewGroup) {
        val availableWidth = parent.width - parent.paddingStart
        val perItemWith = availableWidth / spanSize
        val layoutParams: GridLayoutManager.LayoutParams = this.layoutParams as GridLayoutManager.LayoutParams
        layoutParams.width = perItemWith - layoutParams.marginStart - layoutParams.marginEnd
        this.layoutParams = layoutParams

    }

    /**
     * 五天填充还是七天填充
     */
    class ScheduleDisplayType {
        companion object {
            val FIVEDAYS = 0
            val SEVENDAYS = 1
        }
    }

}

class CourseSpanSizeLookup(val courses: List<Course>) : GridLayoutManager.SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int {
        val span = courses[position].arrange[0].let { it.end - it.start + 1 }
        return span
    }
}

