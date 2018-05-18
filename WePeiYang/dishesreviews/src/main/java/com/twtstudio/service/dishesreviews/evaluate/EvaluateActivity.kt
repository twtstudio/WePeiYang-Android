package com.twtstudio.service.dishesreviews.evaluate

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.CheckedTextView
import android.widget.TextView
import com.twtstudio.service.dishesreviews.R
import kotlinx.android.synthetic.main.dishes_reviews_item_evaluate_score.*

class EvaluateActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var tvTitle: TextView
    private lateinit var imgDish: ImageView
    private lateinit var tvDishName: TextView
    private lateinit var tvLocation: TextView
    private lateinit var ratingBar: RatingBar
    private var foodId = 0
    private var singlSelectedCTVs = SingleSelector()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dishes_reviews_activity_evaluate)
        foodId = intent.getIntExtra("FoodId", 0)
        toolbar = findViewById<Toolbar>(R.id.toolbar).apply {
            title = ""
        }
        setSupportActionBar(toolbar)
        setDisplayHomeAsUpEnabled(true)
        tvTitle = findViewById<TextView>(R.id.tv_toolbar_title).apply {
            text = "点评"
        }
        imgDish = findViewById<ImageView>(R.id.img_dish)
        tvDishName = findViewById<TextView>(R.id.tv_dish_name)
        tvLocation = findViewById<TextView>(R.id.tv_location)
        ratingBar = findViewById<RatingBar>(R.id.ratingbar_evaluate)
        singlSelectedCTVs.apply {
            add(findViewById(R.id.tv_time_breakfast))
            add(findViewById(R.id.tv_time_lunch))
            add(findViewById(R.id.tv_time_dinner))
            setListener {}
        }

        tv_tag_1.setOnClickListener {
            (it as CheckedTextView).toggle()
        }
        tv_tag_2.setOnClickListener {
            (it as CheckedTextView).toggle()
        }
        tv_tag_3.setOnClickListener {
            (it as CheckedTextView).toggle()
        }
    }

    private fun setDisplayHomeAsUpEnabled(showHomeAsUp: Boolean) {
        getSupportActionBar()?.apply {
            setDisplayHomeAsUpEnabled(showHomeAsUp)
            setHomeAsUpIndicator(R.drawable.dishes_reviews_ic_action_back)
        }
    }
}