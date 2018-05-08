package com.twt.service.schedule2.view.theme

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec.AT_MOST
import android.view.View.MeasureSpec.makeMeasureSpec
import android.view.ViewGroup
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView

/**
 * 简易的链条布局 仿造约束布局里面的链条
 * | * -- * -- * -- * |
 */
class SpreadChainLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : ViewGroup(context, attrs, defStyle) {

    val TAG = "SpreadChainLayout"
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var childHeightMeasureSpec = 0
        var childWidthMeasureSpec = 0
        var maxHeight = 0
        for (i in 0 until childCount) {
            val childView: View = getChildAt(i)
            measureChild(childView, widthMeasureSpec, heightMeasureSpec)
//            Log.e(TAG,"childHeight: ${childView.measuredHeight} childWidth: ${childView.measuredWidth}")
//            Log.e(TAG,"Height: $measuredHeight Width: $measuredWidth")
            childHeightMeasureSpec = makeMeasureSpec(childView.measuredHeight, AT_MOST)
            childWidthMeasureSpec = makeMeasureSpec(childView.measuredWidth, AT_MOST)
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec)

            if (childView.measuredHeight > maxHeight) {
                maxHeight = childView.measuredHeight
            }
        }

        setMeasuredDimension(getDefaultSize(suggestedMinimumWidth, widthMeasureSpec), View.resolveSizeAndState(maxHeight, heightMeasureSpec, 0))
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
//        Log.e(TAG, "left -> $left, top -> $top, right -> $right, bottom -> $bottom")
        val childCount = getChildCount()
        var childTop = paddingTop
        var childLeft = paddingStart

        val totalChildrenWidth = (0 until childCount).map { getChildAt(it) }.fold(0) { acc, view ->
            acc + view.measuredWidth
        }
        val spaceWidth = (measuredWidth - totalChildrenWidth - paddingStart - paddingEnd) / (childCount - 1)

        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            childView.layout(childLeft, childTop, childLeft + childView.measuredWidth, childTop + childView.measuredHeight)
            childLeft += (childView.measuredWidth + spaceWidth)
        }
    }

    inline fun <T : View> T.lparams(
            width: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
            height: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
            init: ViewGroup.LayoutParams.() -> Unit
    ): T {
        val layoutParams = ViewGroup.MarginLayoutParams(width, height)
        layoutParams.init()
        this.layoutParams = layoutParams
        return this
    }
}

fun ViewManager.spreadChainLayout(init: SpreadChainLayout.() -> Unit): SpreadChainLayout {
    return ankoView({ SpreadChainLayout(it) }, theme = 0, init = init)
}
