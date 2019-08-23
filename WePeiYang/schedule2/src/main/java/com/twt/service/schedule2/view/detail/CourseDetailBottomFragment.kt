package com.twt.service.schedule2.view.detail

import android.app.AliasActivity
import android.app.Dialog
import android.content.Intent
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.twt.service.schedule2.R
import com.twt.service.schedule2.extensions.getChineseCharacter
import com.twt.service.schedule2.model.Course
import com.twt.service.schedule2.model.exam.ExamTableLocalAdapter
import com.twt.service.schedule2.model.exam.addEvent
import com.twt.service.schedule2.view.adapter.CourseDetailViewModel
import com.twt.service.schedule2.view.adapter.iconLabel
import com.twt.service.schedule2.view.audit.AuditActivity
import com.twt.service.schedule2.view.audit.search.SearchResultActivity
import com.twt.service.schedule2.view.custom.AddCustomCourseActivity
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.mta.mtaClick
import com.twt.wepeiyang.commons.mta.mtaExpose
import com.twt.wepeiyang.commons.ui.rec.refreshAll
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.alert

class CourseDetailBottomFragment : BottomSheetDialogFragment() {
    companion object {
        private const val TAG_SHARE_BS_DIALOG_FRAGMENT = "CourseDetailBottomFragment"
        private val cacheFragment = CourseDetailBottomFragment()

        fun showCourseDetailBottomSheet(activity: AppCompatActivity, course: Course) {
            val fragmentManager = activity.supportFragmentManager
            var fragment = fragmentManager
                    .findFragmentByTag(TAG_SHARE_BS_DIALOG_FRAGMENT) as? CourseDetailBottomFragment
            if (fragment == null) {
                fragment = cacheFragment
            }
            if (fragment.isAdded) return
            fragment.course = course
            fragment.show(fragmentManager, TAG_SHARE_BS_DIALOG_FRAGMENT)
            mtaExpose("schedule_展示课程底部栏_${course.coursename}")
        }
    }

    var course: Course? = null

    override fun setupDialog(dialog: Dialog?, style: Int) {
        if (dialog == null) return
        val course = this.course ?: return

        val view: View = LayoutInflater.from(context).inflate(R.layout.schedule_frag_bottom_sheet, null)
        dialog.setContentView(view)

        val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet)


        val recyclerView = view.findViewById<RecyclerView>(R.id.rcv_sheet)
        val layoutManager = LinearLayoutManager(context)
//        val adapter = CourseDetailAdapter(context!!)

        recyclerView.layoutManager = layoutManager
//        recyclerView.adapter = adapter

//        adapter.refreshDataList(createCourseDetailList(course))

        val exam = runBlocking(QuietCoroutineExceptionHandler) {
            val courseID = course.classid.toString()
            val table = ExamTableLocalAdapter.getExamMapFromCache().await()
            val exam = table[courseID]
            exam
        }

        recyclerView.refreshAll {
            courseInfo(course = course)
            indicatorText("上课信息")

            val week = course.week
            course.arrangeBackup.forEach {
                iconLabel(CourseDetailViewModel(R.drawable.ic_schedule_location, "${week.start}-${week.end}周，${it.week}上课，每周${getChineseCharacter(it.day)}第${it.start}-${it.end}节\n${it.room}"))
            }

            exam?.let {
                indicatorText("考试信息")
                iconLabel(CourseDetailViewModel(R.drawable.ic_schedule_noti, "${it.type} 加油！\n${it.date} ${it.arrange} \n${it.location}#${it.seat}", {
                    try {
                        val (start, end) = exam.parseToDatePair()
                        addEvent(it.context, "${exam.name} ${exam.type}", "${exam.location}#${exam.seat}", start.time, end.time, exam)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        if (e is IllegalStateException) {
                            Toasty.error(it.context, e.message.toString()).show()
                        }
                    }
                }))
            }
            indicatorText("课程信息")
            iconLabel(CourseDetailViewModel(R.drawable.ic_schedule_other, "逻辑班号：${course.classid}\n课程编号：${course.courseid}"))
            indicatorText("自定义设置")
//            iconLabel(CourseDetailViewModel(R.drawable.ic_schedule_search, "在蹭课功能中搜索相似课程", clickBlock = {
//                mtaClick("schedule_单节课程底部弹出_${course.coursename}_搜索相似蹭课")
//                SearchResultActivity.searchCourse(it.context, course.coursename)
//            }))
            iconLabel(CourseDetailViewModel(R.drawable.ic_schedule_event, "进入蹭课功能", clickBlock = {
                mtaClick("schedule_单节课程底部弹出_${course.coursename}_进入蹭课功能")
                startActivity(Intent(context, AuditActivity::class.java))
            }))
            iconLabel(CourseDetailViewModel(R.drawable.ic_schedule_search, "在蹭课中搜索相似课程", clickBlock = {
                mtaClick("schedule_单节课程底部弹出_${course.coursename}_进入蹭课中搜索相似课程功能")
                SearchResultActivity.searchCourse(it.context, course.coursename)
            }))
            iconLabel(CourseDetailViewModel(R.drawable.ic_schedule_event, "添加自定义课程/事件", clickBlock = {
                mtaClick("schedule_单节课程底部弹出_${course.coursename}_添加自定义课程/事件")
                startActivity(Intent(context, AddCustomCourseActivity::class.java))
            }))
            iconLabel(CourseDetailViewModel(R.drawable.ic_schedule_homework, "添加课程作业/考试", clickBlock = {
                Toasty.info(it.context, "下一版本中加入").show()
            }))
            indicatorText("帮助")

            iconLabel(CourseDetailViewModel(R.drawable.ic_schedule_info, "如何使用课程表的自定义功能", {
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
        }
        recyclerView.post {
            // post方法才可以保证child被测量 否则拿到的都是0
            val arrangeSize = course.arrangeBackup.size
            val totalCollapsedCount = (if (exam == null) 2 else 4) + arrangeSize
            val childCount = layoutManager.childCount
            var totalCollapsedHeight = 0
            if (childCount > totalCollapsedCount) {
                for (i in 0 until totalCollapsedCount) {
                    totalCollapsedHeight += layoutManager.getChildAt(i).height
                }
            }
            behavior.peekHeight = totalCollapsedHeight
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

    }


}