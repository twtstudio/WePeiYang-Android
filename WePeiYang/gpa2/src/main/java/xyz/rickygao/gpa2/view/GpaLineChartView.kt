package xyz.rickygao.gpa2.view

import android.content.Context
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import xyz.rickygao.gpa2.R

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
        const val POPUP_BOX_TRI_WIDTH = 80F
        const val POPUP_BOX_TRI_HEIGHT = 40F
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

    private val detailsTextPaint = TextPaint().apply {
        textSize = DETAILS_TEXT_SIZE
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
    private val pointsX = ArrayList<Float>()
    private val pointsY = ArrayList<Float>()
    private var detailTextLeft = 0F
    private var detailTextTop = 0F
    private var detailTextLayout: StaticLayout? = null

    private fun computePath() {

        val dataWithDetail = dataWithDetail.orEmpty()

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom
        val widthStep = contentWidth.toFloat() / (dataWithDetail.size + 1)

        val minData = dataWithDetail.minBy(DataWithDetail::data)?.data ?: 0.0
        val maxData = dataWithDetail.maxBy(DataWithDetail::data)?.data ?: 1.0
        val dataSpan = if (maxData != minData) maxData - minData else 1.0
        val minDataExtended = minData - dataSpan / 4F
        val maxDataExtended = maxData + dataSpan / 4F
        val dataSpanExtended = maxDataExtended - minDataExtended

        (0 until dataWithDetail.size).mapTo(pointsX.apply { clear() }) {
            paddingLeft + widthStep * (it + 1)
        }

        dataWithDetail.mapTo(pointsY.apply { clear() }) {
            paddingTop + ((1 - ((it.data - minDataExtended) / dataSpanExtended)) * contentHeight).toFloat()
        }

        linePath.apply {
            reset()
            var py = (paddingTop + contentHeight).toFloat()
            moveTo(0F, py)
            (0 until dataWithDetail.size).forEach {
                val cx = pointsX[it] - widthStep / 2F
                cubicTo(cx, py, cx, pointsY[it], pointsX[it], pointsY[it])
                py = pointsY[it]
            }
            val cx = width - widthStep / 2F
            cubicTo(cx, py, cx, paddingTop.toFloat(), width.toFloat(), paddingTop.toFloat())
        }

        fillPath.apply {
            reset()
            addPath(linePath)
            lineTo(width.toFloat(), height.toFloat())
            lineTo(0F, height.toFloat())
            close()
        }

        pointPath.apply {
            reset()


            if (dataWithDetail.isEmpty())
                return@apply // no need to draw

            (0 until dataWithDetail.size)
                    .filter { it != selectedIndex }
                    .forEach {
                        addCircle(
                                pointsX[it] - LINE_STROKE / 4F,
                                pointsY[it] - LINE_STROKE / 4F,
                                POINT_RADIUS,
                                Path.Direction.CCW
                        )
                    }
        }

        selectedPointPath.apply {
            reset()

            if (dataWithDetail.isEmpty())
                return@apply // no need to draw

            addCircle(
                    pointsX[selectedIndex] - LINE_STROKE / 4F,
                    pointsY[selectedIndex] - LINE_STROKE / 4F,
                    SELECTED_POINT_RADIUS,
                    Path.Direction.CCW
            )
        }




        popupBoxPath.apply {
            reset()

            if (dataWithDetail.isEmpty())
                return@apply // no need to draw

            val triCenter = pointsX[selectedIndex]
            val triTop = pointsY[selectedIndex] + SELECTED_POINT_RADIUS + POPUP_BOX_MARGIN

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
                    detailsTextPaint,
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

    private fun checkClickOnPoint(x: Float, y: Float, callback: (Int) -> Unit) {
        (0 until dataWithDetail.orEmpty().size).forEach {
            val dx = x - pointsX[it]
            val dy = y - pointsY[it]
            val d2 = dx * dx + dy * dy
            // expand area of click on point
            if (d2 < POINT_RADIUS * POINT_RADIUS * 4) {
                callback(it)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.takeIf { it.action == MotionEvent.ACTION_DOWN }?.let {
            checkClickOnPoint(it.x, it.y) {
                selectedIndex = it
                onSelectionChangedListener?.invoke(it)
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