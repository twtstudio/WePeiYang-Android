package com.twt.wepeiyang.commons.mta

import android.content.Context
import com.tencent.stat.StatService
import java.util.*

/**
 * Created by tjwhm@TWTStudio at 11:18 PM, 2018/12/9.
 * Happy coding!
 * 此文件为对腾讯 MTA 事件埋点进行的封装
 * @see
 */

/**
 * 用于曝光事件埋点
 * @param name, 请传入 "模块名-xxx" 的 String, 例如 "exam-刷题首页" / "gpa2-课程评价" / "app-首页 GPA Item"
 */
fun mtaExpose(context: Context, name: String) {
    val prop = Properties()
    prop.setProperty("name", name)
    StatService.trackCustomKVEvent(context, "expose", prop)
}

/**
 * 用于点击事件埋点
 * @param name, 请传入 "模块名-xxx" 的 String, 例如 "exam-提醒选择页模拟考试" / "gpa2-GPA 首页右上角刷新" / "app-首页 GPA Item"
 */
fun mtaClick(context: Context, name: String) {
    val prop = Properties()
    prop.setProperty("name", name)
    StatService.trackCustomKVEvent(context, "click", prop)
}
