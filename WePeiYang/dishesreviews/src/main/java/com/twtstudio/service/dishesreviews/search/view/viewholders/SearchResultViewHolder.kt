package com.twtstudio.service.dishesreviews.search.view.viewholders

import android.arch.lifecycle.LifecycleOwner
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.base.BaseItemViewHolder
import com.twtstudio.service.dishesreviews.dish.view.DishActivity
import com.twtstudio.service.dishesreviews.extensions.displayImage
import com.twtstudio.service.dishesreviews.model.Food

class SearchResultViewHolder(itemView: View, lifecycleOwner: LifecycleOwner) : BaseItemViewHolder(itemView, lifecycleOwner) {
    private val ivItemIcon = itemView.findViewById<ImageView>(R.id.item_icon)
    private val tvItemName = itemView.findViewById<TextView>(R.id.item_name)
    private val tvItemLocation = itemView.findViewById<TextView>(R.id.item_location)
    private val tvItemTime = itemView.findViewById<TextView>(R.id.item_time)
    private val tvItemTag = itemView.findViewById<TextView>(R.id.item_tag)
    private val ratingBar = itemView.findViewById<RatingBar>(R.id.ratingbar)
    private val cvComment = itemView.findViewById<CardView>(R.id.cv_comment)
    private val clResult = itemView.findViewById<ConstraintLayout>(R.id.cl_search_result)
    fun bind(food: Food) {
        ivItemIcon.displayImage(itemView.context, food.food_picture_address, ImageView.ScaleType.CENTER_CROP)
        tvItemName.text = food.food_name
        tvItemLocation.text = food.canteen_name + food.food_floor + "层" + food.food_window + "窗口"
        tvItemTime.text = food.food_time
        ratingBar.rating = food.food_score.toFloat()
        clResult.setOnClickListener {
            startDishActivity(food.food_id)
        }
    }

    private fun startDishActivity(foodId: Int) {
        val intent = Intent(itemView.context, DishActivity::class.java)
        intent.putExtra("FoodId", foodId)
        itemView.context.startActivity(intent)
    }
}