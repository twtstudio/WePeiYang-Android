package com.twt.wepeiyang.commons.experimental.theme

import android.content.Context
import com.twt.wepeiyang.commons.R
import com.twt.wepeiyang.commons.experimental.CommonContext
import io.multimoon.colorful.CustomThemeColor

object CustomTheme {
    val context: Context get() = CommonContext.application.applicationContext
    val customPeppaPink = CustomThemeColor(
            context,
            R.style.theme_custom_peppa_pink,
            R.style.theme_custom_peppa_pink,
            R.color.custom_peppa_pink,
            R.color.custom_peppa_pink
    )

    val customGrorgeBlue = CustomThemeColor(
            context,
            R.style.theme_custom_george_blue,
            R.style.theme_custom_george_blue,
            R.color.custom_george_blue,
            R.color.custom_george_blue
    )

    val customPigMomYellow = CustomThemeColor(
            context,
            R.style.theme_pig_mom_yellow,
            R.style.theme_pig_mom_yellow,
            R.color.custom_pig_mom_yellow,
            R.color.custom_pig_mom_yellow
    )

    val customPigDadGreen = CustomThemeColor(
            context,
            R.style.theme_pig_dad_green,
            R.style.theme_pig_dad_green,
            R.color.custom_pig_dad_green,
            R.color.custom_pig_dad_green
    )

    val customGayPurple = CustomThemeColor(
            context,
            R.style.theme_gay_purple,
            R.style.theme_gay_purple,
            R.color.custom_gay_purple,
            R.color.custom_gay_purple
    )

    val themeList = listOf(
            customPeppaPink,
            customGrorgeBlue,
            customPigMomYellow,
            customPigDadGreen,
            customGayPurple
    )
}