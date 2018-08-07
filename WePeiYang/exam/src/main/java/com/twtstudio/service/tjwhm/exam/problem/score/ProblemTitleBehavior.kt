package com.twtstudio.service.tjwhm.exam.problem.score

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

/**
 * Created by tjwhm@TWTStudio at 1:42 AM,2018/8/8.
 * Happy coding!
 */
class ProblemTitleBehavior : CoordinatorLayout.Behavior<TextView> {
    constructor() : super()
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun layoutDependsOn(parent: CoordinatorLayout?, child: TextView?, dependency: View?): Boolean {
        return dependency is RecyclerView
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: TextView?, dependency: View?): Boolean {

        return true
    }
}