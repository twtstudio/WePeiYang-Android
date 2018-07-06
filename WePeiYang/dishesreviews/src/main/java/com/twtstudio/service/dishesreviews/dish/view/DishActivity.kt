package com.twtstudio.service.dishesreviews.dish.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.dish.model.DishesFoodProvider
import com.twtstudio.service.dishesreviews.dish.view.adapters.CommentAdapter
import com.twtstudio.service.dishesreviews.dish.view.adapters.LabelAdapter
import com.twtstudio.service.dishesreviews.evaluate.EvaluateActivity
import com.twtstudio.service.dishesreviews.extensions.displayImage
import com.twtstudio.service.dishesreviews.model.Comment
import com.twtstudio.service.dishesreviews.model.FoodMark
import com.twtstudio.service.dishesreviews.share.ShareActivity
import org.jetbrains.anko.image


class DishActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var tvTitle: TextView
    private lateinit var rvLabel: RecyclerView
    private lateinit var rvComment: RecyclerView
    private lateinit var llComment: LinearLayout
    private lateinit var llShare: LinearLayout
    private lateinit var llLove: LinearLayout
    private lateinit var llCollect: LinearLayout
    private lateinit var ivBg: ImageView
    private lateinit var ivCollect: ImageView
    private lateinit var ivLove: ImageView
    private lateinit var tvDishName: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvPrice: TextView
    private lateinit var ratingBar: RatingBar
    private var foodId: Int = 0
    private var labelList: MutableList<String> = mutableListOf()
    private lateinit var labelAdapter: LabelAdapter
    private var isCollect = false
    private var isLove = false
    private var commentList: MutableList<Comment> = mutableListOf()
    private lateinit var commentAdapter: CommentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dishes_reviews_activity_dish)
        ivBg = findViewById(R.id.img_bg)
        tvDishName = findViewById(R.id.tv_dish_name)
        tvLocation = findViewById(R.id.tv_location)
        ratingBar = findViewById(R.id.rb_score)
        tvPrice = findViewById(R.id.tv_price)
        foodId = intent.getIntExtra("FoodId", 0)
        toolbar = findViewById<Toolbar>(R.id.toolbar).apply {
            title = ""
        }
        setSupportActionBar(toolbar)
        getSupportActionBar()?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.dishes_reviews_ic_action_back)
        }
        tvTitle = findViewById(R.id.tv_toolbar_title)
        rvLabel = findViewById<RecyclerView>(R.id.rv_labels).apply {
            layoutManager = object : GridLayoutManager(this@DishActivity, 3) {
                //禁止滑动
                override fun canScrollVertically(): Boolean {
                    return false
                }

                override fun canScrollHorizontally(): Boolean {
                    return false
                }
            }
            labelAdapter = LabelAdapter(labelList, this@DishActivity, this@DishActivity)
            adapter = labelAdapter
        }
        rvComment = findViewById<RecyclerView>(R.id.rv_comment).apply {
            layoutManager = LinearLayoutManager(this@DishActivity, LinearLayoutManager.VERTICAL, false)
            commentAdapter = CommentAdapter(commentList, this@DishActivity, this@DishActivity)
            adapter = commentAdapter
        }
        ivCollect = findViewById(R.id.iv_collect)
        ivLove = findViewById(R.id.iv_love)
        llComment = findViewById<LinearLayout>(R.id.ll_comment).apply {
            setOnClickListener {
                val intent = Intent(this@DishActivity, EvaluateActivity::class.java)
                intent.putExtra("FoodId", foodId)
                startActivity(intent)
            }
        }
        llShare = findViewById<LinearLayout>(R.id.ll_share).apply {
            setOnClickListener {
                val intent = Intent(this@DishActivity, ShareActivity::class.java)
                intent.putExtra("FoodId", foodId)
                startActivity(intent)
            }
        }
        llCollect = findViewById(R.id.ll_collect)
        llCollect.apply {
            setOnClickListener {
                //                DishesFoodProvider.collectFood(foodId,,this@DishActivity)
                DishesFoodProvider.dishCollectLiveData.bindNonNull(this@DishActivity) {
                    isCollect = !isCollect
                    this@DishActivity.setCollectStatus()
                }

            }
        }
        llLove = findViewById(R.id.ll_love)
        llLove.apply {
            setOnClickListener {
                isLove = !isLove
                this@DishActivity.setLoveStatus()
            }
        }



    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    //TODO 标签逻辑有问题
    private fun setTag(foodMark: FoodMark) {
        labelList.clear()
        if (foodMark.spicy > 0) labelList.add("辣")
        if (foodMark.attitude > 0) labelList.add("服务好")
        if (foodMark.fine > 0) labelList.add("清淡")
        if (foodMark.mobilePayment > 0) labelList.add("移动支付")
        if (foodMark.schoolCard > 0) labelList.add("校园卡支付")
        if (foodMark.cash > 0) labelList.add("现金支付")
        if (foodMark.sweet > 0) labelList.add("甜")
        if (foodMark.speed > 0) labelList.add("上菜速度快")
        if (foodMark.salt > 0) labelList.add("咸")
        if (foodMark.enough > 0) labelList.add("分量足")
    }

    //test
    private fun testData() {
        for (i in 1..10) {
            commentList.add(Comment(commenter_name = "John", comment_is_anonymous = 1))
            if (i < 3)
                labelList.add("服务好")
        }
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    private fun setCollectStatus() {
        if (!isCollect)
            ivCollect.image = getDrawable(R.drawable.dishes_reviews_action_collect)
        else ivCollect.image = getDrawable(R.drawable.dishes_reviews_action_collect_selected)
    }

    private fun setLoveStatus() {
        if (!isLove)
            ivLove.image = getDrawable(R.drawable.dishes_reviews_action_love)
        else ivLove.image = getDrawable(R.drawable.dishes_reviews_action_love_selected)
    }

    private fun refresh() {
        DishesFoodProvider.getDishesFood(foodId, this).bindNonNull(this) {
            tvTitle.text = it.foodInfo.food_name
            tvDishName.text = it.foodInfo.food_name
            tvLocation.text = it.foodInfo.canteen_name
            tvPrice.text = "￥" + it.foodInfo.food_price
            ivBg.displayImage(this, it.foodInfo.food_picture_address, ImageView.ScaleType.CENTER_CROP)
            setTag(it.foodMark)
            commentList.clear()
            commentList.addAll(it.comment)
            commentAdapter.notifyDataSetChanged()
            labelAdapter.notifyDataSetChanged()
            ratingBar.rating = it.foodInfo.food_score.toFloat()
            isCollect = it.isCollectedFood
            isLove = it.isPraisedFood
            setCollectStatus()
            setLoveStatus()
        }
    }
}