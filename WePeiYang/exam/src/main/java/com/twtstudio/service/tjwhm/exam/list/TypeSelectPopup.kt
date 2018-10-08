package com.twtstudio.service.tjwhm.exam.list

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.ui.blur.BlurPopupWindow
import com.twtstudio.service.tjwhm.exam.R
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.dip
import org.jetbrains.anko.horizontalMargin
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.verticalMargin

/**
 * Created by tjwhm@TWTStudio at 7:54 PM, 2018/10/8.
 * Happy coding!
 */

class TypeSelectPopup(private val activity: ListActivity, private val startXY: Pair<Float, Float>, private val lessonID: Int) : BlurPopupWindow(activity), LifecycleOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    lateinit var view: View

    lateinit var tvSingleNum: TextView
    lateinit var tvSingleEnter: TextView
    lateinit var tvMultiNum: TextView
    lateinit var tvMultiEnter: TextView
    lateinit var tvTfNum: TextView
    lateinit var tvTfEnter: TextView

    val density = activity.resources.displayMetrics.density

    override fun createContentView(parent: ViewGroup): View = parent.context.layoutInflater
            .inflate(R.layout.exam_popup_type_select, parent, false).apply {
                layoutParams = (layoutParams as FrameLayout.LayoutParams).apply {
                    gravity = Gravity.CENTER
                    horizontalMargin = dip(50)
//                    verticalMargin = dip(130)
                }
            }.also {
                view = it
                tvSingleNum = view.findViewById(R.id.tv_type_select_single_num)
                tvSingleEnter = view.findViewById(R.id.tv_type_select_single_enter)
                tvMultiNum = view.findViewById(R.id.tv_type_select_multi_num)
                tvMultiEnter = view.findViewById(R.id.tv_type_select_multi_enter)
                tvTfNum = view.findViewById(R.id.tv_type_select_tf_num)
                tvTfEnter = view.findViewById(R.id.tv_type_select_tf_enter)
            }

    override fun onShow() {
        lifecycleRegistry.markState(Lifecycle.State.STARTED)

        getLessonInfo(lessonID.toString()) {
            when (it) {
                is RefreshState.Failure -> Toasty.error(activity, "网络错误").show()
                is RefreshState.Success -> {
                    it.message.data!!.apply {
                        tvSingleNum.text = "$single_done_count/$single_num"
                        tvMultiNum.text = "$multi_done_count/$multi_num"
                        tvTfNum.text = "$decide_done_count/$decide_num"
                    }
                }
            }
        }
    }

    override fun onDismiss() {
        super.onDismiss()
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED)
    }

    override fun createShowAnimator(): Animator {
        val alphaAnim = super.createShowAnimator()
        val animSet = AnimatorSet()
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f)
        val x = ObjectAnimator.ofFloat(view, "X", startXY.first, 50f * density)
        animSet.duration = 200L
        animSet.interpolator = AccelerateDecelerateInterpolator()
        animSet.play(scaleX).with(scaleY).with(alphaAnim).with(x)
        return animSet
    }

    override fun createDismissAnimator(): Animator {
        val animSet = AnimatorSet()
        val contentAnim = ObjectAnimator.ofFloat(mContentLayout, "alpha", mContentLayout.alpha, 0f)
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f)
        val x = ObjectAnimator.ofFloat(view, "X", 50f * density, startXY.first)
        animSet.duration = 200L
        animSet.interpolator = AccelerateDecelerateInterpolator()
        animSet.play(scaleX).with(scaleY).with(contentAnim).with(x)
        return animSet
    }

    override fun getAnimationDuration(): Long = 200L

    override fun isDismissOnTouchBackground(): Boolean = true

    init {
        setOnClickListener {
            dismiss()
        }
    }

}
