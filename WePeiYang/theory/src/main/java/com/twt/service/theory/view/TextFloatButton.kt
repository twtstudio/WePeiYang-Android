package com.twt.service.theory.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import org.jetbrains.anko.sp
import org.jetbrains.anko.windowManager

class TextFloatButton(context: Context, attrs: AttributeSet) : FloatingActionButton(context, attrs) {
    var time: String = ""
    private var lock: Boolean = true
    private var thread: Thread? = null
    private var lastX: Float = 0.0f
    private var lastY: Float = 0.0f
    var actionH: Int = 0
    var ansH: Int = 0

    init {
        val defaultDisplay = context.windowManager.defaultDisplay
        val point = Point()
        defaultDisplay.getSize(point)
        val screenWidth = point.x
        val screenHeight = point.y
        setOnTouchListener { _, event ->
            when {
                event.action == MotionEvent.ACTION_DOWN -> {
                    lastX = event.x
                    lastY = event.y
                }
                event.action == MotionEvent.ACTION_MOVE -> {
                    val offsetX = event.x - lastX
                    val offsetY = event.y - lastY
                    Log.d("BUG", (right - left).toString())
                    Log.d("BUG", (bottom - top).toString())
                    offsetTopAndBottom(offsetY.toInt())
                    offsetLeftAndRight(offsetX.toInt())
//                    layout((left + offsetX).toInt(), (top + offsetY).toInt(), (right + offsetX).toInt(), (bottom + offsetY).toInt())
                }
                event.action == MotionEvent.ACTION_UP -> {
                    val objectAnimator: ObjectAnimator = if (x > screenWidth / 2.0) {
                        ObjectAnimator.ofFloat(this, "x", x, (screenWidth - width - 10).toFloat())
                    } else {
                        ObjectAnimator.ofFloat(this, "x", x, 10.0f)
                    }
                    objectAnimator.start()
                    when {
                        y < actionH + height / 2.0 -> ObjectAnimator.ofFloat(this, "y", y, (actionH + height / 2.0).toFloat())
                        y > screenHeight - ansH - height / 2.0 -> ObjectAnimator.ofFloat(this, "y", y, (screenHeight - ansH - height).toFloat())
                        else -> null
                    }?.start()
                }
            }
            true
        }
    }

    private fun dp2px(value: Float, context: Context): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.resources.displayMetrics);
    }

    fun sp2px(spValue: Float): Float {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return spValue * fontScale + 0.5f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = sp2px(15.0f)
        val bounds = Rect()
        paint.color = Color.WHITE
        paint.textSize = sp(15).toFloat()
        paint.getTextBounds(time, 0, time.length, bounds)
        val textWidth = bounds.width()
        val fontMetrics = paint.fontMetricsInt
        val fontHeight = fontMetrics.bottom - fontMetrics.top
        val textBaseY = height - (height - fontHeight) / 2 - fontMetrics.bottom
        canvas?.drawText(time, ((width - textWidth) / 2).toFloat(), textBaseY.toFloat(), paint)

    }

    fun end() {
        lock = false
    }

    fun start(beginMinute: Int, beginSecond: Int, callBack: () -> (Unit)) {
        time = "$beginMinute:$beginSecond"
        lock = true
        invalidate()
        thread = object : Thread() {
            override fun run() {
                super.run()
                while (true) {
                    if (!lock) break
                    sleep(1000)
                    var minute = time.substring(0, 2).toInt()
                    var second = time.substring(3, 5).toInt()
                    if (second != 0) --second
                    else if (minute != 0) {
                        --minute
                        second = 59
                    } else {
                        callBack()
                        break
                    }
                    time = if (minute < 10) " $minute" else "$minute"
                    time = "$time:"
                    time += if (second < 10) "0$second" else "$second"
                    invalidate()
                }
            }
        }
        thread?.start()
    }
}