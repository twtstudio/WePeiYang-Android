package xyz.rickygao.gpa2.view

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import xyz.rickygao.gpa2.R
import xyz.rickygao.gpa2.ext.*

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
            if (dataWithLabel.isEmpty())
                invalidate()
        }

    data class DataWithLabel(val data: Double, val label: String)

    var dataWithLabel: List<DataWithLabel> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    private val linePath = Path()
    private val radarPath = Path()
    private val rpoints = mutableListOf<PointF>()


    private fun computePath() {

        val contentWidth = (width - paddingLeft - paddingRight).toFloat()
        val contentHeight = (height - paddingTop - paddingBottom).toFloat()
        val centerX = paddingLeft + contentWidth / 2F
        val centerY = paddingTop + contentHeight / 2F
        val radarRadius = (if (contentWidth < contentHeight) contentWidth else contentHeight) / 2F * 0.75F
        val radStep = 2 * Math.PI / dataWithLabel.size

        val minData = dataWithLabel.minBy(DataWithLabel::data)?.data?.toFloat() ?: return
        val maxData = dataWithLabel.maxBy(DataWithLabel::data)?.data?.toFloat() ?: return
        val dataSpan = maxData - minData
        val minDataExtended = 0F
//        val maxDataExtended = 100F
        val maxDataExtended = maxData + dataSpan / 4F
        val dataSpanExtended = maxDataExtended - minDataExtended

        dataWithLabel.asSequence()
                .mapIndexed { index, _ -> startRad + radStep * index }
                .mapTo(rpoints.apply { clear() }) {
                    PointF(-Math.sin(it).toFloat() * radarRadius,
                            -Math.cos(it).toFloat() * radarRadius)
                }

        linePath.reuse {

            rpoints.forEach { (x, y) ->
                moveTo(centerX, centerY)
                rLineTo(x, y)
            }

            rpoints.forEach { (x, y) -> lineTo(centerX + x, centerY + y) }

        }

        radarPath.reuse {

            dataWithLabel.asSequence()
                    .map { (it.data - minDataExtended) / dataSpanExtended }
                    .zip(rpoints.asSequence())
                    .map { (ratio, rpoint) ->
                        PointF(centerX + rpoint.x * ratio.toFloat(),
                                centerY + rpoint.y * ratio.toFloat())
                    }
                    .forEachIndexed { index, point ->
                        if (index == 0)
                            moveTo(point)
                        else
                            lineTo(point)
                    }

            close()

        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val contentWidth = (width - paddingLeft - paddingRight).toFloat()
        val contentHeight = (height - paddingTop - paddingBottom).toFloat()
        val centerX = paddingLeft + contentWidth / 2F
        val centerY = paddingTop + contentHeight / 2F
        val radarRadius = (if (contentWidth < contentHeight) contentWidth else contentHeight) / 2F * 0.75F

        if (dataWithLabel.isEmpty()) {
            textPaint.textSize = EMPTY_TEXT_SIZE
            canvas.drawText(
                    emptyText,
                    centerX - textPaint.measureText(emptyText) / 2,
                    centerY,
                    textPaint
            )
            textPaint.textSize = LABEL_TEXT_SIZE
            return
        }

        computePath()

        canvas.apply {
            drawPath(linePath, linePaint)
            drawPath(radarPath, radarPaint)

            dataWithLabel.asSequence()
                    .map { it.label }
                    .zip(rpoints.asSequence())
                    .forEach { (label, rpoint) ->
                        val textWidth = textPaint.measureText(label)
                        val zoom = 1.125F
                        val threshold = 0.5F

                        // adjust position of drawing
                        val xRatio = rpoint.x / radarRadius
                        val x = when (xRatio) {
                        /* left */ in -1F..-threshold ->
                                centerX + rpoint.x * zoom - textWidth
                        /* right */ in threshold..1F ->
                                centerX + rpoint.x * zoom
                        /* center */ else ->
                                centerX + rpoint.x * zoom - textWidth * (0.5F - xRatio)
                        }

                        val yRatio = rpoint.y / radarRadius
                        val y = when (yRatio) {
                        /* above */ in -1F..-threshold ->
                                centerY + rpoint.y * zoom
                        /* below */ in threshold..1F ->
                                centerY + rpoint.y * zoom - textPaint.fontMetrics.ascent
                        /* center */ else ->
                                centerY + rpoint.y * zoom - textPaint.fontMetrics.ascent * (0.5F + yRatio)
                        }

                        drawText(label, x, y, textPaint)
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