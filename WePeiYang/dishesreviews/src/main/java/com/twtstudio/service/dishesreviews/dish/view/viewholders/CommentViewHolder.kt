package com.twtstudio.service.dishesreviews.dish.view.viewholders

import android.arch.lifecycle.LifecycleOwner
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.base.BaseItemViewHolder
import com.twtstudio.service.dishesreviews.extensions.displayImage
import com.twtstudio.service.dishesreviews.model.Comment

class CommentViewHolder(itemView: View, lifecycleOwner: LifecycleOwner) : BaseItemViewHolder(itemView, lifecycleOwner) {
    private val tvUserName = itemView.findViewById<TextView>(R.id.tv_user_name)
    private val tvDate = itemView.findViewById<TextView>(R.id.tv_date)
    private val iv1 = itemView.findViewById<ImageView>(R.id.iv_1)
    private val iv2 = itemView.findViewById<ImageView>(R.id.iv_2)
    private val iv3 = itemView.findViewById<ImageView>(R.id.iv_3)
    private val iv4 = itemView.findViewById<ImageView>(R.id.iv_4)
    fun bind(comment: Comment) {
        tvUserName.text = comment.commenter_name
        tvDate.text = comment.updated_at
        iv1.displayImage(itemView.context, comment.picture_address1)
        iv2.displayImage(itemView.context, comment.picture_address2)
        iv3.displayImage(itemView.context, comment.picture_address3)
        iv4.displayImage(itemView.context, comment.picture_address4)
    }
}