package com.twt.service.schedule2.view.theme

import android.graphics.Color
import android.support.annotation.ColorInt

enum class ScheduleStyleType {
    LEX, PINK
}

// ColorPack For ScheduleDecoration
data class ScheduleDecoColorPack(@ColorInt val backGroundColor: Int, @ColorInt val textColor: Int)

//ColorPack For ScheduleCourses and nested with Deco
data class ScheduleColorPack(val name: String, val courseColors: List</*ColorInt*/ Int>, val scheduleDecoColorPack: ScheduleDecoColorPack, val style: ScheduleStyleType = ScheduleStyleType.LEX)

object ScheduleTheme {

    private val lexDecoColorPack = ScheduleDecoColorPack(rgbPercent(0.97f, 0.97f, 0.95f), rgbPercent(0.55f, 0.55f, 0.55f))
    private val lexThemePack = ScheduleColorPack("尊享", listOf(
            parseColor("#738d91"),
            parseColor("#c4ae65"),
            parseColor("#a7776b"),
            parseColor("#927a87"),
            parseColor("#646155"),
            parseColor("#dcc373"),
            parseColor("#748165"),
            parseColor("#ae837b"),
            parseColor("#738d91"),
            parseColor("#c4ae65"),
            parseColor("#a7776b")), lexDecoColorPack)

    private val lightLexThemeColorPack = ScheduleColorPack("尊享-Light", listOf(
            parseColor("#7a6270"),
            parseColor("#647179"),
            parseColor("#cc5968"),
            parseColor("#a8766b"),
            parseColor("#85976d"),
            parseColor("#e4ae58"),
            parseColor("#3f3534"),
            parseColor("#da7154"),
            parseColor("#97876d"),
            parseColor("#88443d"),
            parseColor("#e28045")), lexDecoColorPack)

    private val pinkDecoColorPack = ScheduleDecoColorPack(rgbPercent(0.95f, 0.97f, 0.97f), rgbPercent(0.46f, 0.55f, 0.62f))
    private val pinkThemeColorPack = ScheduleColorPack("粉色", listOf(
            parseColor("#fcc780"),
            parseColor("#acd87a"),
            parseColor("#fba890"),
            parseColor("#81c3f7"),
            parseColor("#eb99fb"),
            parseColor("#f5b4b2"),
            parseColor("#92d9a1"),
            parseColor("#bc88ea"),
            parseColor("#94d9c6"),
            parseColor("#7dd893"),
            parseColor("#8c9bee")), pinkDecoColorPack, ScheduleStyleType.PINK)


    private var currentTheme: ScheduleColorPack = pinkThemeColorPack

    fun getCurrentTheme(): ScheduleColorPack = currentTheme

    private fun parseColor(colorString: String) = Color.parseColor(colorString)

    fun setCurrentTheme(themePack: String) {
        when (themePack) {
            "Lex" -> currentTheme = lexThemePack
            "Lex-Light" -> currentTheme = lightLexThemeColorPack
            "Pink" -> currentTheme = pinkThemeColorPack
        }
//        SchedulePref.scheduleThemeName = themePack
    }

    private fun rgbPercent(red: Float, green: Float, blue: Float): Int {
        return -0x1000000 or
                ((red * 255.0f + 0.5f).toInt() shl 16) or
                ((green * 255.0f + 0.5f).toInt() shl 8) or
                (blue * 255.0f + 0.5f).toInt()
    }
}