package com.twt.service.slide.flash

import android.graphics.Point
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.MotionEvent.*
import android.view.ViewConfiguration
import kotlin.math.abs

/**
 * 因为RecyclerView令人费解的事件分发机制，只能纯自己处理
 * 所以写了一个非常Stupid的TouchListener
 */
class StupidRecTouchListener(val scrollListener: ScrollListener) : View.OnTouchListener {
    var willClickFlag = false
    var prevEventPoint = Point(0, 0)
    var downMotionEvent: MotionEvent? = null
    var isScrolling = false
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        Log.d("Rec-Touch", event.toString())

        when (event.action) {
            ACTION_DOWN -> {
                willClickFlag = true
                downMotionEvent?.recycle()
                downMotionEvent = obtain(event)
            }
            ACTION_MOVE -> {
                willClickFlag = willClickFlag && event.x.toInt() == prevEventPoint.x && event.y.toInt() == prevEventPoint.y
                val localDownEvent = downMotionEvent as MotionEvent
                if (abs(event.rawX - localDownEvent.rawX) > ViewConfiguration.get(v.context).scaledTouchSlop) {
                    val moveEvent = event
                    if (!isScrolling) {
                        scrollListener.onScrollStart(event)
                    }
                    isScrolling = true
                    return scrollListener.onScroll(localDownEvent, moveEvent)
                }
            }
            ACTION_UP -> {
                if (isScrolling) {
                    isScrolling = false
                    scrollListener.onScrollEnd(event)
                }
                if (willClickFlag && event.x.toInt() == prevEventPoint.x && event.y.toInt() == prevEventPoint.y) {
                    v.performClick()
                    willClickFlag = false
                    prevEventPoint.apply {
                        x = event.x.toInt()
                        y = event.y.toInt()
                    }
                    return true
                }
            }
        }
        prevEventPoint.apply {
            x = event.x.toInt()
            y = event.y.toInt()
        }

        return true
    }
}

interface ScrollListener {
    fun onScrollStart(motionEvent: MotionEvent)
    fun onScroll(startMotionEvent: MotionEvent, endMotionEvent: MotionEvent): Boolean
    fun onScrollEnd(motionEvent: MotionEvent)
}