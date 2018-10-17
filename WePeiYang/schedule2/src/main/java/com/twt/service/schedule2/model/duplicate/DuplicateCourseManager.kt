package com.twt.service.schedule2.model.duplicate

import com.twt.service.schedule2.extensions.termStart
import com.twt.service.schedule2.model.AbsClasstableProvider
import com.twt.service.schedule2.model.Classtable
import com.twt.service.schedule2.model.CommonClassTable
import com.twt.service.schedule2.model.Course

object DuplicateCourseManager {
    val duplicateCourses = mutableMapOf<Int, Course>()
    fun addCourse(course: Course) {
        duplicateCourses.put(course.classid, course)
    }

    fun getDuplicateCourseProvider(): AbsClasstableProvider {
        val classtable = Classtable(courses = duplicateCourses.values.toList(), termStart = termStart)
        val commonClassTable = CommonClassTable(classtable)
        return commonClassTable
    }

}