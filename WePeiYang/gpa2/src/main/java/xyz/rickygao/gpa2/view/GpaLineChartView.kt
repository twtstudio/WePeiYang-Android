package xyz.rickygao.gpa2.view

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
import com.twt.wepeiyang.commons.experimental.CommonContext
import xyz.rickygao.gpa2.R
import xyz.rickygao.gpa2.extensions.*

/**
 * Created by rickygao on 2017/11/9.
 */
// @JvmOverloads annotation fixes default value of kotlin's function parameter for java reflection
class GpaLineChartView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle) {

    /**
     * padding                                     padding
     *  Left   width  t0        t1        t2        Right
     *  |   |  step   |         |         |         |   |
     *  +---+---------+---------+---------+---------+---+-
     *  |   |                                       |   | paddingTop
     *  + - + - - - - - - - - - - - - - - - - - - - + - +- maxScoreExtended
     *  |   |          ________(*)________          |  /|
     *  | __+_________*                   *_________+_/ |  scoreSpanExtended
     *  |/  |                                       |   |
     *  + - + - - - - - - - - - - - - - - - - - - - + - +- minScoreExtended
     *  |   |                                       |   | paddingBottom
     *  +---+---------------------------------------+---+
     */

    companion object {
        // fixed constant
        const val LINE_STROKE = 16F
        const val POINT_RADIUS = 24F
        const val SELECTED_POINT_RADIUS = 28F
        const val SELECTED_POINT_STROKE_WIDTH = 20F
        const val SELECTED_POINT_STROKE_COLOR = Color.WHITE
        const val POPUP_BOX_COLOR = Color.WHITE
        const val POPUP_BOX_TRI_WIDTH = 64F
        const val POPUP_BOX_TRI_HEIGHT = 32F
        const val POPUP_BOX_RECT_WIDTH = 320F
        const val POPUP_BOX_RECT_HEIGHT = 320F
        const val POPUP_BOX_RECT_ROUND_RADIUS = 16F
        const val POPUP_BOX_MARGIN = 40F
        const val POPUP_BOX_PADDING = 40F
        const val DETAILS_TEXT_SIZE = 36F
        const val SHADOW_RADIUS = 16F
        const val SHADOW_COLOR = 0x66666666

        // default constant for attrs
        const val DEFAULT_LINE_COLOR = 0xFFEC826A.toInt()
        const val DEFAULT_FILL_COLOR = 0xFFF3AB9B.toInt()
        const val DEFAULT_POINT_COLOR = 0xFFEC826A.toInt()
    }

    var onSelectionChangedListener: ((Int) -> Unit)? = null

    private val linePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = LINE_STROKE
        setShadowLayer(SHADOW_RADIUS, 0F, 0F, SHADOW_COLOR)
        isAntiAlias = true
    }

    private val fillPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val pointPaint = Paint().apply {
        style = Paint.Style.FILL
        setShadowLayer(SHADOW_RADIUS, 0F, 0F, SHADOW_COLOR)
        isAntiAlias = true
    }

    private val selectedPointStrokePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = SELECTED_POINT_STROKE_WIDTH
        color = SELECTED_POINT_STROKE_COLOR
        setShadowLayer(SHADOW_RADIUS, 0F, 0F, SHADOW_COLOR)
        isAntiAlias = true
    }

    private val selectedPointPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val popupBoxPaint = Paint().apply {
        style = Paint.Style.FILL
        color = POPUP_BOX_COLOR
        setShadowLayer(SHADOW_RADIUS, 0F, 0F, SHADOW_COLOR)
        isAntiAlias = true
    }

    private val detailTextPaint = TextPaint().apply {
        textSize = DETAILS_TEXT_SIZE
        isAntiAlias = true
        typeface = ResourcesCompat.getFont(CommonContext.application.applicationContext, R.font.montserrat_regular)
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
            selectedPointPaint.color = value
        }

    data class DataWithDetail(val data: Double, val detail: String)

    var dataWithDetail: List<DataWithDetail> = emptyList()
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
    private val selectedPointPath = Path()
    private val popupBoxPath = Path()
    //    private val pointsX = mutableListOf<Float>()
//    private val pointsY = mutableListOf<Float>()
    private val points = mutableListOf<PointF>()
    private var detailTextLeft = 0F
    private var detailTextTop = 0F
    private var detailTextLayout: StaticLayout? = null

    private fun computePath() {

        val contentWidth = (width - paddingLeft - paddingRight).toFloat()
        val contentHeight = (height - paddingTop - paddingBottom).toFloat()
        val centerY = paddingTop + contentHeight / 2
        val widthStep = contentWidth / (dataWithDetail.size + 1)

        points.clear()
        if (dataWithDetail.isNotEmpty()) {
            val minData = dataWithDetail.minBy(DataWithDetail::data)?.data!!
            val maxData = dataWithDetail.maxBy(DataWithDetail::data)?.data!!
            val dataSpan = maxData - minData

            if (dataSpan > 0F) {
                // not all the same
                val extension = dataSpan * 0.382F
                val minDataExtended = minData - extension
                val maxDataExtended = maxData + extension
                val dataSpanExtended = maxDataExtended - minDataExtended

                dataWithDetail.asSequence()
                        .map { (it.data - minDataExtended) / dataSpanExtended }
                        .mapIndexedTo(points) { index, ratio ->
                            PointF(paddingLeft + widthStep * (index + 1),
                                    paddingTop + (1 - ratio.toFloat()) * contentHeight)
                        }

            } else {

                dataWithDetail.mapIndexedTo(points) { index, _ ->
                    PointF(paddingLeft + widthStep * (index + 1), centerY)
                }

            }

        }

        linePath.reuse {

            if (points.isNotEmpty()) {

                val startPoint = PointF(0F, (points.first().y * 1.2F).coerceAtMost(paddingTop + contentHeight))
                val endPoint = PointF(width.toFloat(), (points.last().y * 0.8F).coerceAtLeast(paddingTop.toFloat()))

                moveTo(startPoint)
                cubicThrough(listOf(startPoint, points.first()))
                cubicThrough(points)
                cubicThrough(listOf(points.last(), endPoint))

            } else {

                moveTo(0F, centerY)
                lineTo(width.toFloat(), centerY)

            }

        }

        fillPath.reuse {

            addPath(linePath)
            lineTo(width.toFloat(), height.toFloat())
            lineTo(0F, height.toFloat())
            close()

        }

        pointPath.reuse {

            points.asSequence()
                    .filterIndexed { index, _ -> index != selectedIndex }
                    .forEach { (x, y) -> addCircle(x, y, POINT_RADIUS, Path.Direction.CCW) }

        }

        selectedPointPath.reuse {

            if (points.isEmpty())
                return@reuse

            addCircle(
                    points[selectedIndex].x,
                    points[selectedIndex].y,
                    SELECTED_POINT_RADIUS,
                    Path.Direction.CCW
            )

        }

        popupBoxPath.reuse {

            if (dataWithDetail.isEmpty())
                return@reuse // no need to draw

            val triCenter = points[selectedIndex].x
            val triTop = points[selectedIndex].y + SELECTED_POINT_RADIUS + POPUP_BOX_MARGIN

            moveTo(triCenter, triTop)
            lineTo(triCenter - POPUP_BOX_TRI_WIDTH / 2F, triTop + POPUP_BOX_TRI_HEIGHT)
            lineTo(triCenter + POPUP_BOX_TRI_WIDTH / 2F, triTop + POPUP_BOX_TRI_HEIGHT)
            close()

            val rectCenter =
                    when {
                        triCenter - POPUP_BOX_RECT_WIDTH / 2F < POPUP_BOX_MARGIN -> POPUP_BOX_MARGIN + POPUP_BOX_RECT_WIDTH / 2F
                        triCenter + POPUP_BOX_RECT_WIDTH / 2F > width - POPUP_BOX_MARGIN -> width - POPUP_BOX_MARGIN - POPUP_BOX_RECT_WIDTH / 2F
                        else -> triCenter
                    }

            val rectTop = triTop + POPUP_BOX_TRI_HEIGHT

            detailTextLayout = StaticLayout(
                    dataWithDetail[selectedIndex].detail,
                    detailTextPaint,
                    (POPUP_BOX_RECT_WIDTH - POPUP_BOX_PADDING * 2).toInt(),
                    Layout.Alignment.ALIGN_NORMAL,
                    1.75F,
                    0F,
                    true
            ).also {
                detailTextLeft = rectCenter - it.width / 2F
                detailTextTop = rectTop + POPUP_BOX_PADDING
            }

            val rectHeight = detailTextLayout?.height?.toFloat() ?: POPUP_BOX_RECT_HEIGHT

            addRoundRect(
                    RectF(rectCenter - POPUP_BOX_RECT_WIDTH / 2F,
                            rectTop,
                            rectCenter + POPUP_BOX_RECT_WIDTH / 2F,
                            rectTop + rectHeight + POPUP_BOX_PADDING * 2F),
                    POPUP_BOX_RECT_ROUND_RADIUS,
                    POPUP_BOX_RECT_ROUND_RADIUS,
                    Path.Direction.CCW
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        computePath()

        canvas.apply {
            // disable hardware acceleration for a perfect display of shadows
            if (isHardwareAccelerated) setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            drawPath(linePath, linePaint)
            drawPath(fillPath, fillPaint)
            drawPath(pointPath, pointPaint)
            drawPath(selectedPointPath, selectedPointStrokePaint)
            drawPath(selectedPointPath, selectedPointPaint)
            drawPath(popupBoxPath, popupBoxPaint)
//            setLayerType(View.LAYER_TYPE_HARDWARE, null)

            save()
            translate(detailTextLeft, detailTextTop)
            detailTextLayout?.draw(canvas)
            restore()
        }
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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        event.takeIf { it.action == MotionEvent.ACTION_DOWN }?.let {
            checkClickOnPoint(it.x, it.y) {
                selectedIndex = it
                onSelectionChangedListener?.invoke(it)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.GpaLineChartView, defStyle, 0).apply {

            lineColor = getColor(R.styleable.GpaLineChartView_lineColor, DEFAULT_LINE_COLOR)

            fillColor = getColor(R.styleable.GpaLineChartView_fillColor, DEFAULT_FILL_COLOR)

            pointColor = getColor(R.styleable.GpaLineChartView_pointColor, DEFAULT_POINT_COLOR)

            recycle()
        }
    }

}