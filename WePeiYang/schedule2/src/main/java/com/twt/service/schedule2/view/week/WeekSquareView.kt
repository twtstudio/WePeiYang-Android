package com.twt.service.schedule2.view.week

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.twt.service.schedule2.R
import com.twt.service.schedule2.extensions.layer
import com.twt.service.schedule2.extensions.textCenter
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import java.util.*

class WeekSquareView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle) {

    val defaultBooleans: List<List<Boolean>>
        get() {
            val list = mutableListOf<MutableList<Boolean>>()
            for (i in 0 until 7) {
                val child = mutableListOf<Boolean>()
                for (x in 0 until 7) {
                    child.add(Random().nextBoolean())
                }
                list.add(child)
            }
            return list
        }
    var data: WeekSquareData = WeekSquareData(1, defaultBooleans)

    val backGroundPaint = Paint().apply {
        color = Color.parseColor("#E6F7F5")
    }
    val pointPaintTrue = Paint().apply {
        color = getColorCompat(R.color.colorPrimary)
    }
    val pointPaintFalse = Paint().apply {
        color = Color.parseColor("#D0D8D8")
    }

    val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 30f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }
    val textPaintBig = Paint().apply {
        color = getColorCompat(R.color.colorPrimary)
        textSize = 60f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas) {
        val padding = height / 5 // 核心点矩阵的左右下边距均为五分之一高度
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), backGroundPaint)

        layer {
            val text1 = "第"
            val text2 = "5"
            val text3 = "周"
            val measuredText1 = textPaint.measureText(text1)
            val measuredText2 = textPaintBig.measureText(text2)
            val measuredText3 = textPaint.measureText(text3)

            val yBase = (height / 2 - padding * 5 / 4).toFloat()
            val xBase = (width / 2).toFloat()

            canvas.drawText(text2, xBase, yBase, textPaintBig)
            canvas.drawText(text1, xBase - (measuredText1 + measuredText2) / 2, yBase, textPaint)
            canvas.drawText(text3, xBase + (measuredText3 + measuredText2) / 2, yBase, textPaint)

        }
        layer {
            val xBase = padding
            val yBase = height / 2 - padding * 3 / 4
            val pointMargin = (width - padding * 2) / 5
            for (x in 0 until 5) {
                val list = data.booleanPoints[x]
                for (y in 0 until 5) {
                    val pointX = xBase + (pointMargin / 2) + x * pointMargin
                    val pointY = yBase + (pointMargin / 2) + y * pointMargin
                    if (list[y]) {
                        canvas.drawCircle(pointX.toFloat(), pointY.toFloat(), 10f, pointPaintTrue)
                    } else {
                        canvas.drawCircle(pointX.toFloat(), pointY.toFloat(), 10f, pointPaintFalse)
                    }
                }
            }
        }

        layer {
            val text = "本周"
            canvas.textCenter(listOf(text), textPaint, (width / 2).toFloat(), (height - padding / 2).toFloat(), Paint.Align.CENTER)
        }

    }

    data class WeekSquareData(val weekInt: Int, val booleanPoints: List<List<Boolean>>)
}