package com.twt.service.schedule2.model.audit

import com.twt.service.schedule2.extensions.termStart
import com.twt.service.schedule2.model.AbsClasstableProvider
import com.twt.service.schedule2.model.Classtable
import com.twt.service.schedule2.model.CommonClassTable
import com.twt.service.schedule2.model.ScheduleDb
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle

object AuditCourseManager {

    fun getAuditClasstableProvider(dao: AuditCourseDao = ScheduleDb.auditCourseDao): AbsClasstableProvider {
        val auditCourseList = dao.loadAllAuditCourses().asSequence()
                .map { it.convertToCourse() }
                .toList()
        val classtable = Classtable(courses = auditCourseList, termStart = termStart)
        val realClasstableProvider = CommonClassTable(classtable)
        return realClasstableProvider
    }

    suspend fun refreshAuditClasstable(dao: AuditCourseDao = ScheduleDb.auditCourseDao) {
        val auditCourse = AuditApi.getMyAudit().awaitAndHandle{ it.printStackTrace() }?.data ?: throw IllegalStateException()
        deleteAuditCoursesLocal(dao)
        val array = auditCourse.toTypedArray()
        dao.insertAuditCourses(*array)
    }

    private fun deleteAuditCoursesLocal(dao: AuditCourseDao = ScheduleDb.auditCourseDao) {
        val courses = dao.loadAllAuditCourses().toTypedArray()
        dao.deleteAuditCourse(*courses)
    }
}