package com.twt.service.slide.flash

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.view.WindowManager.LayoutParams.FLAG_DIM_BEHIND
import android.widget.FrameLayout
import com.twt.service.schedule2.extensions.dp2px
import com.twt.service.schedule2.model.total.TotalCourseManager
import com.twt.service.slide.R
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import com.twt.wepeiyang.commons.ui.rec.withItems
import kotlinx.android.synthetic.main.flash_layout_float_window.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.forEachChild
import org.jetbrains.anko.matchParent
import java.text.SimpleDateFormat
import java.util.*

class FlashFloatWindow2(val context: Context) {
    val TAG = "FlashWindow2"
    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val floatViewGroup = LayoutInflater.from(context).inflate(R.layout.flash_layout_float_window, null, false)
    private val recyclerView: RecyclerView = floatViewGroup.rec_flash_today
    private val dragBar: FrameLayout = floatViewGroup.frame_drag_bar

    private val screenWidth = 1080f
    private var isAdded = false

    private val pannelViewParams = WindowManager.LayoutParams().apply {
        x = 0
        y = 400
        gravity = Gravity.END or Gravity.TOP
        width = dp2px(10f, context).toInt()
        height = dp2px(80f, context).toInt()
        type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        format = PixelFormat.RGBA_8888
    }
    private val expandViewParams = WindowManager.LayoutParams().apply {
        type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        flags = FLAG_DIM_BEHIND
        dimAmount = 0.4f
        format = PixelFormat.RGBA_8888
        width = matchParent
        height = matchParent
    }

    init {
        floatViewGroup.backgroundColor = Color.parseColor("#00000000")
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            TotalCourseManager.getTotalCourseManager().observeForever { classtableProvider ->
                if (classtableProvider == null) return@observeForever
                withItems {
                    flashTodayHeader()
                    val courses = classtableProvider.getTodayCourse().filter {
                        it.dayAvailable && it.weekAvailable
                    }.sortedBy { course ->
                        course.arrange.getOrNull(0)?.start ?: 100 //如果越界或者没有就给他个100
                    }
                    val dateFormatMonth = SimpleDateFormat("HH:mm")
                    courses.map {
                        FlashCourseData(
                                courseName = it.coursename,
                                courseColor = getColorCompat(it.courseColor),
                                courseLocation = it.arrange[0].room.replace("楼", " - "),
                                courseTime = "${dateFormatMonth.format(Date(offsetTimetable(it.arrange[0].start) * 1000L))} - ${dateFormatMonth.format(Date(offsetTimetable(it.arrange[0].end, getEnd = true) * 1000L))}"
                        )
                    }.forEach {
                        flashCourse(it)
                    }
                }
            }

        }

        val detectorNewForRec = StupidRecTouchListener(object : ScrollListener {
            override fun onScrollStart(motionEvent: MotionEvent) = Unit

            override fun onScroll(startMotionEvent: MotionEvent, endMotionEvent: MotionEvent): Boolean {
                val distance = endMotionEvent.rawX - startMotionEvent.rawX
                floatViewGroup.translationX = distance
                return true
            }

            override fun onScrollEnd(motionEvent: MotionEvent) {
                closeRecExpandAnim()
            }
        })

        recyclerView.setOnTouchListener(detectorNewForRec)
        recyclerView.setOnClickListener {
            closeRecExpandAnim()
        }

    }

    fun showFloatWindow() {

        windowManager.addView(floatViewGroup, pannelViewParams)

        val detectorNew = StupidRecTouchListener(object : ScrollListener {
            override fun onScrollStart(motionEvent: MotionEvent) {
            }

            override fun onScroll(startMotionEvent: MotionEvent, endMotionEvent: MotionEvent): Boolean {
                Log.d("Scroll:", "startMotionEvent: ${startMotionEvent.rawX} endMotionEvent: ${endMotionEvent.rawX} ")
                if (!isAdded) {
                    floatViewGroup.visibility = View.INVISIBLE
                    toggleState(true)
                    floatViewGroup.post {
                        // todo 暂时使用控制整体Visibility的方式来避免闪烁 到时候再看看有没有啥其他方案
                        floatViewGroup.visibility = View.VISIBLE
                    }
                    isAdded = true
                }
                if (isAdded) {
                    floatViewGroup.translationX = screenWidth / 2 - (startMotionEvent.rawX - endMotionEvent.rawX)  /*- 400*/
                    Log.d("ViewGroupTranX: ", "tranX : ${floatViewGroup.translationX}")
                }
                Log.d(TAG, "recyclerwidth: ${floatViewGroup.rec_flash_today.width} + ${floatViewGroup.rec_flash_today.height}")
                return true
            }

            override fun onScrollEnd(motionEvent: MotionEvent) {
                val animator = ObjectAnimator.ofFloat(floatViewGroup, "translationX", 0f).apply {
                    duration = 300L
                }
                animator.start()
            }

        })
        floatViewGroup.frame_drag_bar.setOnTouchListener(detectorNew)

        floatViewGroup.frame_drag_bar.setOnClickListener {
            Log.d(TAG, "Drag Click!")
        }


    }

    /**
     * 获取上课/下课时间对于当天零点的相对时间戳
     */
    fun offsetTimetable(startNumber: Int, getEnd: Boolean = false): Int {
        var offset: Int = when (startNumber) {
            1 -> 1800
            2 -> 4800
            3 -> 8700
            4 -> 11700 // 上午四节课的开始时间
            5 -> 19800
            6 -> 22800
            7 -> 26700
            8 -> 29700 // 下午四节课
            9 -> 37800
            10 -> 40800
            11 -> 43800
            12 -> 46800
            else -> 0
        }
        return if (getEnd) {
            offset + 2700
        } else offset
    }

    fun closeRecExpandAnim() {
        val animator = ObjectAnimator.ofFloat(floatViewGroup, "translationX", screenWidth / 2).apply {
            duration = 300L
        }
        animator.start()
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) = Unit

            override fun onAnimationCancel(animation: Animator?) = Unit

            override fun onAnimationStart(animation: Animator?) = Unit

            override fun onAnimationEnd(animation: Animator?) {
                toggleState(false)
            }
        })
    }

    fun toggleState(expand: Boolean) {
        if (expand) {
//            floatViewGroup.backgroundColor = Color.parseColor("#66000000")
            recyclerView.apply {
                visibility = View.VISIBLE
            }
            dragBar.visibility = View.INVISIBLE
            windowManager.updateViewLayout(floatViewGroup, expandViewParams)
        } else {
            floatViewGroup.backgroundColor = Color.parseColor("#00000000")
            isAdded = false
            recyclerView.apply {
                visibility = View.INVISIBLE
            }
            floatViewGroup.visibility = View.INVISIBLE
            floatViewGroup.translationX = 0f
            windowManager.updateViewLayout(floatViewGroup, pannelViewParams)
            floatViewGroup.post {
                floatViewGroup.visibility = View.VISIBLE
                dragBar.visibility = View.VISIBLE
            }
        }
    }
}