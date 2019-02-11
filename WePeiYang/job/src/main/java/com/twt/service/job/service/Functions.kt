package com.twt.service.job.service

import android.text.Html

object Functions {
    fun convertNotice(recruit: Recruit?): Notice? {
        if (recruit == null) return null
        val notice = Notice(recruit.attach1, recruit.attach1_name, recruit.attach2, recruit.attach2_name, recruit.attach3, recruit.attach3_name, recruit.click, recruit.content, recruit.date, recruit.id, recruit.title, recruit.type)
        return notice
    }

    fun convertNoticeAfter(notice: Notice): NoticeAfter {
        val mMap: MutableMap<String, String> = mutableMapOf()
        var hasAttach: Boolean = false // 便于判断分割线
        notice.apply {
            if (attach1_name != null && attach1!=null) {
                mMap[attach1_name] = attach1
                hasAttach = true
            }
            if (attach2_name != null && attach2!=null) {
                mMap[attach2_name] = attach2
                hasAttach = true
            }
            if (attach3_name != null && attach3!=null) {
                mMap[attach3_name] = attach3
                hasAttach = true
            }
        }
        return NoticeAfter(mMap, notice.click, Html.fromHtml(notice.content).toString(), notice.date, notice.title, hasAttach)
    }
}