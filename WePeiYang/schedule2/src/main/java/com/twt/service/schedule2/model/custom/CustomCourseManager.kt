package com.twt.service.schedule2.model.custom

import com.twt.service.schedule2.extensions.termStart
import com.twt.service.schedule2.model.*

/**
 * 建议在协程中异步调用
 */
object CustomCourseManager {

    fun addCustomCourse(name: String, teacher: String, arrange: List<Arrange>, week: Week, ext: String = "") {
        val customCourse = CustomCourse(name, teacher, ext,arrange, week)
        addCustomCourse(customCourse)
    }

    fun addCustomCourse(customCourse: CustomCourse, dao: CustomCourseDao = ScheduleDb.customCourseDao) {
        dao.insertCourse(customCourse)
    }

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

    val customCourseMapper: (CustomCourse) -> Course = {
        Course(coursename = it.name, teacher = it.teacher, arrangeBackup = it.arrange, week = it.week, ext = it.ext)
    }

    /**
     * 应该异步
     * 把自定义课程从数据库拿出 并且转换成Classtable
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