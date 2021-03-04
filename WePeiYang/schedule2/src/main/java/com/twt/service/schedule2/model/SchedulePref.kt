package com.twt.service.schedule2.model

import com.twt.service.schedule2.spider.ScheduleSpider
import com.twt.wepeiyang.commons.experimental.preference.hawk

object SchedulePref {
    var termStart by hawk("schedule2_term_start", ScheduleSpider.termStart)
    var autoCollapseSchedule by hawk("schedule2_auto_collapse", true)
    var scheduleThemeName by hawk("schedule2_theme_name", "Lex")
    var auditCourseVisibility by hawk("schedule2_audit_visibility", false)
    var ifMinor by hawk("schedule2_minor", false)

}