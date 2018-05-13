package com.twtstudio.service.dishesreviews.dish.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.dish.view.adapters.CommentAdapter
import com.twtstudio.service.dishesreviews.dish.view.adapters.LabelAdapter
import com.twtstudio.service.dishesreviews.evaluate.EvaluateActivity
import com.twtstudio.service.dishesreviews.model.Comment
import com.twtstudio.service.dishesreviews.model.FoodMark
import com.twtstudio.service.dishesreviews.share.ShareActivity



class DishActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var tvTitle: TextView
    private lateinit var rvLabel: RecyclerView
    private lateinit var rvComment: RecyclerView
    private lateinit var llComment: LinearLayout
    private lateinit var llShare: LinearLayout
    private var labelList: MutableList<FoodMark> = mutableListOf()
        set(value) {
            labelAdapter.notifyDataSetChanged()
        }
    private lateinit var labelAdapter: LabelAdapter
    private var commentList: MutableList<Comment> = mutableListOf()
        set(value) {
            labelAdapter.notifyDataSetChanged()
        }
    private lateinit var commentAdapter: CommentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dishes_reviews_activity_dish)
        toolbar = findViewById<Toolbar>(R.id.toolbar).apply {
            title = ""
        }
        setSupportActionBar(toolbar)
        getSupportActionBar()?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.dishes_reviews_ic_action_back)
        }
        tvTitle = findViewById<TextView>(R.id.tv_toolbar_title).apply {

        }
        rvLabel = findViewById<RecyclerView>(R.id.rv_labels).apply {
            layoutManager = object : GridLayoutManager(this@DishActivity, 2) {
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
        llComment = findViewById<LinearLayout>(R.id.ll_comment).apply {
            setOnClickListener {
                val intent = Intent(this@DishActivity, EvaluateActivity::class.java)
                startActivity(intent)
            }
        }
        llShare = findViewById<LinearLayout>(R.id.ll_share).apply {
            setOnClickListener {
                val intent = Intent(this@DishActivity, ShareActivity::class.java)
                startActivity(intent)
            }
        }
        testData()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    //test
    private fun testData() {
        for (i in 1..10) {
            commentList.add(Comment(commenter_name = "John", comment_is_anonymous = 1))
            if (i < 3)
                labelList.add(FoodMark(spicy = (i + 1) % 2, attitude = i % 2))
        }
    }
}