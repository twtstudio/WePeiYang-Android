package com.twtstudio.service.dishesreviews.canteen

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twtstudio.service.dishesreviews.R

/**
 * Created by SGXM on 2018/5/6.
 */
class CanteenFragment : Fragment() {
    var leftList: MutableList<String> = mutableListOf()
        set(value) {
            leftAdapter.notifyDataSetChanged()
        }
    var rightList: MutableList<CanteenBean> = mutableListOf()
        set(value) {
            rightAdapter.notifyDataSetChanged()
        }
    lateinit var leftRec: RecyclerView
    lateinit var rightRec: RecyclerView
    lateinit var leftAdapter: LeftAdapter
    lateinit var rightAdapter: RightAdapter
    var mSuspensionHeight = 20
    var mcurrentPos = 0
    // var rightLayoutManager = LinearLayoutManager(this.context)
    //https://stackoverflow.com/questions/30528206/layoutmanager-is-already-attached-to-a-recyclerview-error
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = LayoutInflater.from(activity).inflate(R.layout.dishes_reviews_item_canteen_fragment, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val leftcallback = fun(n: Int) {
            rightRec.recmove(n * 5)
        }
        leftRec = view.findViewById(R.id.recycler_left)
        rightRec = view.findViewById(R.id.recycler_right)
        leftAdapter = LeftAdapter(leftList, this.context!!, leftcallback)
        rightAdapter = RightAdapter(rightList, this.context!!)
        leftRec.layoutManager = LinearLayoutManager(this.context)
        rightRec.layoutManager = LinearLayoutManager(this.context)
        leftRec.adapter = leftAdapter
        rightRec.adapter = rightAdapter


        ////////////////////假数据////////////////
        leftList.add("窗口1")
        leftList.add("窗口2")
        leftList.add("窗口3")
        leftList.add("窗口4")
        var templist: MutableList<CanteenBean> = mutableListOf()
        for (i in 0..20) {
            val methon = listOf<String>("红烧", "清蒸", "烤", "炖")
            templist.add(CanteenBean("窗口" + ((i / 5) + 1), methon[(i / 5).coerceAtMost(3)] + "紫薯"))
        }
        templist.removeAt(20)
        rightList.addAll(templist)
        rightRec.addItemDecoration(FoodItemDividerDecoration(), 0)
        rightRec.addItemDecoration(FoodItemDecoration(this.context!!, rightList).apply { leftAdapter = this@CanteenFragment.leftAdapter }, 1)

    }
}