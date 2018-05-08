package com.twt.service.schedule2.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import com.twt.wepeiyang.commons.experimental.CommonContext

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

/**
 * This method converts dp unit to equivalent pixels, depending on device density.
 *
 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
 * @param context Context to get resources and device specific display metrics
 * @return A float value to represent px equivalent to dp depending on device density
 */
fun dp2px(dp: Float, context: Context = CommonContext.application.applicationContext): Float {
    val resources = context.getResources()
    val metrics = resources.getDisplayMetrics()
    return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

/**
 * This method converts device specific pixels to density independent pixels.
 *
 * @param px A value in px (pixels) unit. Which we need to convert into db
 * @param context Context to get resources and device specific display metrics
 * @return A float value to represent dp equivalent to px value
 */
fun px2dp(px: Float, context: Context = CommonContext.application.applicationContext): Float {
    val resources = context.getResources()
    val metrics = resources.getDisplayMetrics()
    return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

inline fun <T : View> T.viewGrouplparams(
        width: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
        height: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
        init: ViewGroup.LayoutParams.() -> Unit
): T {
    val layoutParams = ViewGroup.MarginLayoutParams(width, height)
    layoutParams.init()
    this.layoutParams = layoutParams
    return this
}