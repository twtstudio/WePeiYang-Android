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
import kotlinx.coroutines.*

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
        var usableCourse :ArrayList<AuditCourse> = ArrayList()
        auditCourse.forEach {
            if(it.infos.isNotEmpty())
                usableCourse.add(it)
        }
        val array = usableCourse.toTypedArray()
        dao.insertAuditCourses(*array)
    }

    private val suggestionLiveData = MutableLiveData<List<String>>()
    /**
     * 获取搜索建议的LiveData
     * LiveData获取一次即可 其余情况自动观察 不需要处理返回值
     * 返回字符串带 '-' 表示列表中的指示器
     */
    fun getSearchSuggestions(keyWord: String, dao: AuditCourseDao = ScheduleDb.auditCourseDao): LiveData<List<String>> {
        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
            val suggestions = async(Dispatchers.Default) {
                val suggestions = mutableListOf<String>()
                if (keyWord.isBlank()) return@async suggestions
                /**
                 * 定义 # 开头的搜索是查询学院对应的课程
                 */
                if (!keyWord.startsWith("#")) {
                    val collegeList = dao.searchCollege("%${keyWord}%")  // 通配符来模糊搜索
                    if (collegeList.isNotEmpty()) {
                        suggestions.add("-学院")
                        suggestions.addAll(collegeList.map { it.collegeName + "college" })
                    }

                    /**
                     * 这是一个搜索优化 当输入空白的时候 只会出现全部的学院 而不是出现课程
                     */
                    if (keyWord.isNotBlank()) {
                        val courseList = dao.searchCollegeCourses("%${keyWord}%")
                        if (courseList.isNotEmpty()) {
                            suggestions.add("-课程")
                            suggestions.addAll(courseList.map { it.name })
                        }
                    }
                } else {
                    val realKeyWord = keyWord.removePrefix("#")
                    val courseList = if (realKeyWord.isNotBlank()) dao.searchCoursesByCollege("%$realKeyWord%") else listOf()
                    if (courseList.isNotEmpty()) {
                        suggestions.add("-课程")
                        suggestions.addAll(courseList.map { it.name })
                    } else {
                        suggestions.add("-提示: 请输入正确的学院名，或使用下方建议")
                        val collegeList = dao.loadAllColleges()  // 通配符来模糊搜索
                        if (collegeList.isNotEmpty()) {
                            suggestions.addAll(collegeList.map { it.collegeName + "college" })
                        }
                    }
                }

                suggestions
            }
            suggestionLiveData.value = suggestions.await()
        }

        return suggestionLiveData
    }

    fun refreshAuditSearchDatabase(dao: AuditCourseDao = ScheduleDb.auditCourseDao) {
        GlobalScope.async(Dispatchers.Default + QuietCoroutineExceptionHandler) {
            val auditCollegeDataList = AuditApi.getAuditCollege().awaitAndHandle { it.printStackTrace() }?.data
                    ?: throw IllegalStateException("刷新课程表搜索提示失败")
            // 先全部清除课程表的缓存
            dao.deleteAllCollegesCache()
            dao.deleteAllCollegesCoursesCache()
            dao.insertColleges(auditCollegeDataList)
            auditCollegeDataList.forEach { data: AuditCollegeData ->
                dao.insertCollegeCourses(data.collegeCourses.onEach {
                    it.collegeName = data.collegeName
                })
            }
        }
    }

    private fun deleteAuditCoursesLocal(dao: AuditCourseDao = ScheduleDb.auditCourseDao) {
        val courses = dao.loadAllAuditCourses().toTypedArray()
        dao.deleteAuditCourse(*courses)
    }
}