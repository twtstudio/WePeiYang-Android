package com.twt.service.slide.flash

import android.animation.Animator
import android.animation.ObjectAnimator
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
import android.widget.TextView
import com.twt.service.schedule2.model.total.TotalCourseManager
import com.twt.service.slide.R
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import com.twt.wepeiyang.commons.ui.rec.withItems
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.verticalLayout
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.properties.Delegates


class FlashFloatWindow(val context: Context) : LifecycleOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private lateinit var panelViewGlobal : View

    fun showFloatWindow() {
        var foregroundView: View by Delegates.notNull()

        val view = context.frameLayout {
            val viewFrame = this
            foregroundView = viewFrame
            /*foregroundView = */verticalLayout {
            val headerView = LayoutInflater.from(context).inflate(R.layout.flash_header_today, this, false)
            addView(headerView)

            val dateFormat = SimpleDateFormat("yyyy/MM/dd E")
            val date = Date(Calendar.getInstance().timeInMillis)
            val dateString = dateFormat.format(date)

            headerView.apply {
                val headerTodayDetailText = findViewById<TextView>(R.id.tv_flash_today_detail)
                headerTodayDetailText.text = dateString
            }

            val rec = RecyclerView(context).apply {
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                layoutManager = LinearLayoutManager(context)
                TotalCourseManager.getTotalCourseManager().observeForever { classtableProvider ->
                    if (classtableProvider == null) return@observeForever
                    withItems {
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
            addView(rec)

            rec.setOnClickListener {
                val objectAnimator = ObjectAnimator.ofFloat(foregroundView, "translationX", foregroundView.width.toFloat()).apply {
                    duration = 500
                }
                objectAnimator.start()
                objectAnimator.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) = Unit

                    override fun onAnimationCancel(animation: Animator?) = Unit

                    override fun onAnimationStart(animation: Animator?) = Unit

                    override fun onAnimationEnd(animation: Animator?) {
                        val view = this@frameLayout
                        view.callOnClick()
                    }

                })
            }
//            val stupidRecTouchListener = StupidRecTouchListener()
//                rec.setOnTouchListener { v, event ->
//                    Log.d("Rec-Touch", event.toString())
//                    false
//                }
//            rec.setOnTouchListener(stupidRecTouchListener)

            val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
                override fun onDown(e: MotionEvent?): Boolean {
                    Log.d("Rec-Down:", "e: $e")
                    return super.onDown(e)
                }

                override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                    Log.d("Rec-Fling:", "e1: $e1 e2: $e2 vex: $velocityX vey: $velocityY")
                    if (e1 == null || e2 == null) {
                        Log.e("Check NULL:", "e1: $e1 , e2: $e2")
                        return false
                    }
//                        val innerForegroundView = this@frameLayout
                    val objectAnimator = ObjectAnimator.ofFloat(foregroundView, "translationX", foregroundView.width.toFloat()).apply {
                        duration = 500
                    }
                    objectAnimator.start()
                    objectAnimator.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(animation: Animator?) = Unit

                        override fun onAnimationCancel(animation: Animator?) = Unit

                        override fun onAnimationStart(animation: Animator?) = Unit

                        override fun onAnimationEnd(animation: Animator?) {
                            val view = this@frameLayout
                            view.callOnClick()
                        }

                    })
                    return super.onFling(e1, e2, velocityX, velocityY)
                }

                var preE2x = 0.0f;
                override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                    Log.d("Rec-Scroll:", "e1: $e1 e2: $e2 vex: $distanceX vey: $distanceY")
                    if (e1 == null || e2 == null) {
                        Log.e("Check NULL:", "e1: $e1 , e2: $e2")
                        return false
                    }
                    val distance = e2.x - e1.x
//                        val innerForegroundView = this@verticalLayout
                    if (distance > 0 && e2.x - preE2x > 0) {
                        preE2x = e2.x
                        foregroundView.translationX = distance
                    }
                    return super.onScroll(e1, e2, distanceX, distanceY)
                }
            }
            val detector = GestureDetector(context, gestureListener)
//                rec.setOnTouchListener { v, event ->
//                    detector.onTouchEvent(event)
//                }
        }
            backgroundColor = Color.parseColor("#66000000")
        }

        val layoutParams = WindowManager.LayoutParams().apply {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else TYPE_SYSTEM_ALERT
            flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
            format = PixelFormat.RGBA_8888
        }
        lifecycleRegistry.markState(Lifecycle.State.STARTED)

        val panelView = context.frameLayout {
            backgroundColor = Color.TRANSPARENT
            frameLayout {
                backgroundColor = Color.parseColor("#44000000")
            }.lparams(width = 20, height = matchParent) {
                gravity = Gravity.END
            }
        }
        val pannelViewParams = WindowManager.LayoutParams().apply {
            x = 0
            y = 800
            gravity = Gravity.END or Gravity.BOTTOM
            width = 40
            height = 300
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else TYPE_SYSTEM_ALERT
            flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            format = PixelFormat.RGBA_8888
        }

        var isAdded = false
        val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                Log.d("Fling:", "e1: $e1 e2: $e2 vex: $velocityX vey: $velocityY")
                val distenceX = abs(e2.x - e1.x)
                if (distenceX > 100) {
//                    panelView.callOnClick()
                }
                val objectAnimator = ObjectAnimator.ofFloat(foregroundView, "translationX", 0f).apply {
                    duration = (abs(foregroundView.translationX - 0f)).toLong() / 2
                }
                objectAnimator.start()
                return super.onFling(e1, e2, velocityX, velocityY)
            }

            override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
                Log.d("Scroll:", "e1: $e1 e2: $e2 vex: $distanceX vey: $distanceY")
                if (abs(e2.x - e1.x) > 100 && !isAdded) {
                    windowManager.addView(view, layoutParams)
                    isAdded = true
//                    recyclerV.adapter?.notifyDataSetChanged()
                }
                if (isAdded) {
                    foregroundView.translationX = foregroundView.width + e2.x - e1.x  /*- 400*/
                }
                return super.onScroll(e1, e2, distanceX, distanceY)
            }
        }
        val detector = GestureDetector(context, gestureListener)
        panelView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                try {
                    windowManager.addView(view, layoutParams) // 优化不同进入的流程
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val objectAnimator = ObjectAnimator.ofFloat(foregroundView, "translationX", 0f).apply {
                    duration = (abs(foregroundView.translationX - 0f)).toLong() / 2
                    Log.d("Duration:", foregroundView.translationX.toString())
                }
                objectAnimator.start()
            }
            detector.onTouchEvent(event)
        }

        panelViewGlobal = panelView

        windowManager.addView(panelView, pannelViewParams)
        view.setOnClickListener {
            try {
                windowManager.removeView(view)
            } catch (e: Exception) {
                e.printStackTrace()
                // todo: 排查两个异常的来源
            }
            isAdded = false
//            lifecycleRegistry.markState(Lifecycle.State.DESTROYED)
        }
    }

    fun closePop() = windowManager.removeView(panelViewGlobal)


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


}