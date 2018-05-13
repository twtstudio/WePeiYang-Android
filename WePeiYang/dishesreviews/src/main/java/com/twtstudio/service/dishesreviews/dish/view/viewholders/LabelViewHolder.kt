package com.twtstudio.service.dishesreviews.dish.view.viewholders

import android.arch.lifecycle.LifecycleOwner
import android.view.View
import android.widget.TextView
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.base.BaseItemViewHolder
import com.twtstudio.service.dishesreviews.model.FoodMark

class LabelViewHolder(itemView: View, lifecycleOwner: LifecycleOwner) : BaseItemViewHolder(itemView, lifecycleOwner) {
    private val tvLabel = itemView.findViewById<TextView>(R.id.tv_label)
    fun bind(foodMark: FoodMark) {
        when {
            foodMark.attitude != 0 -> tvLabel.text = "服务好"
            foodMark.spicy != 0 -> tvLabel.text = "辣"
        }


    }
}