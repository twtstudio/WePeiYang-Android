package com.twt.service.schedule2.model.audit

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.twt.service.schedule2.extensions.termStart
import com.twt.service.schedule2.model.AbsClasstableProvider
import com.twt.service.schedule2.model.Classtable
import com.twt.service.schedule2.model.CommonClassTable
import com.twt.service.schedule2.model.ScheduleDb
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

object AuditCourseManager {

    /**
     * 从数据库拉取蹭课信息
     */
    fun getAuditClasstableProvider(dao: AuditCourseDao = ScheduleDb.auditCourseDao): AbsClasstableProvider {
        val auditCourseList = dao.loadAllAuditCourses().asSequence()
                .map { it.convertToCourse() }
                .toList()
        val classtable = Classtable(courses = auditCourseList, termStart = termStart)
        val realClasstableProvider = CommonClassTable(classtable)
        return realClasstableProvider
    }

    /**
     * 获取蹭课数据的LiveData
     */
    fun getAuditListLive(dao: AuditCourseDao = ScheduleDb.auditCourseDao) = dao.loadAllAuditCoursesLiveData()

    /**
     * 网络拉取课程刷新信息 并且更新数据库
     */
    suspend fun refreshAuditClasstable(dao: AuditCourseDao = ScheduleDb.auditCourseDao) {
        val auditCourse = AuditApi.getMyAudit().awaitAndHandle { it.printStackTrace() }?.data
                ?: throw IllegalStateException("刷新蹭课列表失败")
        deleteAuditCoursesLocal(dao)
        val array = auditCourse.toTypedArray()
        dao.insertAuditCourses(*array)
    }

    private val suggestionLiveData = MutableLiveData<List<String>>()
    /**
     * 获取搜索建议的LiveData
     * LiveData获取一次即可 其余情况自动观察 不需要处理返回值
     * 返回字符串带 '-' 表示列表中的指示器
     */
    fun getSearchSuggestions(keyWord: String, dao: AuditCourseDao = ScheduleDb.auditCourseDao): LiveData<List<String>> {
        async(UI + QuietCoroutineExceptionHandler) {
            val suggestions = async(CommonPool) {
                val suggestions = mutableListOf<String>()
                val collegeList = dao.searchCollege(keyWord)
                if (collegeList.isNotEmpty()) {
                    suggestions.add("-学院")
                    suggestions.addAll(collegeList.map { it.collegeName })
                }
                val courseList = dao.searchCollegeCourses(keyWord)
                if (courseList.isNotEmpty()) {
                    suggestions.add("-课程")
                    suggestions.addAll(courseList.map { it.name })
                }
                suggestions
            }
            suggestionLiveData.value = suggestions.await()
        }

        return suggestionLiveData
    }

    fun refreshAuditSearchDatabase(dao: AuditCourseDao = ScheduleDb.auditCourseDao) {
        async(CommonPool + QuietCoroutineExceptionHandler) {
            val auditCollegeDataList = AuditApi.getAuditCollege().awaitAndHandle { it.printStackTrace() }?.data
                    ?: throw IllegalStateException("刷新课程表搜索提示失败")
            dao.insertColleges(auditCollegeDataList)
            auditCollegeDataList.forEach {
                dao.insertCollegeCourses(it.collegeCourses)
            }
        }
    }

    private fun deleteAuditCoursesLocal(dao: AuditCourseDao = ScheduleDb.auditCourseDao) {
        val courses = dao.loadAllAuditCourses().toTypedArray()
        dao.deleteAuditCourse(*courses)
    }
}