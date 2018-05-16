package com.twt.wepeiyang.commons.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewManager
import com.twt.wepeiyang.commons.R
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.dip

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

    var stroke: Boolean = true
        set(value) {
            field = value
            invalidate()
        }

    var circlePadding: Float = 5f
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
        strokeWidth = dip(1).toFloat()
        color = Color.parseColor("#979797")
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        val centerX = measuredWidth.toFloat() / 2
        val centerY = measuredHeight.toFloat() / 2
        val radius = measuredWidth.toFloat() / 2 - circlePadding

        fillPaint.color = color

        canvas.drawCircle(centerX, centerY, radius, fillPaint)
        if (stroke) {
            canvas.drawCircle(centerX, centerY, radius, strokePaint)
        }

    }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.ColorCircleView, defStyle, 0).apply {

            stroke = getBoolean(R.styleable.ColorCircleView_drawCircleBounds, true)
            circlePadding = getFloat(R.styleable.ColorCircleView_radiusPadding, 5f)
            color = getColor(R.styleable.ColorCircleView_circleColor, getColorCompat(R.color.colorPrimary))
            recycle()
        }
    }

}

inline fun ViewManager.colorCircleView() = colorCircleView {}

inline fun ViewManager.colorCircleView(init: ColorCircleView.() -> Unit): ColorCircleView {
    return ankoView({ ColorCircleView(it) }, theme = 0, init = init)
}