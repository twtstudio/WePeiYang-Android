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
 * @param name, 请传入 "模块名_xxx" 的 String, 例如 "exam_刷题首页" / "gpa2_课程评价" / "app_首页 GPA Item"
 */
fun mtaExpose(context: Context, name: String) {
    val prop = Properties()
    prop.setProperty("name", name)
    StatService.trackCustomKVEvent(context, "expose", prop)
}

/**
 * 用于点击事件埋点
 * @param name, 请传入 "模块名_xxx" 的 String, 例如 "exam_提醒选择页模拟考试" / "gpa2_GPA 首页右上角刷新" / "app_首页 GPA Item"
 */
fun mtaClick(context: Context, name: String) {
    val prop = Properties()
    prop.setProperty("name", name)
    StatService.trackCustomKVEvent(context, "click", prop)
}

// Begin 和 End 相对应, 用于统计事件的时长
// 同一事件需要传入相同的 name, name 命名规范和上面两个方法相同
fun mtaBegin(context: Context, name: String) {
    val prop = Properties()
    prop.setProperty("name", name)
    StatService.trackCustomBeginKVEvent(context, "time", prop)
}

fun mtaEnd(context: Context, name: String) {
    val prop = Properties()
    prop.setProperty("name", name)
    StatService.trackCustomEndKVEvent(context, "time", prop)
}
