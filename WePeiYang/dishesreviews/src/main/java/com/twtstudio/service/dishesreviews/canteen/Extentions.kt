package com.twtstudio.service.dishesreviews.canteen

import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout

/**
 * Created by SGXM on 2018/5/6.
 */
val recmove = fun RecyclerView.(n: Int) {
    val layoutManager = this.layoutManager as LinearLayoutManager
    layoutManager.scrollToPositionWithOffset(n, 0)
//    if (layoutManager.findFirstVisibleItemPosition() != n) {
//        val view = this.findViewHolderForLayoutPosition(n).itemView
//        this.scrollBy(0, view.top)
//    }
    //layoutManager.scrollToPositionWithOffset(n, 0)
}

//通过反射修改TabLayout Indicator长度
fun TabLayout.setIndicator(left: Int = 0, right: Int = 0) {
    var clazz = this.javaClass
    val tabStrip = clazz.getDeclaredField("mTabStrip")//获取Strip 返回Field类型
    tabStrip.isAccessible = true
    val linearTab = tabStrip.get(this) as LinearLayout//返回指定对象上由此Field表示的字段的值
    for (i in 0 until linearTab.childCount) {
        val child = linearTab.getChildAt(i)
        child.isClickable = false
        val params = child.layoutParams as LinearLayout.LayoutParams
        params.leftMargin = left
        params.rightMargin = right
        child.requestLayout()
        child.width
    }
}