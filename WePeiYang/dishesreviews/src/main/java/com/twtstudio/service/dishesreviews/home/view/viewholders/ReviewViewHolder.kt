package com.twtstudio.service.dishesreviews.home.view.viewholders

import android.arch.lifecycle.LifecycleOwner
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.base.BaseItemViewHolder
import com.twtstudio.service.dishesreviews.dish.view.DishActivity
import com.twtstudio.service.dishesreviews.model.Comment

class ReviewViewHolder(itemView: View, lifecycleOwner: LifecycleOwner) : BaseItemViewHolder(itemView, lifecycleOwner) {
    private val ivDish = itemView.findViewById<ImageView>(R.id.iv_dish)
    private val tvDishName = itemView.findViewById<TextView>(R.id.tv_dish_name)
    private val tvUserName = itemView.findViewById<TextView>(R.id.tv_user_name)
    private val tvDate = itemView.findViewById<TextView>(R.id.tv_date)
    private val tvComment = itemView.findViewById<TextView>(R.id.tv_comment)

    fun bind(comment: Comment) {
        if (comment.comment_is_anonymous == 0)
            tvUserName.text = "匿名"
        else tvUserName.text = comment.commenter_name.toString()

        ivDish.setOnClickListener {
            startDishActivity(comment.food_id) //test
        }
    }

    private fun startDishActivity(foodId: Int) {
        val intent = Intent(itemView.context, DishActivity::class.java)
        intent.putExtra("FoodId", foodId)
        itemView.context.startActivity(intent)
    }
}