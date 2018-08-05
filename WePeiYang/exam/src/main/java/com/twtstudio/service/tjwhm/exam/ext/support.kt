package com.twtstudio.service.tjwhm.exam.ext

import android.animation.Animator
import android.content.Context
import android.view.View
import android.widget.Scroller

/**
 * Created by tjwhm@TWTStudio at 11:30 AM,2018/8/5.
 * Happy coding!
 */

object NoneAnimatorListener : Animator.AnimatorListener {
    override fun onAnimationEnd(animation: Animator?) = Unit
    override fun onAnimationCancel(animation: Animator?) = Unit
    override fun onAnimationStart(animation: Animator?) = Unit
    override fun onAnimationRepeat(animation: Animator?) = Unit
}

class GoneAnimatorListener(var view: View?) : Animator.AnimatorListener {
    override fun onAnimationEnd(animation: Animator?) {
        view?.visibility = View.GONE
    }

    override fun onAnimationCancel(animation: Animator?) = Unit
    override fun onAnimationStart(animation: Animator?) = Unit
    override fun onAnimationRepeat(animation: Animator?) = Unit
}

class FixedSpeedScroller(context: Context) : Scroller(context) {
    private val mDuration = 500
    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) =
            super.startScroll(startX, startY, dx, dy, mDuration)

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) =
            super.startScroll(startX, startY, dx, dy, mDuration)
}