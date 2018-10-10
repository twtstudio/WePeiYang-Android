package xyz.rickygao.gpa2.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import xyz.rickygao.gpa2.R
import xyz.rickygao.gpa2.extensions.*
import android.graphics.Shader
import android.graphics.LinearGradient
import org.jetbrains.anko.dip


/**
 * Created by asus on 2018/5/11.
 */
class GpaMiniLineChartView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private val LINE_STROKE = dip(1)
    private val POINT_RADIUS = dip(0f)
    private val POINT_STROKE_WIDTH = 4F
    private val AXIS_STROKE = 0.8F
    private val DOTTED_LINE_STROKE = 3F
    private val DETAILS_TEXT_SIZE = dip(9)


    private val DottedLineStrokePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#D3D3D3")
        strokeWidth = DOTTED_LINE_STROKE
        isAntiAlias = true

    }

    private val AxisPaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#F5F5F5")
        strokeWidth = AXIS_STROKE
        isSubpixelText = true
        isAntiAlias = true
    }

    private val linePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = LINE_STROKE.toFloat()
        color = Color.parseColor("#DBB86B")
        isSubpixelText = true
        isAntiAlias = true

    }

    private val fillPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true

    }

    private val pointPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.WHITE
        isAntiAlias = true
    }

    private val pointStrokePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#DBB86B")
        strokeWidth = POINT_STROKE_WIDTH
    }

    private val AxisTextPaint = Paint().apply {
        textSize = DETAILS_TEXT_SIZE.toFloat()
        textAlign = Paint.Align.RIGHT
        isAntiAlias = true

    }
    private val TextPaint = Paint().apply {
        textSize = DETAILS_TEXT_SIZE.toFloat()
        textAlign = Paint.Align.CENTER
        isAntiAlias = true

    }

    var fillColor
        get() = fillPaint.color
        set(value) {
            fillPaint.color = value
        }

    var pointColor
        get() = pointPaint.color
        set(value) {
            pointPaint.color = value
        }

    data class DataWithDetail(val data: Double, val year: String)

    var dataWithDetail: MutableList<DataWithDetail> = mutableListOf()
        set(value) {
            field = value
            selectedIndex = selectedIndex // ha?
            invalidate()
        }

    var selectedIndex = 0
        set(value) {
            field = value.coerceIn(if (dataWithDetail.isNotEmpty()) dataWithDetail.indices else 0..0)
            invalidate()
        }


    private val linePath = Path()
    private val fillPath = Path()
    private val pointPath = Path()
    private val dottedLineStrokePath = Path()
    private val axisPath = Path()
    private val points = mutableListOf<PointF>()

    private fun computePath() {

        val contentWidth = (width - paddingLeft - paddingRight - 48).toFloat()
        val contentHeight = (height - paddingTop - paddingBottom).toFloat()
        for (dw in dataWithDetail) {
            if (dw.year == "第二课堂") {
                dataWithDetail.remove(dw)
            }
        }
        val widthStep = contentWidth / (dataWithDetail.size - 1.toFloat())


        val centerY = paddingTop + contentHeight / 2
        val startX = paddingLeft + 24
        val endX = paddingRight + 24


        points.clear()
        if (dataWithDetail.isNotEmpty()) {
            val minData = dataWithDetail.minBy(DataWithDetail::data)?.data!!
            val maxData = dataWithDetail.maxBy(DataWithDetail::data)?.data!!
            // not all the same
            val extension = 0f
            val minDataExtended = minData - extension
            val maxDataExtended = maxData + extension
            val dataSpanExtended = maxDataExtended - minDataExtended
            if (dataWithDetail.size == 1) {
                points.add(PointF(startX.toFloat(), centerY))
            } else {
                /*单个数据的时候不执行……蜜汁问题*/
                dataWithDetail.asSequence()
                        .map { (it.data - minDataExtended) / dataSpanExtended }
                        .mapIndexedTo(points) { index, ratio ->
                            PointF(startX + widthStep * (index),
                                    paddingTop + (1 - ratio.toFloat()) * contentHeight * 0.8.toFloat())
                        }
            }


        }

        linePath.reuse {
            if (points.isNotEmpty()) {
                moveTo(points.first())
                cubicThrough(points)

            } else {
                moveTo(startX.toFloat(), centerY)
                lineTo(width - endX.toFloat(), centerY)
            }
        }

        fillPath.reuse {

            addPath(linePath)
            lineTo(width - endX.toFloat(), height - paddingBottom.toFloat())
            lineTo(startX.toFloat(), height - paddingBottom.toFloat())
            close()
        }

        pointPath.reuse {

            points.asSequence()
                    .forEach { (x, y) -> addCircle(x, y, POINT_RADIUS.toFloat(), Path.Direction.CCW) }
        }

        dottedLineStrokePath.reuse {
            moveTo(paddingLeft.toFloat(), paddingTop.toFloat())
            lineTo(paddingLeft.toFloat(), height - paddingBottom.toFloat())
            moveTo(paddingLeft * 1.2.toFloat(), height - paddingBottom.toFloat())
            lineTo(width - paddingRight.toFloat(), height - paddingBottom.toFloat())
        }

        axisPath.reuse {
            val interval = (height - paddingTop - paddingBottom) / 5
            val lengthRight = (width - paddingRight * 1.2.toFloat())
            val lengthLeft = paddingLeft * 1.2.toFloat()
            moveTo(lengthLeft, paddingTop.toFloat())
            lineTo(lengthRight, paddingTop.toFloat())
            moveTo(lengthLeft, paddingTop.toFloat() + interval)
            lineTo(lengthRight, paddingTop.toFloat() + interval)
            moveTo(lengthLeft, paddingTop.toFloat() + 2 * interval)
            lineTo(lengthRight, paddingTop.toFloat() + 2 * interval)
            moveTo(lengthLeft, paddingTop.toFloat() + 3 * interval)
            lineTo(lengthRight, paddingTop.toFloat() + 3 * interval)
            moveTo(lengthLeft, paddingTop.toFloat() + 4 * interval)
            lineTo(lengthRight, paddingTop.toFloat() + 4 * interval)
        }

    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        computePath()
        val shader = LinearGradient(width / 2.toFloat(), 0.toFloat(), width / 2.toFloat(), height - paddingBottom.toFloat(), Color.parseColor("#99DBB86B"),
                Color.parseColor("#00FFFFFF"), Shader.TileMode.CLAMP)
        fillPaint.shader = shader

        canvas.apply {
            drawPath(dottedLineStrokePath, DottedLineStrokePaint)
            drawPath(axisPath, AxisPaint)
            if (points.size > 1) {
                drawPath(fillPath, fillPaint)
                drawPath(linePath, linePaint)
            }

            drawPath(pointPath, pointPaint)
            drawPath(pointPath, pointStrokePaint)
            val interval = (height - paddingTop - paddingBottom) / 5
            val lengthLeft2 = paddingLeft * 0.8.toFloat()
            if (dataWithDetail.isNotEmpty()) {
                val minScore = ((dataWithDetail.minBy(DataWithDetail::data)?.data!!) * 100).toInt()
                val maxScore = ((dataWithDetail.maxBy(DataWithDetail::data)?.data!!) * 100).toInt()
                val minScore2 = minScore / 100.toFloat()
                val maxScore2 = maxScore / 100.toFloat()
                val subt = (((maxScore2 - minScore2 - 0.1) / 3) * 100).toInt()
                val sbut = subt / 100.toFloat()

                if (minScore2 == maxScore2) {
                    drawText("4.0", lengthLeft2, paddingTop.toFloat() + dip(2), AxisTextPaint)
                    drawText(minScore2.toString(), lengthLeft2, paddingTop + dip(2) + (height - paddingTop - paddingBottom) / 2.toFloat(), AxisTextPaint)
                    drawText("0.0", lengthLeft2, height - paddingBottom.toFloat() + dip(2), AxisTextPaint)
                } else {
                    drawText(maxScore2.toString(), lengthLeft2, paddingTop.toFloat() + dip(2), AxisTextPaint)
                    drawText(minScore2.toString(), lengthLeft2, paddingTop.toFloat() + dip(2) + 4 * interval.toFloat(), AxisTextPaint)
                    for (h in 2..4) {
                        val x: Float = minScore2 + (h - 1) * sbut
                        val y = Math.round(x * 100) / 100.toDouble()
                        drawText(y.toString(), lengthLeft2, paddingTop + (5 - h) * interval.toFloat() + dip(2), AxisTextPaint)
                    }
                    val y = (((minScore2 - sbut) * 100).toInt()) / 100.toFloat()
                    drawText(y.toString(), lengthLeft2, paddingTop.toFloat() + dip(2) + 5 * interval.toFloat(), AxisTextPaint)

                }
            } else {
                val sq = 3.0
                for (h in 0..5) {
                    val x = sq + h * 0.2
                    val y = ((x * 10).toInt()) / 10.toFloat()
                    drawText(y.toString(), lengthLeft2, paddingTop + (5 - h) * interval.toFloat() + dip(2), AxisTextPaint)
                }
            }

            for ((j, i) in points.withIndex()) {
                drawText(dataWithDetail[j].year, i.x, height - paddingTop + 4.toFloat(), TextPaint)
            }

        }
    }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.GpaMiniLineChartView, defStyleAttr, 0).apply {

            fillColor = getColor(R.styleable.GpaMiniLineChartView_fillColor, GpaLineChartView.DEFAULT_FILL_COLOR)

            pointColor = getColor(R.styleable.GpaMiniLineChartView_pointColor, GpaLineChartView.DEFAULT_POINT_COLOR)

            recycle()
        }
    }
}