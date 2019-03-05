package com.twt.service.ecard.extansion

import android.graphics.Path
import android.graphics.PointF

operator fun PointF.component1() = x

operator fun PointF.component2() = y

inline fun Path.reuse(block: Path.() -> Unit) = apply {
    rewind()
    block()
}

fun Path.moveTo(dp: PointF) = moveTo(dp.x, dp.y)
fun Path.lineTo(dp: PointF) = lineTo(dp.x, dp.y)
fun Path.cubicTo(cp1: PointF, cp2: PointF, dp: PointF) = cubicTo(cp1.x, cp1.y, cp2.x, cp2.y, dp.x, dp.y)

fun Path.cubicThrough(points: List<PointF>, cpHRatio: Float = 0.450F, cpVRatio: Float = 0F) =
        cubicThrough(points.asSequence(), cpHRatio, cpVRatio)

fun Path.cubicThrough(points: Sequence<PointF>, cpHRatio: Float = 0.450F, cpVRatio: Float = 0F) =
        points.zipWithNext().forEach { (p1, p2) ->
            val cpHBias = (p2.x - p1.x) * cpHRatio
            val cpVBias = (p2.y - p1.y) * cpVRatio
            val cp1 = PointF(p1.x + cpHBias, p1.y + cpVBias)
            val cp2 = PointF(p2.x - cpHBias, p2.y - cpVBias)
            cubicTo(cp1, cp2, p2)
        }