package com.twt.service.schedule2.view.detail

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.twt.service.schedule2.R
import com.twt.service.schedule2.extensions.getChineseCharacter
import com.twt.service.schedule2.model.Course
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class CourseInfoComponent(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val courseNameTextView: TextView = itemView.findViewById(R.id.tv_course_name)
    private val courseTypeTextView: TextView = itemView.findViewById(R.id.tv_course_type)
    private val courseTeacherTextView: TextView = itemView.findViewById(R.id.tv_course_teacher)
    private val courseCreditTextView: TextView = itemView.findViewById(R.id.tv_course_credit)
    private val courseStatusTextView: TextView = itemView.findViewById(R.id.tv_course_status)

    fun bind(course: Course) {
        courseNameTextView.text = course.coursename
        courseTypeTextView.text = "${course.coursenature} - ${course.coursetype}"
        courseTeacherTextView.text = course.teacher
        if (course.credit.contains(".")) {
            courseCreditTextView.text = "${course.credit}学分"
        } else {
            courseCreditTextView.text = "${course.credit}"
        }
        courseStatusTextView.text = course.statusMessage
    }

    companion object {
        fun create(inflater: LayoutInflater, parent: ViewGroup): CourseInfoComponent {
            val view = inflater.inflate(R.layout.schedule_item_course_info, parent, false)
            return CourseInfoComponent(view)
        }
    }

}

class CourseIndicatorComponent(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val indicatorTextView: TextView = itemView.findViewById(R.id.tv_course_indicator)
    fun bind(indicator: String) {
        indicatorTextView.text = indicator
    }

    companion object {
        fun create(inflater: LayoutInflater, parent: ViewGroup): CourseIndicatorComponent {
            val view = inflater.inflate(R.layout.schedule_item_indicator, parent, false)
            return CourseIndicatorComponent(view)
        }
    }
}

class CourseDetailComponent(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val iconImageView: ImageView = itemView.findViewById(R.id.iv_item_detail)
    private val detailTextView: TextView = itemView.findViewById(R.id.tv_item_detail)
    private val container: ViewGroup = itemView.findViewById(R.id.container_item_detail)

    fun bind(viewModel: CourseDetailViewModel) {
        iconImageView.setImageResource(viewModel.imgResId)
        detailTextView.text = viewModel.content
        container.setOnClickListener {
            viewModel.clickBlock.invoke(it)
        }
    }

    companion object {
        fun create(inflater: LayoutInflater, parent: ViewGroup): CourseDetailComponent {
            val view = inflater.inflate(R.layout.schedule_item_detail, parent, false)
            return CourseDetailComponent(view)
        }
    }
}

data class CourseDetailViewModel(val imgResId: Int, val content: String, val clickBlock: (View) -> Unit = {})

/**
 * 创建BottomSheet的列表
 */
fun createCourseDetailList(course: Course): List<Any> {
    val list = mutableListOf<Any>()
    list.add(course)
    list.add("上课地点")
    val week = course.week
    course.arrangeBackup.forEach {
        list.add(CourseDetailViewModel(
                R.drawable.ic_schedule_location,
                "${week.start}-${week.end}周，${it.week}上课，每周${getChineseCharacter(it.day)}第${it.start}-${it.end}节\n${it.room}"
        ))
    }
    list.add("其他信息")
    list.add(CourseDetailViewModel(R.drawable.ic_schedule_other, "逻辑班号：${course.classid}\n课程编号：${course.courseid}"))
    list.add("自定义（开发中 敬请期待）")
    list.add(CourseDetailViewModel(R.drawable.ic_schedule_search, "在蹭课功能中搜索相似课程"))
    list.add(CourseDetailViewModel(R.drawable.ic_schedule_event, "添加自定义课程/事件"))
    list.add(CourseDetailViewModel(R.drawable.ic_schedule_homework, "添加课程作业/考试"))
    list.add("帮助")
    list.add(CourseDetailViewModel(R.drawable.ic_schedule_info, "如何使用课程表的自定义功能",{
        it.context.alert {
            title = "课程表使用帮助"
            message = "蹭课，自定义课程，作业考试添加还没有完全完成,所以不可点击。\n周数切换最近就会添加上去 \n\n" +
                    "多节：在此课程方格中存在多节课程，可能是冲突，也可能是非本周（一般都是非本周），多节课程点击弹出的底部栏可以通过左右滑动来查看不同的课程详情\n\n" +
                    "[非本周]：课程表体现为灰白色课程，有这个课程但是这周不上\n\n" +
                    "[蹭课]：来自于我选择的蹭课\n\n" +
                    "[冲突]：在同一个时间段内有两门课程或者时间（不同于非本周），冲突一般来自于蹭课冲突或者自定义课程和已有课程冲突，" +
                    "当然也可能是重修课补修课的冲突，遇见此类课程要格外留意，避免漏掉课程耽误学习\n\n" +
                    "为什么会出现多个同名课程？ 因为教务系统在同一课程由不同老师教授（或者其他迷之情况的时候）会把一门课程拆分成多个课程返回，微北洋遵照教务系统的返回数据"
        }.show()
    }))
    return list
}