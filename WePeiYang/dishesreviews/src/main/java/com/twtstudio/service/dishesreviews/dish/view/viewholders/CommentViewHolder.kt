package com.twtstudio.service.dishesreviews.dish.view.viewholders

import android.arch.lifecycle.LifecycleOwner
import android.view.View
import android.widget.TextView
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.base.BaseItemViewHolder
import com.twtstudio.service.dishesreviews.model.Comment

class CommentViewHolder(itemView: View, lifecycleOwner: LifecycleOwner) : BaseItemViewHolder(itemView, lifecycleOwner) {
    private val tvUserName = itemView.findViewById<TextView>(R.id.tv_user_name)
    fun bind(comment: Comment) {
        tvUserName.text = comment.commenter_name.toString()
    }
}