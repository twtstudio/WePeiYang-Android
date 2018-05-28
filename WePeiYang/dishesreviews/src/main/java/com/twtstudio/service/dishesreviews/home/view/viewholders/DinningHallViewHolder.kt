package com.twtstudio.service.dishesreviews.home.view.viewholders

import android.arch.lifecycle.LifecycleOwner
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.base.BaseItemViewHolder
import com.twtstudio.service.dishesreviews.canteen.CanteenActivity
import com.twtstudio.service.dishesreviews.extensions.DishPreferences

class DinningHallViewHolder(itemView: View, lifecycleOwner: LifecycleOwner) : BaseItemViewHolder(itemView, lifecycleOwner) {
    private val imageView = itemView.findViewById<ImageView>(R.id.iv_dinning_hall)
    private val textView = itemView.findViewById<TextView>(R.id.tv_dinning_hall)
    override fun bind(position: Int) {
        when (position) {
            0 -> {
                if (DishPreferences.isNewCampus)
                    setView("学一食堂", R.drawable.dishes_reviews_mei)
                else
                    setView("学二食堂", R.drawable.dishes_reviews_xue2)
            }
            1 -> {
                if (DishPreferences.isNewCampus)
                    setView("学二食堂", R.drawable.dishes_reviews_lan)
                else
                    setView("学三食堂", R.drawable.dishes_reviews_xue3)
            }
            2 -> {
                if (DishPreferences.isNewCampus)
                    setView("学三食堂", R.drawable.dishes_reviews_tang)
                else
                    setView("学四食堂", R.drawable.dishes_reviews_xue4)
            }
            3 -> {
                if (DishPreferences.isNewCampus)
                    setView("学四食堂", R.drawable.dishes_reviews_zhu)
                else
                    setView("学五食堂", R.drawable.dishes_reviews_xue5)
            }
            4 -> {
                if (DishPreferences.isNewCampus)
                    setView("学五食堂", R.drawable.dishes_reviews_tao)
                else
                    setView("清真食堂", R.drawable.dishes_reviews_qingzhen)
            }
            5 -> {
                setView("学六食堂", R.drawable.dishes_reviews_ju)
            }
            6 -> {
                setView("留园食堂", R.drawable.dishes_reviews_liu)
            }
            7 -> {
                setView("青园食堂", R.drawable.dishes_reviews_qing)
            }
        }
    }

    private fun setView(canteenName: String, imgRes: Int) {
        imageView.apply {
            setImageResource(imgRes)
            setOnClickListener {
                startCanteenActivity(canteenName, imgRes)
            }
        }
        textView.text = canteenName
    }

    private fun startCanteenActivity(canteenName: String, imgRes: Int) {
        val intent = Intent(itemView.context, CanteenActivity::class.java)
        intent.putExtra("CanteenName", canteenName)
        itemView.context.startActivity(intent)
    }
}