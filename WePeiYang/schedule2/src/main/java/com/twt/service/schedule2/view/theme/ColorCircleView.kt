package com.twt.service.schedule2.view.theme

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewManager
import com.twt.service.schedule2.R
import com.twt.service.schedule2.extensions.dp2px
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import org.jetbrains.anko.custom.ankoView

/**
 * 主题切换用的圆形表示器
 * 建议宽高 24x24dp 相同宽高
 */
class ColorCircleView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle) {
    var color: Int = getColorCompat(R.color.colorPrimary)
        set(value) {
            field = value
            invalidate()
        }

    private val fillPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val strokePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = dp2px(1f)
        color = Color.parseColor("#979797")
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        val centerX = measuredWidth.toFloat() / 2
        val centerY = measuredHeight.toFloat() / 2
        val radius = measuredWidth.toFloat() / 2 - 5f

        fillPaint.color = color

        canvas.drawCircle(centerX, centerY, radius, fillPaint)
        canvas.drawCircle(centerX, centerY, radius, strokePaint)

    }
}

inline fun ViewManager.colorCircleView() = colorCircleView {}

inline fun ViewManager.colorCircleView(init: ColorCircleView.() -> Unit): ColorCircleView {
    return ankoView({ ColorCircleView(it) }, theme = 0, init = init)
}