package com.twtstudio.service.dishesreviews.canteen

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.canteen.model.CanteenDishInfoViewModel
import com.twtstudio.service.dishesreviews.model.Floor

//import com.twtstudio.service.dishesreviews.model.Floor


/**
 * Created by SGXM on 2018/5/6.
 */
class CanteenFragment : Fragment() {
    var mTag = "一层"
    var numOfType = mutableListOf<Pair<Int, Int>>()//存储对应菜品数量
    private var leftList: MutableList<String> = mutableListOf()
        set(value) {
            leftAdapter.notifyDataSetChanged()
        }
    private var rightList: MutableList<CanteenBean> = mutableListOf()
        set(value) {
            rightAdapter.notifyDataSetChanged()
        }
    private lateinit var leftRec: RecyclerView
    private lateinit var rightRec: RecyclerView
    private lateinit var leftAdapter: LeftAdapter
    private lateinit var rightAdapter: RightAdapter
    //    var mSuspensionHeight = 20
//    var mcurrentPos = 0
    // var rightLayoutManager = LinearLayoutManager(this.context)
    //https://stackoverflow.com/questions/30528206/layoutmanager-is-already-attached-to-a-recyclerview-error
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = LayoutInflater.from(activity).inflate(R.layout.dishes_reviews_item_canteen_fragment, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val leftcallback = fun(n: Int) {
            rightRec.recmove(numOfType[n].second)
        }
        leftRec = view.findViewById(R.id.recycler_left)
        rightRec = view.findViewById(R.id.recycler_right)
        leftAdapter = LeftAdapter(leftList, this.context!!, leftcallback)
        rightAdapter = RightAdapter(rightList, this.context!!)
        leftRec.layoutManager = LinearLayoutManager(this.context)
        rightRec.layoutManager = LinearLayoutManager(this.context)
        leftRec.adapter = leftAdapter
        rightRec.adapter = rightAdapter


        ViewModelProviders.of(activity!!).get(CanteenDishInfoViewModel::class.java).liveData.bindNonNull(this@CanteenFragment) {
            Log.d("woggle", "CanteenFrg" + mTag)
            val data: Floor = if (mTag.equals("一层")) it.firstFloor else it.secondFloor
            val templist: MutableList<CanteenBean> = mutableListOf()
            leftList.clear()
            rightList.clear()
            numOfType.apply {
                clear()
                add(0 to 0)
            }
            data.foodRecommend?.let {
                leftList.add("近期推荐")
                it.forEach {
                    templist.add(CanteenBean("近期推荐", it.food_name))
                }
                numOfType.add(it.size to it.size)
            }
            data.latestFood?.let {
                leftList.add("最近新品")
                it.forEach {
                    templist.add(CanteenBean("最近新品", it.food_name))
                }
                numOfType.add(it.size to it.size + numOfType.last().second)
            }
            data.foodList?.groupBy { it.food_window }?.forEach {
                val item = it
                leftList.add("窗口" + item.key)
                it.value.forEach {
                    templist.add(CanteenBean("窗口" + item.key, it.food_name))
                }
                numOfType.add(it.value.size to numOfType.last().second)
            }
            rightList.addAll(templist)

        }

        rightRec.addItemDecoration(FoodItemDividerDecoration(), 0)
        rightRec.addItemDecoration(FoodItemDecoration(this.context!!, rightList, numOfType).apply { leftAdapter = this@CanteenFragment.leftAdapter }, 1)

    }
}