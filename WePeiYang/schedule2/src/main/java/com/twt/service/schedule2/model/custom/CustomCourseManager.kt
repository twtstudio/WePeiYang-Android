package com.twt.service.schedule2.model.custom

import com.twt.service.schedule2.extensions.termStart
import com.twt.service.schedule2.model.*


/**
 * 建议在协程中异步调用,修改本地自定义课程的数据库
 */
object CustomCourseManager {

    fun addCustomCourse(name: String, teacher: String, arrange: List<Arrange>, week: Week, ext: String = "") {
        val customCourse = CustomCourse(name, teacher, ext,arrange, week)
        addCustomCourses(customCourse)
    }

    fun addCustomCourses(customCourse: CustomCourse, dao: CustomCourseDao = ScheduleDb.customCourseDao) {
        dao.insertCourses(customCourse)
    }

    fun getAllCustomCourses(dao: CustomCourseDao = ScheduleDb.customCourseDao) = dao.loadAllCourses()

    fun getAllCustomCoureseLiveData(dao: CustomCourseDao = ScheduleDb.customCourseDao) = dao.loadAllCustomCoursesLiveData()

    fun deleteCustomCourse(customCourse: CustomCourse, dao: CustomCourseDao = ScheduleDb.customCourseDao) {
        dao.deleteCourses(customCourse)
    }

    /**
     * 别乱调用... 要不凉了啊
     */
    fun deleteAllCustomCourse(dao: CustomCourseDao = ScheduleDb.customCourseDao) {
        val courses = dao.loadAllCourses()
        courses.forEach {
            dao.deleteCourses(it)
        }
    }

    /**
     * 获取自定义课程信息并更新数据库
     */
    fun refreshCustomClasstable(dao: CustomCourseDao = ScheduleDb.customCourseDao) {
        val customCourse = dao.loadAllCourses()
        CustomCourseManager.deleteAllCustomCourse(dao)
        val array = customCourse.toTypedArray()
        array.forEach {
            dao.insertCourse(it)
        }
    }

    val customCourseMapper: (CustomCourse) -> Course = {
        Course(coursename = it.name, teacher = it.teacher, arrangeBackup = it.arrange, week = it.week, ext = it.ext)
    }

    /**
     * 应该异步
     * 从数据库拉取自定义课程信息 并且转换成Classtable,在TotalCourseManager调用
     */
    fun getCustomClasstableProvider(dao: CustomCourseDao = ScheduleDb.customCourseDao): AbsClasstableProvider {
        val customCourseList = dao.loadAllCourses().asSequence()
                .map(customCourseMapper)
                .toList()
        val classtable = Classtable(courses = customCourseList,termStart = termStart)
        val realClasstableProvider = CommonClassTable(classtable)
        return realClasstableProvider
    }
}