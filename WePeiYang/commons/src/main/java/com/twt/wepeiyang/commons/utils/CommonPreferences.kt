package com.twt.wepeiyang.commons.utils

import com.orhanobut.hawk.Hawk
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


fun <T> hawk(key: String, default: T) = object : ReadWriteProperty<Any?, T> {

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T = Hawk.get(key, default)

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        Hawk.put(key, value)
    }

}

/**
 * A persistent preferences' holder using Hawk.
 */
object CommonPreferences {

    var token by hawk("token", "")

    var gpaToken by hawk("gpa_token", "")

    var isLogin by hawk("is_login", false)

    var startUnix by hawk("start_unix", 946656000L)

    var studentNumber by hawk("student_number", "")

    var isBindTju by hawk("is_bind_tju", false)

    var isBindLibrary by hawk("is_bind_library", false)

    var isBindBike by hawk("is_bind_bike", false)

    var isFirstLogin by hawk("is_first_login", true)

    var isAcceptTos by hawk("is_accept_tos", false)

    var dropOut by hawk("drop_out", 0)

    var userId by hawk("user_id", "")

    var proxyAddress by hawk("proxy_address", "")

    var proxyPort by hawk("proxy_port", 0)

    fun clear() = Hawk.deleteAll()

}
