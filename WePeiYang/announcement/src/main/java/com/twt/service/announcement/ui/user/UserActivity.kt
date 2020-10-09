package com.twt.service.announcement.ui.user

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.githang.statusbar.StatusBarCompat
import com.twt.service.announcement.R
import com.twt.service.announcement.service.AnnoPreference
import com.twt.service.announcement.service.AnnoService
import com.twt.service.announcement.ui.detail.DetailActivity
import com.twt.service.announcement.ui.main.MyLinearLayoutManager
import com.twt.service.announcement.ui.main.QuestionItem
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

enum class Category(name: String) {
    QUESTION("已点赞的问题"), ANSWER("已点赞的回复"), COMMENT("已点赞的评论")
}

class UserActivity : AppCompatActivity() {
    private val likedRecController by lazy { ItemManager() }
    private lateinit var likedRec: RecyclerView
    private lateinit var stateText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        findViewById<ImageView>(R.id.liked_back).apply {
            setOnClickListener {
                onBackPressed()
            }
        }

        stateText = findViewById(R.id.liked_no_connect_text)
        likedRec = findViewById<RecyclerView>(R.id.liked_rec).apply {
            layoutManager = MyLinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = ItemAdapter(likedRecController)
            itemAnimator = FadeInAnimator()
            itemAnimator?.let {
                it.addDuration = 100
                it.removeDuration = 400
                it.changeDuration = 300
                it.moveDuration = 200
            }
        }

        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
            AnnoPreference.myId?.let { id ->
                AnnoService.getLikedQuestions(user_id = id).awaitAndHandle {
                    stateText.visibility = View.VISIBLE
                    likedRec.visibility = View.INVISIBLE
                    stateText.text = "网络异常"
                }?.let {
                    when (it.ErrorCode) {
                        0 -> {
                            it.data?.let { list ->
                                if (list.isNotEmpty()) {
                                    stateText.visibility = View.INVISIBLE
                                    likedRec.visibility = View.VISIBLE
                                    val itemList = list.map { qd ->
                                        QuestionItem(this@UserActivity, qd) {
                                            val mIntent: Intent = Intent(this@UserActivity, DetailActivity::class.java)
                                                    .putExtra("question", qd)
                                                    .putExtra("likeState", qd)
                                            startActivity(mIntent)
                                        }
                                    }
                                    likedRecController.refreshAll(itemList)
                                } else {
                                    stateText.visibility = View.VISIBLE
                                    likedRec.visibility = View.INVISIBLE
                                    stateText.text = "您还没有点了赞的问题"
                                }
                            }
                        }
                        else -> {
                            stateText.visibility = View.VISIBLE
                            likedRec.visibility = View.INVISIBLE
                            stateText.text = "服务出现异常"
                        }
                    }
                }
            }
        }
    }
}