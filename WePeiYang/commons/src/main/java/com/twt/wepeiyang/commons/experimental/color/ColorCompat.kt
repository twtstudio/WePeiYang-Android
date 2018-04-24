package com.twt.wepeiyang.commons.experimental.color

import android.support.v4.content.ContextCompat
import com.twt.wepeiyang.commons.R
import com.twt.wepeiyang.commons.experimental.CommonContext
import io.multimoon.colorful.Colorful

/**
 * Created by retrox on 2018/3/26.
 */

fun getColorCompat(resId: Int): Int {
    return when (resId) {
        R.color.colorPrimary -> return Colorful().getPrimaryColor().getColorPack().normal().asInt()
        R.color.colorPrimaryDark -> return Colorful().getPrimaryColor().getColorPack().dark().asInt()
        R.color.colorAccent -> return Colorful().getAccentColor().getColorPack().normal().asInt()
        else -> ContextCompat.getColor(CommonContext.application.applicationContext, resId)
    }
}