package com.twt.service.schedule2.model.audit

data class AuditCourse(val college: String = "",
                       val courseId: Int = 0,
                       val courseName: String = "",
                       val year: String = "",
                       val semester: String = "",
                       val infos: List<InfosItem>?)

data class InfosItem(val courseId: String = "",
                     val endWeek: String = "",
                     val courseName: String = "",
                     val weekType: String = "",
                     val courseIdInTju: String = "",
                     val startWeek: String = "",
                     val building: String = "",
                     val room: String = "",
                     val startTime: String = "",
                     val teacher: String = "",
                     val courseLength: String = "",
                     val weekDay: String = "",
                     val teacherType: String = "",
                     val id: Int = 0)


