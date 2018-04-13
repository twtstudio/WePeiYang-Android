package com.twt.service.schedule2.extensions

import android.app.Activity
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics

/**
 * Canvas绘制时候的图层隔离
 */
fun layer(block: () -> Unit) = block()

/**
 * 自动Restore Canvas
 */
fun Canvas.autoRestore(block: (Canvas) -> Unit) {
    this.save()
    block(this)
    this.restore()
}

/**
 * 多个字符串的居中绘制
 * @see https://www.jianshu.com/p/2eb8ae713c1f
 */
fun Canvas.textCenter(strings: List<String>, paint: Paint, x: Float, y: Float, align: Paint.Align) {
    paint.textAlign = align
    val fontMetrics = paint.fontMetrics
    val top = fontMetrics.top
    val bottom = fontMetrics.bottom
    val length = strings.size
    val total = (length - 1) * (-top + bottom) + (-fontMetrics.ascent + fontMetrics.descent)
    val offset = total / 2 - bottom
    for (i in 0 until length) {
        val yAxis = -(length - i - 1) * (-top + bottom) + offset
        this.drawText(strings[i], x, y + yAxis, paint)
    }
}

fun Activity.getScreenWidth(): Int {
    val dm = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(dm)
    //宽度 dm.widthPixels
    //高度 dm.heightPixels
    return dm.widthPixels
}

fun Activity.getScreenHeight(): Int {
    val dm = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(dm)
    //宽度 dm.widthPixels
    //高度 dm.heightPixels
    return dm.heightPixels
}