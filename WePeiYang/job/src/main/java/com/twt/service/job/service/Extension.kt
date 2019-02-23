package com.twt.service.job.service

import android.text.Html

fun Recruit?.convertNotice(): Notice? {
    if (this == null) return null
    return Notice(attach1, attach1_name, attach2, attach2_name, attach3, attach3_name, click, content, date, id, title, type)
}

fun Notice.convertNoticeAfter(): NoticeAfter {
    val mMap: MutableMap<String, String> = mutableMapOf()
    var hasAttach = false // 便于判断分割线

    if (attach1_name != null && attach1 != null) {
        mMap[attach1_name] = attach1
        hasAttach = true
    }
    if (attach2_name != null && attach2 != null) {
        mMap[attach2_name] = attach2
        hasAttach = true
    }
    if (attach3_name != null && attach3 != null) {
        mMap[attach3_name] = attach3
        hasAttach = true
    }

    return NoticeAfter(mMap, click, Html.fromHtml(content).toString(), date, title, hasAttach)
}