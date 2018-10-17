package com.twtstudio.service.tjwhm.exam.commons

import android.content.Context
import android.content.Intent
import com.twtstudio.service.tjwhm.exam.problem.ProblemActivity

/**
 * Created by tjwhm@TWTStudio at 11:29 AM,2018/8/5.
 * Happy coding!
 */

fun String.toLessonType(): String {
    return when (this) {
        "3" -> "网课"
        "2" -> "党课"
        "1" -> "形势与政策"
        else -> "其他"
    }
}

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

fun String.multiSelectionIndexToInt(): MutableList<Int> {
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

fun Int.toMode(): String {
    return when (this) {
        0 -> "顺序练习"
        1 -> "模拟测试"
        else -> "其他"
    }
}

// 比较两个 List<Int> 其中含有的元素是否相同
fun List<Int>.isRightWith(answer: List<Int>): Boolean {
    for (i in answer)
        if (i !in this) return false
    if (size == answer.size) return true
    return false
}

fun Context?.startProblemActivity(mode: Int, lessonID: Int, quesType: Int?, index: Int?) {
    Intent(this, ProblemActivity::class.java).apply {
        putExtra(ProblemActivity.LESSON_ID_KEY, lessonID)
        putExtra(ProblemActivity.MODE_KEY, mode)
        putExtra(ProblemActivity.PROBLEM_TYPE_KEY, quesType)
        putExtra(ProblemActivity.CONTINUE_INDEX_KEY, index)
        this@startProblemActivity?.startActivity(this)
    }
}
