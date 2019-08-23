package com.twt.service.schedule2.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.DisplayMetrics
import android.view.*
import android.widget.*
import com.twt.service.schedule2.R
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import kotlinx.android.synthetic.main.schedule_class_picker_pop.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.textColor
import org.jetbrains.anko.wrapContent


/**
 * 弹出弹窗的动画设置
 */
fun customPopUpWindow(context: Context, title: String, tip: String, num: Int, startTime: TextView, endTime: TextView){
    val popupWindow = PopupWindow(context)
    val view = LayoutInflater.from(context).inflate(R.layout.schedule_class_picker_pop, null, false).apply {
        layoutParams = FrameLayout.LayoutParams(matchParent, wrapContent).apply {
            gravity = Gravity.BOTTOM
            backgroundColor = Color.WHITE
        }
    }
    popupWindow.apply {
        isFocusable = true
        contentView = view
        width = WindowManager.LayoutParams.MATCH_PARENT
        animationStyle = R.style.style_pop_animation
        setBackgroundDrawable(null)
        showAtLocation(
                LayoutInflater.from(contentView.context).inflate(R.layout.schedule_frag_add_custom, null),
                Gravity.BOTTOM,
                0,
                0
        )
        contentView.apply {
            pop_title.textColor = getColorCompat(R.color.colorPrimary)
            indicator.textColor = getColorCompat(R.color.colorPrimary)
            class_tip.textColor = getColorCompat(R.color.colorPrimary)
            pop_title.text = title
            class_tip.text = tip
            divider_line.backgroundColor = getColorCompat(R.color.colorPrimary)
            custom_number_picker1.apply {
                minValue = 1
                maxValue = num
                value = this.value
                descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                setOnValueChangedListener { numberPicker, oldVal, newVal ->
                    startTime.text = newVal.toString()
                }
            }
            custom_number_picker2.apply {
                minValue = contentView.custom_number_picker1.value
                maxValue = num
                value = this.value
                descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                setOnValueChangedListener { numberPicker, oldVal, newVal ->
                    endTime.text = newVal.toString()
                }

            }

        }
    }
}

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