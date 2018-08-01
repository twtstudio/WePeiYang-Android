package com.twtstudio.service.tjwhm.exam.problem

import android.animation.Animator
import android.view.View

fun String.toProblemType(): String {
    return when (this) {
        "0" -> "单选"
        "1" -> "多选"
        "2" -> "判断"
        else -> "其他"
    }
}

fun Int.toProblemType(): String {
    return when (this) {
        0 -> "单选"
        1 -> "多选"
        2 -> "判断"
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

fun List<Int>.toSelectionIndex(): String {
    var result = ""
    repeat(this.size) {
        result += when (this[it]) {
            0 -> "A"
            1 -> "B"
            2 -> "C"
            3 -> "D"
            4 -> "E"
            5 -> "F"
            else -> "W"
        }
    }
    return result
}

fun String.multiSelectionIndexToInt(): List<Int> {
    val result = mutableListOf<Int>()
    for (i in 0 until length) {
        result.add(this[i].selectionIndexToInt())
    }
    return result
}

fun Int.toSelectionIndexForTrueFalse(): String {
    return when (this) {
        0 -> "T"
        1 -> "F"
        else -> "W"
    }
}

fun String.selectionIndexToInt(): Int {
    return when (this) {
        "A", "T" -> 0
        "B", "F" -> 1
        "C" -> 2
        "D" -> 3
        "E" -> 4
        else -> 6
    }
}

fun Char.selectionIndexToInt(): Int {
    return when (this) {
        'A', 'T' -> 0
        'B', 'F' -> 1
        'C' -> 2
        'D' -> 3
        'E' -> 4
        else -> 6
    }
}

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