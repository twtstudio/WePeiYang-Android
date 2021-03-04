package com.twt.service.schedule2.model

import com.twt.wepeiyang.commons.experimental.preference.hawk

object SchedulePref {
    var termStart by hawk("schedule2_term_start",1520179200L)
    var autoCollapseSchedule by hawk("schedule2_auto_collapse", true)
    var scheduleThemeName by hawk("schedule2_theme_name", "Lex")
    var auditCourseVisibility by hawk("schedule2_audit_visibility", false)

}