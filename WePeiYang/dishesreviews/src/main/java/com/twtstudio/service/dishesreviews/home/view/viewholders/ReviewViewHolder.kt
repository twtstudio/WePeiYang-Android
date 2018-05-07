package com.twtstudio.service.dishesreviews.home.view.viewholders

import android.arch.lifecycle.LifecycleOwner
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.base.BaseItemViewHolder

class ReviewViewHolder(itemView: View, lifecycleOwner: LifecycleOwner) : BaseItemViewHolder(itemView, lifecycleOwner) {
    private val ivDish = itemView.findViewById<ImageView>(R.id.iv_dish)
    private val tvDishName = itemView.findViewById<TextView>(R.id.tv_dish_name)
    private val tvUserName = itemView.findViewById<TextView>(R.id.tv_user_name)
    private val tvDate = itemView.findViewById<TextView>(R.id.tv_date)
    private val tvComment = itemView.findViewById<TextView>(R.id.tv_comment)

}