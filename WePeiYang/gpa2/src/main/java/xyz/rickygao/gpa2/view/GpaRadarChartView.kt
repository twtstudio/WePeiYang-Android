package xyz.rickygao.gpa2.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import xyz.rickygao.gpa2.R

/**
 * Created by rickygao on 2017/11/13.
 */
// @JvmOverloads annotation fixes default value of kotlin's function parameter for java reflection
class GpaRadarChartView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle) {

    companion object {
        // fixed constant
        const val LINE_STROKE = 6F
        const val LABEL_TEXT_SIZE = 32F
        const val EMPTY_TEXT_SIZE = 64F

        // default constant for attrs
        const val DEFAULT_LINE_COLOR = Color.WHITE
        const val DEFAULT_FILL_COLOR = 0x99FFFFFF.toInt()
        const val DEFAULT_TEXT_COLOR = Color.WHITE
    }

    private val linePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = LINE_STROKE
    }

    private val radarPaint = Paint().apply {
        style = Paint.Style.FILL
    }

    private val textPaint = TextPaint().apply {
        textSize = LABEL_TEXT_SIZE
    }

    var lineColor
        get() = linePaint.color
        set(value) {
            linePaint.color = value
        }

    var fillColor
        get() = radarPaint.color
        set(value) {
            radarPaint.color = value
        }

    var textColor
        get() = textPaint.color
        set(value) {
            textPaint.color = value
        }

    var startRad = 0.0
        set(value) {
            field = value
            invalidate()
        }

    var emptyText: String? = null
        set(value) {
            field = value
            if (dataWithLabel.orEmpty().isNotEmpty())
                invalidate()
        }

    data class DataWithLabel(val data: Double, val label: String)

    var dataWithLabel: List<DataWithLabel>? = null
        set(value) {
            field = value
            invalidate()
        }

    private val linePath = Path()
    private val radarPath = Path()
    private val pointsRx = ArrayList<Float>()
    private val pointsRy = ArrayList<Float>()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val dataWithLabel = dataWithLabel.orEmpty()

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom
        val centerX = paddingLeft + contentWidth.toFloat() / 2F
        val centerY = paddingTop + contentHeight.toFloat() / 2F

        if (dataWithLabel.isEmpty()) {
            textPaint.textSize = EMPTY_TEXT_SIZE
            canvas.drawText(
                    emptyText,
                    centerX - textPaint.measureText(emptyText) / 2,
                    centerY,
                    textPaint
            )
            textPaint.textSize = LABEL_TEXT_SIZE
        }


        val minData = dataWithLabel.minBy(DataWithLabel::data)?.data?.toFloat() ?: return
        val maxData = dataWithLabel.maxBy(DataWithLabel::data)?.data?.toFloat() ?: return
        val dataSpan = maxData - minData
        val minDataExtended = 0F
//        val maxDataExtended = 100F
        val maxDataExtended = maxData + dataSpan / 4F
        val dataSpanExtended = maxDataExtended - minDataExtended

        val radarRadius = (if (contentWidth < contentHeight) contentWidth else contentHeight) / 2F * 0.75F
        val radStep = 2 * Math.PI / dataWithLabel.size
        pointsRx.clear()
        pointsRy.clear()
        (0 until dataWithLabel.size)
                .forEach {
                    pointsRx.add(-Math.sin(startRad + radStep * it).toFloat() * radarRadius)
                    pointsRy.add(-Math.cos(startRad + radStep * it).toFloat() * radarRadius)
                }

        linePath.apply {
            reset()

            (0 until dataWithLabel.size)
                    .forEach {
                        moveTo(centerX, centerY)
                        lineTo(centerX + pointsRx[it], centerY + pointsRy[it])
                    }

            (0 until dataWithLabel.size)
                    .forEach {
                        if (it == 0)
                            moveTo(centerX + pointsRx[it], centerY + pointsRy[it])
                        else
                            lineTo(centerX + pointsRx[it], centerY + pointsRy[it])
                    }
            close()
        }

        radarPath.apply {
            reset()

            moveTo(centerX, centerY)
            dataWithLabel.withIndex().forEach { (i, e) ->
                val ratio = (e.data.toFloat() - minDataExtended) / dataSpanExtended
                if (i == 0)
                    moveTo(centerX + pointsRx[i] * ratio,
                            centerY + pointsRy[i] * ratio)
                else
                    lineTo(centerX + pointsRx[i] * ratio,
                            centerY + pointsRy[i] * ratio)
            }

            close()
        }

        canvas.apply {
            drawPath(linePath, linePaint)
            drawPath(radarPath, radarPaint)

            dataWithLabel.withIndex()
                    .forEach { (i, e) ->
                        val textWidth = textPaint.measureText(e.label)

//                        val x =
//                                // center
//                                if(i == 0 || i * 2 == dataWithLabel.size)
//                                    centerX + pointsRx[i] * 1.125F - textWidth / 2
//                                // left
//                                else if(i * 2 < dataWithLabel.size)
//                                    centerX + pointsRx[i] * 1.125F - textWidth
//                                // right
//                                else
//                                    centerX + pointsRx[i] * 1.125F

                        val x =
                                // center
                                if (Math.abs(pointsRx[i]) < radarRadius * 0.5)
                                    centerX + pointsRx[i] * 1.125F - textWidth * (0.5F - pointsRx[i] / radarRadius * 1F)
                                // left
                                else if (pointsRx[i] < 0)
                                    centerX + pointsRx[i] * 1.125F - textWidth
                                // right
                                else
                                    centerX + pointsRx[i] * 1.125F

//                        val y =
//                                // center
//                                if (dataWithLabel.size == i * 4 && i * 4 == dataWithLabel.size * 3)
//                                    centerY + pointsRy[i] * 1.125F
//                                // above
//                                else if(i * 4 < dataWithLabel.size || i * 4 > dataWithLabel.size * 3)
//                                    centerY + pointsRy[i] * 1.125F - textPaint.fontMetrics.ascent // all Font.Metrics relative to baseline
//                                // below
//                                else
//                                    centerY + pointsRy[i] * 1.125F

                        val y =
                                // center
                                if (Math.abs(pointsRy[i]) < radarRadius * 0.5)
                                    centerY + pointsRy[i] * 1.125F - textPaint.fontMetrics.ascent * (0.5F + pointsRy[i] / radarRadius * 1F)
                                // above
                                else if (pointsRy[i] < 0)
                                    centerY + pointsRy[i] * 1.125F
                                // below
                                else
                                    centerY + pointsRy[i] * 1.125F - textPaint.fontMetrics.ascent // Font.Metrics are all relative to baseline

                        drawText(e.label, x, y, textPaint)
                    }
        }
    }


    init {
        context.obtainStyledAttributes(attrs, R.styleable.GpaRadarChartView, defStyle, 0).apply {

            lineColor = getColor(R.styleable.GpaRadarChartView_lineColor, DEFAULT_LINE_COLOR)

            fillColor = getColor(R.styleable.GpaRadarChartView_fillColor, DEFAULT_FILL_COLOR)

            textColor = getColor(R.styleable.GpaRadarChartView_textColor, DEFAULT_TEXT_COLOR)

            recycle()
        }
    }

}