package com.twtstudio.service.tjwhm.exam.problem

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import com.twt.wepeiyang.commons.ui.blur.BlurPopupWindow
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.twtstudio.service.tjwhm.exam.R
import org.jetbrains.anko.dip
import org.jetbrains.anko.horizontalMargin
import org.jetbrains.anko.layoutInflater

/**
 * Created by tjwhm@TWTStudio at 12:31 AM, 2018/8/16.
 * Happy coding!
 */

class ProblemIndexPopup(val activityInterface: ProblemActivityInterface, mContext: Context, private val startXY: Pair<Float, Float>, private val indexData: List<ProblemIndex>) : BlurPopupWindow(mContext), LifecycleOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    lateinit var view: View

    private lateinit var rvProblemIndex: RecyclerView

    val density = context.resources.displayMetrics.density

    override fun createContentView(parent: ViewGroup): View = parent.context.layoutInflater
            .inflate(R.layout.exam_popup_problem_index, parent, false).apply {
                layoutParams = (layoutParams as FrameLayout.LayoutParams).apply {
                    gravity = Gravity.CENTER
                    horizontalMargin = dip(50)
                }
            }.also { view = it }

    override fun onShow() {
        rvProblemIndex = view.findViewById(R.id.rv_popup_problem_index)
        lifecycleRegistry.markState(Lifecycle.State.STARTED)
        rvProblemIndex.apply {
            layoutManager = GridLayoutManager(context, 5)
            withItems {
                repeat(indexData.size) {
                    problemIndexItem(activityInterface, it, indexData[it])
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
        animSet.apply {
            duration = 200L
            interpolator = AccelerateDecelerateInterpolator()
            play(scaleX).with(scaleY).with(alphaAnim).with(x)
        }
        return animSet
    }

    override fun createDismissAnimator(): Animator {
        val animSet = AnimatorSet()
        val contentAnim = ObjectAnimator.ofFloat(mContentLayout, "alpha", mContentLayout.alpha, 0f)
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f)
        val x = ObjectAnimator.ofFloat(view, "X", 50f * density, startXY.first)
        animSet.apply {
            duration = 200L
            interpolator = AccelerateDecelerateInterpolator()
            play(scaleX).with(scaleY).with(contentAnim).with(x)
        }
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

sealed class ProblemIndex {
    object NONE : ProblemIndex()
    object TRUE : ProblemIndex()
    object WRONG : ProblemIndex()
    sealed class NOW : ProblemIndex() {
        object NONE : NOW()
        object TRUE : NOW()
        object WRONG : NOW()
    }
}
