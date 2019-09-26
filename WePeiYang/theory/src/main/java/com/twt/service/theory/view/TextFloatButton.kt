package com.twt.service.theory.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
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
            val w = width
            val h = height
            when {
                event.action == MotionEvent.ACTION_DOWN -> {
                    lastX = event.x
                    lastY = event.y
                }
                event.action == MotionEvent.ACTION_MOVE -> {
                    val offsetX = event.x - lastX
                    val offsetY = event.y - lastY
                    layout((left + offsetX).toInt(), (top + offsetY).toInt(), (right + offsetX).toInt(), (bottom + offsetY).toInt())
                }
                event.action == MotionEvent.ACTION_UP -> {
                    val objectAnimator: ObjectAnimator = if (x > screenWidth / 2.0) {
                        ObjectAnimator.ofFloat(this, "x", x, (screenWidth - w - 10).toFloat())
                    } else {
                        ObjectAnimator.ofFloat(this, "x", x, 10.0f)
                    }
                    objectAnimator.start()
                }
            }
            if (x > screenWidth - w - 10) layout(screenWidth - 10 - w, top, screenWidth - 10, bottom)
            if (x < 10) layout(10, top, 10 + w, bottom)
            if (y > screenHeight - h - 10 - ansH) layout(left, screenHeight - 10 - ansH - h, right, screenHeight - 10 - ansH)
            if (y < actionH) layout(left, actionH, right, actionH + h)
            true
        }
    }

    private fun dp2px(value: Float, context: Context): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.resources.displayMetrics);
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.BLACK
        paint.textSize = 40.0f
        canvas?.drawText(time, dp2px(9f, context), dp2px(30f, context), paint)
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