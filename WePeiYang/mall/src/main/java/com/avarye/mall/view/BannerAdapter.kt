package com.avarye.mall.view

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.avarye.mall.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.mall_item_header.view.*

class BannerAdapter(private val mContext: Context, private val info: List<Any>) : PagerAdapter() {
    override fun getCount(): Int {
        return 1000000//假无限滑动
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(mContext).inflate(R.layout.mall_item_header, container, false)
        val img = info[position % info.size].toString()
        val viewHolder = ViewHolder(view)

        viewHolder.img.setOnClickListener {
            //TODO:莫得接口
            Glide.with(mContext).load("http://www.weimeicun.com/uploads/allimg/181002/1016-1Q002162I0I3.jpg").into(viewHolder.img)
        }
        return super.instantiateItem(container, position)
    }

    internal class ViewHolder(view: View) {
        var img: ImageView = view.mall_iv_main_header
    }

}