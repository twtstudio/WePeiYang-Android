package com.twt.service.ecard.view

import android.content.Context
import android.graphics.*
import android.support.v4.content.res.ResourcesCompat
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SoundEffectConstants
import android.view.View
import com.twt.service.ecard.R
import com.twt.service.ecard.extansion.*
import com.twt.wepeiyang.commons.experimental.CommonContext
import org.jetbrains.anko.dip

class EcardChartView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private val LINE_STROKE = dip(2)
    var widthStep: Float = 0F
    var downX = 0F

    var onSelectionChangedListener: ((Int) -> Unit)? = null

    private val linePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = LINE_STROKE.toFloat()
        color = Color.parseColor("#ffe043")
        isSubpixelText = true
        isAntiAlias = true
    }

    private val bottomLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = LINE_STROKE.toFloat()
        color = Color.parseColor("#555555")
        isSubpixelText = true
        isAntiAlias = true
    }

    private val pointPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val pointPaintWhite = Paint().apply {
        style = Paint.Style.FILL
        color = Color.WHITE
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        textSize = DETAILS_TEXT_SIZE
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        color = Color.parseColor("#666666")
        typeface = ResourcesCompat.getFont(context, R.font.montserrat_regular)
    }

    private val fillPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val detailTextPaint = TextPaint().apply {
        textSize = DETAILS_TEXT_SIZE
        color = Color.parseColor("#666666")
        isAntiAlias = true
        typeface = ResourcesCompat.getFont(CommonContext.application.applicationContext, R.font.montserrat_regular)
    }

    private val popupBoxPaint = Paint().apply {
        style = Paint.Style.FILL
        setShadowLayer(SHADOW_RADIUS, 0F, 0F, SHADOW_COLOR)
        color = POPUP_BOX_COLOR
        isAntiAlias = true
    }

    var lineColor
        get() = linePaint.color
        set(value) {
            linePaint.color = value
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

    var distanceOfBegin: Double = 0.0
        set(value) {
            if (value <= 0 && value >= -widthStep * (dataWithDetail.size - 5))
                field = value
            invalidate()
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
    private val whitePointPath = Path()
    private val centerPointPath = Path()
    private val bottomLinePath = Path()
    private val popupBoxPath = Path()
    private val points = mutableListOf<PointF>()
    private var detailTextLeft = 0F
    private var detailTextTop = 0F
    private var detailTextLayout: StaticLayout? = null

    private fun computePath() {
        val contentWidth = (width - paddingLeft - paddingRight).toFloat()
        val contentHeight = (height - paddingTop - paddingBottom).toFloat()

        widthStep = contentWidth / 4

        val centerY = paddingTop + contentHeight / 2
        val startX = paddingLeft + distanceOfBegin // * widthStep * (dataWithDetail.size - 5)
        val endX = widthStep * (dataWithDetail.size - 1) * (1 - distanceOfBegin)

        points.clear()
        if (dataWithDetail.isNotEmpty()) {
            val maxData = dataWithDetail.maxBy(DataWithDetail::data)?.data!!
            // not all the same
            val extension = 10f
            val minDataExtended = 0f
            val maxDataExtended = maxData + extension
            val dataSpanExtended = maxDataExtended - minDataExtended

            if (dataWithDetail.size == 1) {
                points.add(PointF(startX.toFloat(), extension * contentHeight / dataSpanExtended.toFloat()))
            } else {
                /*单个数据的时候不执行……蜜汁问题*/

                dataWithDetail.asSequence()
                        .map { (it.data - minDataExtended) / dataSpanExtended }
                        .mapIndexedTo(points) { index, ratio ->
                            PointF((startX + widthStep * (index)).toFloat(),
                                    (1 - ratio.toFloat()) * (contentHeight - dip(22)))
                        }
            }
        }

        linePath.reuse {
            if (points.isNotEmpty()) {
                moveTo(points.first())
                cubicThrough(points)

            } else {
                moveTo(startX.toFloat(), centerY)
                lineTo(pivotX - endX.toFloat(), centerY)
            }
        }

        bottomLinePath.reuse {
            moveTo(points.first().x, height.toFloat() - dip(22))
            lineTo(points.last().x, height.toFloat() - dip(22))
        }

        fillPath.reuse {
            addPath(linePath)
            lineTo(points.last().x, height.toFloat() - dip(22))
            lineTo(points.first().x, height.toFloat() - dip(22))
            close()
        }

        val firstPoint = points.removeAt(0)
        val lastPoint = points.removeAt(points.size - 1)
        pointPath.reuse {
            points.asSequence()
                    .forEach { (x, y) -> addCircle(x, y, POINT_RADIUS, Path.Direction.CCW) }
        }

        whitePointPath.reuse {
            points.asSequence()
                    .forEach { (x, y) -> addCircle(x, y, WHITE_POINT_RADIUS, Path.Direction.CCW) }
        }

        centerPointPath.reuse {
            points.asSequence()
                    .forEach { (x, y) -> addCircle(x, y, CENTER_POINT_RADIUS, Path.Direction.CCW) }
        }
        points.add(0, firstPoint)
        points.add(lastPoint)

        popupBoxPath.reuse {
            if (dataWithDetail.isEmpty())
                return@reuse // no need to draw

            val triCenter = points[selectedIndex].x
            val triTop = points[selectedIndex].y - POINT_RADIUS - POPUP_BOX_MARGIN

            moveTo(triCenter, triTop)
            lineTo(triCenter - POPUP_BOX_TRI_WIDTH / 2F, triTop - POPUP_BOX_TRI_HEIGHT)
            lineTo(triCenter + POPUP_BOX_TRI_WIDTH / 2F, triTop - POPUP_BOX_TRI_HEIGHT)
            close()

            val rectCenter =
                    when {
                        triCenter - POPUP_BOX_RECT_WIDTH / 2F < POPUP_BOX_MARGIN -> POPUP_BOX_MARGIN + POPUP_BOX_RECT_WIDTH / 2F
                        triCenter + POPUP_BOX_RECT_WIDTH / 2F > width - POPUP_BOX_MARGIN -> width - POPUP_BOX_MARGIN - POPUP_BOX_RECT_WIDTH / 2F
                        else -> triCenter
                    }
            val rectTop = triTop - POPUP_BOX_TRI_HEIGHT

            detailTextLayout = StaticLayout(
                    "${dataWithDetail[selectedIndex].data}元",
                    detailTextPaint,
                    (POPUP_BOX_RECT_WIDTH - POPUP_BOX_PADDING * 2).toInt(),
                    Layout.Alignment.ALIGN_NORMAL,
                    1.75F,
                    0F,
                    true
            ).also {
                detailTextLeft = rectCenter - it.width / 2F
                detailTextTop = rectTop - POPUP_BOX_PADDING
            }

            val rectHeight = detailTextLayout?.height?.toFloat() ?: POPUP_BOX_RECT_HEIGHT

            addRoundRect(
                    RectF(rectCenter - POPUP_BOX_RECT_WIDTH / 2F,
                            rectTop,
                            rectCenter + POPUP_BOX_RECT_WIDTH / 2F,
                            rectTop - rectHeight - POPUP_BOX_PADDING),
                    POPUP_BOX_RECT_ROUND_RADIUS,
                    POPUP_BOX_RECT_ROUND_RADIUS,
                    Path.Direction.CCW)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (dataWithDetail.size > 4) {
            computePath()
        }

        canvas.apply {
            if (points.size > 1) {
                drawPath(fillPath, fillPaint)
                drawPath(linePath, linePaint)
            }
            drawPath(pointPath, pointPaint)
            drawPath(whitePointPath, pointPaintWhite)
            drawPath(centerPointPath, pointPaint)
            drawPath(bottomLinePath, bottomLinePaint)

            points.asSequence().forEachIndexed { index, (x, y) ->
                drawText(dataWithDetail[index].year, x, height.toFloat(), textPaint)
            }

            drawPath(popupBoxPath, popupBoxPaint)
            save()
            translate(detailTextLeft, detailTextTop)
            detailTextLayout?.draw(canvas)
            restore()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.rawX!!

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = x
                checkClickOnPoint(event.x, event.y) {
                    selectedIndex = it
                    onSelectionChangedListener?.invoke(it)
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                parent.requestDisallowInterceptTouchEvent(true)
                val moveX = x - downX
                distanceOfBegin += (moveX / 15)
            }
        }

        return super.dispatchTouchEvent(event)
    }

    private inline fun checkClickOnPoint(x: Float, y: Float, callback: (Int) -> Unit) {
        points.forEachIndexed { index, (px, py) ->
            val dx = x - px
            val dy = y - py
            val d2 = dx * dx + dy * dy
            // expand area of click on point
            if (d2 < POINT_RADIUS * POINT_RADIUS * 4) {
                callback(index)
                playSoundEffect(SoundEffectConstants.CLICK)
            }
        }
        performClick()
    }


    init {
        context.obtainStyledAttributes(attrs, R.styleable.EcardChartView, defStyleAttr, 0).apply {

            fillColor = getColor(R.styleable.EcardChartView_fillColor, EcardChartView.DEFAULT_FILL_COLOR)

            pointColor = getColor(R.styleable.EcardChartView_pointColor, EcardChartView.DEFAULT_POINT_COLOR)

            lineColor = getColor(R.styleable.EcardChartView_lineColor, EcardChartView.DEFAULT_LINE_COLOR)

            recycle()
        }
    }

    companion object {
        const val DEFAULT_LINE_COLOR = 0xFFFFE043.toInt()
        const val DEFAULT_FILL_COLOR = 0xFFFFF5C2.toInt()
        const val DEFAULT_POINT_COLOR = 0xFFFFE043.toInt()
        const val POINT_RADIUS = 25F
        const val WHITE_POINT_RADIUS = 21F
        const val CENTER_POINT_RADIUS = 12F
        const val DETAILS_TEXT_SIZE = 30F
        const val POPUP_BOX_TRI_WIDTH = 15F
        const val POPUP_BOX_TRI_HEIGHT = 16F
        const val POPUP_BOX_MARGIN = 16F
        const val POPUP_BOX_RECT_WIDTH = 166F
        const val POPUP_BOX_PADDING = 40F
        const val POPUP_BOX_RECT_HEIGHT = 72F
        const val POPUP_BOX_RECT_ROUND_RADIUS = 16F
        const val SHADOW_COLOR = 0xf3f3f3
        const val SHADOW_RADIUS = 35F
        const val POPUP_BOX_COLOR = Color.WHITE
    }
}