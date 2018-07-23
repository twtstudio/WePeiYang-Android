package com.twtstudio.service.tjwhm.exam.problem

import android.animation.Animator

fun String.toProblemType(): String {
    return when (this) {
        "0" -> "单选"
        "1" -> "多选"
        "2" -> "判断"
        else -> "其他"
    }
}

fun Int.toSelectionIndex(): String {
    return when (this) {
        0 -> "A"
        1 -> "B"
        2 -> "C"
        3 -> "D"
        4 -> "E"
        5 -> "F"
        else -> "W"
    }
}

fun Int.toSelectionIndexForTrueFalse(): String {
    return when (this) {
        0 -> "T"
        2 -> "F"
        else -> "W"
    }
}

object NoneAnimatorListener : Animator.AnimatorListener {
    override fun onAnimationEnd(animation: Animator?) = Unit
    override fun onAnimationCancel(animation: Animator?) = Unit
    override fun onAnimationStart(animation: Animator?) = Unit
    override fun onAnimationRepeat(animation: Animator?) = Unit
}
