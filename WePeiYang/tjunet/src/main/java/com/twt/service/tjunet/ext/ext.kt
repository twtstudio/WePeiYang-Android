package com.twt.service.tjunet.ext

/**
 * Created by retrox on 2018/3/19.
 */
internal inline val String.fineSSID: String
    get() {
        val ssid = this
        if (ssid.startsWith("\"") && ssid.endsWith("\"")) {

            return ssid.substring(1, ssid.length - 1);
        } else return ssid
    }

inline val Int.stringIP: String
    get() {
        val ip = this
        return (ip and 0xFF).toString() + "." +
                (ip shr 8 and 0xFF) + "." +
                (ip shr 16 and 0xFF) + "." +
                (ip shr 24 and 0xFF)
    }