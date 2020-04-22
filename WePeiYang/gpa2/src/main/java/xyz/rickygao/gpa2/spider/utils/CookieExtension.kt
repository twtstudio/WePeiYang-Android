package xyz.rickygao.gpa2.spider.utils

import okhttp3.Cookie

/**
 * 判断cookie是否失效
 * @param timeZone 因为okhttp3.Cookie使用0时区解析过期日期，得到的时间戳与当前时区不符。
 * 东为正 1，2，3,...
 * 西为负 -1,-2,-3,...
 */
fun Cookie.isExpired(timeZone:Int = 8): Boolean = (this.expiresAt() - timeZone*60*60*1000) < System.currentTimeMillis()