package com.twtstudio.service.dishesreviews.home.view.viewholders

import android.arch.lifecycle.LifecycleOwner
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.base.BaseItemViewHolder
import com.twtstudio.service.dishesreviews.dish.view.DishActivity
import com.twtstudio.service.dishesreviews.extensions.displayImage
import com.twtstudio.service.dishesreviews.model.GoodComment

class ReviewViewHolder(itemView: View, lifecycleOwner: LifecycleOwner) : BaseItemViewHolder(itemView, lifecycleOwner) {
    private val ivDish = itemView.findViewById<ImageView>(R.id.iv_dish)
    private val tvDishName = itemView.findViewById<TextView>(R.id.tv_dish_name)
    private val tvUserName = itemView.findViewById<TextView>(R.id.tv_user_name)
    private val tvDate = itemView.findViewById<TextView>(R.id.tv_date)
    private val tvComment = itemView.findViewById<TextView>(R.id.tv_comment)
    private val iv1 = itemView.findViewById<ImageView>(R.id.iv_1)
    private val iv2 = itemView.findViewById<ImageView>(R.id.iv_2)
    private val iv3 = itemView.findViewById<ImageView>(R.id.iv_3)
    private val iv4 = itemView.findViewById<ImageView>(R.id.iv_4)
    private val ratingBar = itemView.findViewById<RatingBar>(R.id.ratingbar_evaluate)
    fun bind(comment: GoodComment) {
        if (comment.comment_is_anonymous == 0)
            tvUserName.text = "匿名"
        else tvUserName.text = comment.commenter_name
        tvDishName.text = comment.food_name
        tvDate.text = comment.updated_at
        tvComment.text = comment.comment_content
        iv1.displayImage(itemView.context, comment.picture_address1)
        iv2.displayImage(itemView.context, comment.picture_address2)
        iv3.displayImage(itemView.context, comment.picture_address3)
        iv4.displayImage(itemView.context, comment.picture_address4)
        ratingBar.numStars = comment.food_score
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