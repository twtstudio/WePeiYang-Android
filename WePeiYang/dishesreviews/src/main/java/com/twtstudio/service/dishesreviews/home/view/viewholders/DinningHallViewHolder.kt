package com.twtstudio.service.dishesreviews.home.view.viewholders

import android.arch.lifecycle.LifecycleOwner
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.base.BaseItemViewHolder
import com.twtstudio.service.dishesreviews.canteen.CanteenActivity

class DinningHallViewHolder(itemView: View, lifecycleOwner: LifecycleOwner) : BaseItemViewHolder(itemView, lifecycleOwner) {
    private val imageView = itemView.findViewById<ImageView>(R.id.iv_dinning_hall)
    private val textView = itemView.findViewById<TextView>(R.id.tv_dinning_hall)
    override fun bind(position: Int) {
        when (position) {
            0 -> {
                imageView.apply {
                    setImageResource(R.drawable.dishes_reviews_mei)
                    setOnClickListener {
                        val intent = Intent(context, CanteenActivity::class.java)
                        intent.putExtra("CanteenName", "学一食堂")
                        context.startActivity(intent)
                    }
                }
                textView.text = "学一食堂"
            }
            1 -> {
                imageView.apply {
                    setImageResource(R.drawable.dishes_reviews_lan)
                    setOnClickListener {
                        val intent = Intent(context, CanteenActivity::class.java)
                        intent.putExtra("CanteenName", "学二食堂")
                        context.startActivity(intent)
                    }
                }
                textView.text = "学二食堂"
            }
            2 -> {
                imageView.apply {
                    setImageResource(R.drawable.dishes_reviews_tang)
                    setOnClickListener {
                        val intent = Intent(context, CanteenActivity::class.java)
                        intent.putExtra("CanteenName", "学三食堂")
                        context.startActivity(intent)
                    }
                }
                textView.text = "学三食堂"
            }
            3 -> {
                imageView.apply {
                    setImageResource(R.drawable.dishes_reviews_zhu)
                    setOnClickListener {
                        val intent = Intent(context, CanteenActivity::class.java)
                        intent.putExtra("CanteenName", "学四食堂")
                        context.startActivity(intent)
                    }
                }
                textView.text = "学四食堂"
            }
            4 -> {
                imageView.apply {
                    setImageResource(R.drawable.dishes_reviews_tao)
                    setOnClickListener {
                        val intent = Intent(context, CanteenActivity::class.java)
                        intent.putExtra("CanteenName", "学五食堂")
                        context.startActivity(intent)
                    }
                }
                textView.text = "学五食堂"
            }
            5 -> {
                imageView.apply {
                    setImageResource(R.drawable.dishes_reviews_ju)
                    setOnClickListener {
                        val intent = Intent(context, CanteenActivity::class.java)
                        intent.putExtra("CanteenName", "学六食堂")
                        context.startActivity(intent)
                    }
                }
                textView.text = "学六食堂"
            }
            6 -> {
                imageView.apply {
                    setImageResource(R.drawable.dishes_reviews_liu)
                    setOnClickListener {
                        val intent = Intent(context, CanteenActivity::class.java)
                        intent.putExtra("CanteenName", "留园食堂")
                        context.startActivity(intent)
                    }
                }
                textView.text = "留园食堂"
            }
            7 -> {
                imageView.apply {
                    setImageResource(R.drawable.dishes_reviews_qing)
                    setOnClickListener {
                        val intent = Intent(context, CanteenActivity::class.java)
                        intent.putExtra("CanteenName", "青园食堂")
                        context.startActivity(intent)
                    }
                }
                textView.text = "青园食堂"
            }
        }
    }
}