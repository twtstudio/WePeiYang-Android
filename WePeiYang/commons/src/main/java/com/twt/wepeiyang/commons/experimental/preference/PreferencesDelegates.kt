package com.twt.wepeiyang.commons.experimental.preference

import com.orhanobut.hawk.Hawk
import com.twt.wepeiyang.commons.experimental.CommonContext
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> hawk(key: String, default: T) = object : ReadWriteProperty<Any?, T> {

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T = Hawk.get(key, default)

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        Hawk.put(key, value)
    }

}

fun shared(key: String, default: String) = object : ReadWriteProperty<Any?, String> {

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
            CommonContext.defaultSharedPreferences.getString(key, default)

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        CommonContext.defaultSharedPreferences.edit().putString(key, value).apply()
    }

}

fun shared(key: String, default: Set<String>) = object : ReadWriteProperty<Any?, Set<String>> {

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): Set<String> =
            CommonContext.defaultSharedPreferences.getStringSet(key, default)

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Set<String>) {
        CommonContext.defaultSharedPreferences.edit().putStringSet(key, value).apply()
    }

}

fun shared(key: String, default: Int) = object : ReadWriteProperty<Any?, Int> {

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): Int =
            CommonContext.defaultSharedPreferences.getInt(key, default)

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        CommonContext.defaultSharedPreferences.edit().putInt(key, value).apply()
    }

}

fun shared(key: String, default: Long) = object : ReadWriteProperty<Any?, Long> {

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): Long =
            CommonContext.defaultSharedPreferences.getLong(key, default)

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) {
        CommonContext.defaultSharedPreferences.edit().putLong(key, value).apply()
    }

}

fun shared(key: String, default: Float) = object : ReadWriteProperty<Any?, Float> {

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): Float =
            CommonContext.defaultSharedPreferences.getFloat(key, default)

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Float) {
        CommonContext.defaultSharedPreferences.edit().putFloat(key, value).apply()
    }

}

fun shared(key: String, default: Boolean) = object : ReadWriteProperty<Any?, Boolean> {

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): Boolean =
            CommonContext.defaultSharedPreferences.getBoolean(key, default)

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
        CommonContext.defaultSharedPreferences.edit().putBoolean(key, value).apply()
    }

}