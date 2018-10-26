package com.twt.wepeiyang.commons.experimental.theme

import android.content.Context
import com.twt.wepeiyang.commons.R
import com.twt.wepeiyang.commons.experimental.CommonContext
import io.multimoon.colorful.CustomThemeColor

object CustomTheme {
    val context: Context get() = CommonContext.application.applicationContext
    val customPollia = CustomThemeColor(
            context,
            R.style.theme_custom_coal,
            R.style.theme_custom_coal,
            R.color.custom_coal,
            R.color.custom_coal
    )

    val customViking = CustomThemeColor(
            context,
            R.style.theme_custom_nordic,
            R.style.theme_custom_nordic,
            R.color.custom_nordic,
            R.color.custom_nordic
    )

    val customMatcha = CustomThemeColor(
            context,
            R.style.theme_custom_rose,
            R.style.theme_custom_rose,
            R.color.custom_rose,
            R.color.custom_rose
    )

    val customTerigaki = CustomThemeColor(
            context,
            R.style.theme_custom_matcha,
            R.style.theme_custom_matcha,
            R.color.custom_matcha,
            R.color.custom_matcha
    )

    val customSaddle = CustomThemeColor(
            context,
            R.style.theme_custom_gold,
            R.style.theme_custom_gold,
            R.color.custom_gold,
            R.color.custom_gold
    )

    val themeList = listOf(
            customPollia,
            customViking,
            customMatcha,
            customTerigaki,
            customSaddle
    )
}