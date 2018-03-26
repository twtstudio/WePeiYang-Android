package com.twt.wepeiyang.commons.experimental.color

import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import com.twt.wepeiyang.commons.experimental.CommonContext

/**
 * Created by retrox on 2018/3/26.
 */

fun getColorCompat(resId: Int): Int = ContextCompat.getColor(CommonContext.application.applicationContext, resId)
