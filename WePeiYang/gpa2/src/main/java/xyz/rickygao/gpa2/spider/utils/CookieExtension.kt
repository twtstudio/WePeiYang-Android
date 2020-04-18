package xyz.rickygao.gpa2.spider.utils

import okhttp3.Cookie

fun Cookie.isExpired(): Boolean = this.expiresAt() < System.currentTimeMillis()