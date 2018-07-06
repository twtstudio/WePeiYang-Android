package com.twtstudio.service.dishesreviews.evaluate

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.*
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.dish.model.DishesFoodProvider
import com.twtstudio.service.dishesreviews.evaluate.model.EvaluateProvider
import com.twtstudio.service.dishesreviews.extensions.displayImage
import kotlinx.android.synthetic.main.dishes_reviews_activity_evaluate.*
import kotlinx.android.synthetic.main.dishes_reviews_item_evaluate_comment.*
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
    private val postMap = mutableMapOf<String, Any>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dishes_reviews_activity_evaluate)
        foodId = intent.getIntExtra("FoodId", 0)
        postMap.put("foodId", foodId.toString())
        toolbar = findViewById<Toolbar>(R.id.toolbar).apply {
            title = ""
        }
        setSupportActionBar(toolbar)
        setDisplayHomeAsUpEnabled(true)
        tvTitle = findViewById<TextView>(R.id.tv_toolbar_title).apply {
            text = "点评"
        }
        imgDish = findViewById(R.id.img_dish)
        tvDishName = findViewById(R.id.tv_dish_name)
        tvLocation = findViewById(R.id.tv_location)
        ratingBar = findViewById(R.id.ratingbar_evaluate)
        DishesFoodProvider.getDishesFood(foodId, this).bindNonNull(this) {
            imgDish.displayImage(this, it.foodInfo.food_picture_address, ImageView.ScaleType.CENTER_CROP)
            tvDishName.text = it.foodInfo.food_name
            tvLocation.text = it.foodInfo.canteen_name + it.foodInfo.food_floor + "层" + it.foodInfo.food_window + "窗口"
        }
        ratingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->
            postMap.put("foodScore", fl.toInt().toString())
        }
        singlSelectedCTVs.apply {
            add(findViewById(R.id.tv_time_breakfast))
            add(findViewById(R.id.tv_time_lunch))
            add(findViewById(R.id.tv_time_dinner))
            setListener {}
        }

        tv_tag_1.setOnClickListener {
            (it as CheckedTextView).toggle()
            postMap.put("attitude", "1")
        }
        tv_tag_2.setOnClickListener {
            (it as CheckedTextView).toggle()
        }
        tv_tag_3.setOnClickListener {
            (it as CheckedTextView).toggle()
        }
        button_submit.setOnClickListener {
            if (cb_anonymous.isChecked)
                postMap.put("commentIsAnonymous", "1")
            else
                postMap.put("commentIsAnonymous", "0")
            postMap.put("commentContent", edt_comment.text.toString())
            EvaluateProvider.postEvaluate(postMap, this@EvaluateActivity).bindNonNull(this@EvaluateActivity) {
                Toast.makeText(this@EvaluateActivity, "评论成功", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun setDisplayHomeAsUpEnabled(showHomeAsUp: Boolean) {
        getSupportActionBar()?.apply {
            setDisplayHomeAsUpEnabled(showHomeAsUp)
            setHomeAsUpIndicator(R.drawable.dishes_reviews_ic_action_back)
        }
    }

}