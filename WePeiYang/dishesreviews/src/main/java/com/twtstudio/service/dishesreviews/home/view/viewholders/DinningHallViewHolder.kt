package com.twtstudio.service.dishesreviews.home.view.viewholders

import android.arch.lifecycle.LifecycleOwner
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.base.BaseItemViewHolder

class DinningHallViewHolder(itemView: View, lifecycleOwner: LifecycleOwner) : BaseItemViewHolder(itemView, lifecycleOwner) {
    private val imageView = itemView.findViewById<ImageView>(R.id.iv_dinning_hall)
    private val textView = itemView.findViewById<TextView>(R.id.tv_dinning_hall)
    override fun bind(position: Int) {
        when (position) {
            0 -> {
                textView.text = "学一食堂"
                imageView.setImageResource(R.drawable.dishes_reviews_mei)
            }
            1 -> {
                imageView.setImageResource(R.drawable.dishes_reviews_lan)
                textView.text = "学二食堂"
            }
            2 -> {
                imageView.setImageResource(R.drawable.dishes_reviews_tang)
                textView.text = "学三食堂"
            }
            3 -> {
                imageView.setImageResource(R.drawable.dishes_reviews_zhu)
                textView.text = "学四食堂"
            }
            4 -> {
                imageView.setImageResource(R.drawable.dishes_reviews_tao)
                textView.text = "学五食堂"
            }
            5 -> {
                imageView.setImageResource(R.drawable.dishes_reviews_ju)
                textView.text = "学六食堂"
            }
            6 -> {
                imageView.setImageResource(R.drawable.dishes_reviews_liu)
                textView.text = "留园食堂"
            }
            7 -> {
                imageView.setImageResource(R.drawable.dishes_reviews_qing)
                textView.text = "青园食堂"
            }
        }
    }
}