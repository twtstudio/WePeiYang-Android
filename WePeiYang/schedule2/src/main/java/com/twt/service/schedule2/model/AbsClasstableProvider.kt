package com.twt.service.schedule2.model

/**
 * Created by retrox on 2018/3/26.
 */
interface AbsClasstableProvider {

    /**
     * 根据时间戳获取当天课程
     * 列表里面返回的课程是可以直接显示在课表上面的 无论是彩色课程(weekAvail = false) 还是白色课程(weekAvail = true)
     * 检查next链表来处理冲突课程
     * 返回的列表需要进行Merge
     */
    fun getCourseByDay(unixTime: Long): List<Course>

    /**
     * 根据时间戳获取第二天课程
     */
    fun getCourseNextDay(unixTime: Long): List<Course>

    /**
     * 获取当天课程
     */
    fun getTodayCourse(): List<Course>

    /**
     * 获取明天课程
     */
    fun getTomorrowCourse(): List<Course>

    /**
     * 根据时间戳获取当时的课程
     * @return 可空Course
     */
    fun getCourseByTime(unixTime: Long): Course?

    /**
     * 根据时间戳返回下一门课程
     * @return 不可空Course
     */
    fun getNextCourseByTime(unixTime: Long): Course

    /**
     * 检查课程的冲突 传入课程检测课表里面有没有冲突课程
     * @return 返回可空课程 无冲突返回null
     */
    fun checkCourseConflict(course: Course): Course?

    /**
     * 传入周数获取当周的课程表
     */
    fun getCourseByWeek(week: Int): List<Course>

    /**
     * 传入时间戳获取当周的课程表
     */
    fun getCourseByWeekWithTime(unixTime: Long): List<Course>

    /**
     * 获取当前周数
     */
    fun getCurrentWeek(): Int

    fun getWeekByTime(unixTime: Long): Int

}