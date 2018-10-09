package com.twt.service.schedule2.view.week

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.twt.service.schedule2.R
import com.twt.service.schedule2.extensions.dp2px
import com.twt.service.schedule2.extensions.layer
import com.twt.service.schedule2.extensions.textCenter
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import java.util.*

class WeekSquareView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle) {

    private val defaultBooleans: List<List<Boolean>>
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

    private val selectedBackGroundPaint = Paint().apply {
        color = getColorCompat(R.color.white)
    }

    private val backGroundPaint = Paint().apply {
        color = getColorCompat(R.color.colorPrimary)
    }
    private val pointPaintTrue = Paint().apply {
        color = getColorCompat(R.color.colorPrimary)
        isAntiAlias = true
    }
    private val pointPaintFalse = Paint().apply {
        color = Color.parseColor("#D8D8D0")
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = getColorCompat(R.color.colorPrimary)
        textSize = dp2px(10f)
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }
    private val textPaintBig = Paint().apply {
        color = getColorCompat(R.color.colorPrimary)
        textSize = dp2px(20f)
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    /**
     * 配置数据
     */
    var data: WeekSquareData = WeekSquareData(1, defaultBooleans)
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        val padding = height / 5 // 核心点矩阵的左右下边距均为五分之一高度
        if (data.currentWeekText.contains("选中")) {
            canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), selectedBackGroundPaint)
        } else {
            canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), backGroundPaint)
        }

        // 文字绘制见Hencoder第三期 -> 练习题中对于文字的measure
        layer {
            val text1 = "第"
            val text2 = data.weekInt.toString()
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

        // padding表示点阵的左右下边距 上面的边距略大一些 差不多是 Height * 1/2 - padding
        // 差不多就是这个意思 反正就是上面下面画文字 中间画点阵（一个两层循环里面drawCircle即可）
        layer {
            val xBase = padding // xBase是点阵左上角的参考点 结合yBase margin（点阵圆心间的距离）来画
            val yBase = height / 2 - padding * 3 / 4
            val pointMargin = (width - padding * 2) / 5
            for (x in 0 until 5) {
                val list = data.booleanPoints[x]
                for (y in 0 until 5) {
                    val pointX = xBase + (pointMargin / 2) + x * pointMargin
                    val pointY = yBase + (pointMargin / 2) + y * pointMargin
                    if (list[y]) {
                        canvas.drawCircle(pointX.toFloat(), pointY.toFloat(), dp2px(3.3f), pointPaintTrue)
                    } else {
                        canvas.drawCircle(pointX.toFloat(), pointY.toFloat(), dp2px(3.3f), pointPaintFalse)
                    }
                }
            }
        }

        layer {
            if (data.currentWeekText != "") {
                val text = data.currentWeekText
                canvas.textCenter(listOf(text), textPaint, (width / 2).toFloat(), (height - padding / 2).toFloat(), Paint.Align.CENTER)
            }
        }

    }

    data class WeekSquareData(val weekInt: Int = 1, val booleanPoints: List<List<Boolean>>, val currentWeekText: String = "") {
        companion object {
            fun generateDefaultMatrix(): List<List<Boolean>> {
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
        }
    }
}