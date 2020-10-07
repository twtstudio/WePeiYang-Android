package com.twt.service.announcement.ui.user

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.githang.statusbar.StatusBarCompat
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionMenu
import com.twt.service.announcement.R
import com.twt.service.announcement.service.AnnoPreference
import com.twt.service.announcement.service.AnnoService
import com.twt.service.announcement.ui.detail.DetailActivity
import com.twt.service.announcement.ui.main.ButtonItem
import com.twt.service.announcement.ui.main.MyLinearLayoutManager
import com.twt.service.announcement.ui.main.QuestionItem
import com.twt.service.announcement.ui.main.ViewType
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserActivity : AppCompatActivity() {
    private val recController by lazy { ItemManager() }
    private lateinit var questionRec: RecyclerView
    private lateinit var stateText: TextView
    private lateinit var userTitle: TextView
    private lateinit var userPage: ConstraintLayout
    private lateinit var likeNum: TextView
    private lateinit var mineNum: TextView
    private lateinit var userBar: AppBarLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        findViewById<ImageView>(R.id.liked_back).apply {
            setOnClickListener {
                onBackPressed()
            }
        }
        likeNum = findViewById(R.id.favor_state)
        mineNum = findViewById(R.id.mine_state)
        userTitle = findViewById(R.id.user_title)
        stateText = findViewById(R.id.liked_no_connect_text)
        swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.user_refresh).apply {
            setOnRefreshListener {
                when {
                    userPage.visibility == View.VISIBLE -> {
                        loadUserInform()
                    }
                    userTitle.text == "点赞的问题" -> {
                        loadLikedQuestions()
                    }
                    userTitle.text == "提出的问题" -> {
                        loadMyQuestions()
                    }
                }
            }
        }

        questionRec = findViewById<RecyclerView>(R.id.liked_rec).apply {
            layoutManager = MyLinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = ItemAdapter(recController)
            itemAnimator = FadeInAnimator()
            itemAnimator?.let {
                it.addDuration = 300
                it.removeDuration = 800
                it.moveDuration = 300
                it.changeDuration = 500
            }
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    swipeRefreshLayout.isEnabled = !recyclerView?.canScrollVertically(-1)!!
                }
            })

        }

        userBar = findViewById(R.id.user_bar)

        userPage = findViewById(R.id.user_page)

        findViewById<CardView>(R.id.user_favor_item).apply {
            setOnClickListener {
                userPage.visibility = View.INVISIBLE
                questionRec.visibility = View.VISIBLE
                loadLikedQuestions()
            }
        }

        findViewById<TextView>(R.id.user_id).apply {
            text = if (CommonPreferences.studentid == "") {
                "神秘数字"
            } else {
                CommonPreferences.studentid
            }
        }

        findViewById<TextView>(R.id.user_name).apply {
            text = if (CommonPreferences.realName == "") {
                "神秘嘉宾"
            } else {
                CommonPreferences.realName
            }
        }

        findViewById<CardView>(R.id.user_mine_item).apply {
            setOnClickListener {
                userPage.visibility = View.INVISIBLE
                questionRec.visibility = View.VISIBLE
                loadMyQuestions()
            }
        }

        swipeRefreshLayout.isRefreshing = true
        loadUserInform()
    }

    override fun onBackPressed() {
        recController.clear()
        swipeRefreshLayout.isRefreshing = false
        if (userPage.visibility == View.INVISIBLE) {
            userTitle.text = "个人主页"
            userBar.setExpanded(true)
            stateText.visibility = View.INVISIBLE
            questionRec.visibility = View.INVISIBLE
            userPage.visibility = View.VISIBLE
        } else {
            super.onBackPressed()
        }

    }

    private fun loadUserInform() {
        AnnoPreference.myId?.let { id ->
            GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                AnnoService.getUserDataById(user_id = id).awaitAndHandle {
                    likeNum.text = "?"
                    mineNum.text = "?"
                    swipeRefreshLayout.isRefreshing = false
                }?.data?.let {
                    likeNum.text = it.my_liked_question_num.toString()
                    mineNum.text = it.my_question_num.toString()
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    private fun loadLikedQuestions() {
        swipeRefreshLayout.isRefreshing = true
        AnnoPreference.myId?.let { id ->
            GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                AnnoService.getLikedQuestions(user_id = id).awaitAndHandle {
                    swipeRefreshLayout.isRefreshing = false
                    stateText.visibility = View.VISIBLE
                    questionRec.visibility = View.INVISIBLE
                    stateText.text = "网络异常"
                }?.let {
                    swipeRefreshLayout.isRefreshing = false
                    userBar.setExpanded(true)
                    questionRec.scrollToPosition(0)
                    when (it.ErrorCode) {
                        0 -> {
                            it.data?.let { list ->
                                if (list.isNotEmpty()) {
                                    stateText.visibility = View.INVISIBLE
                                    questionRec.visibility = View.VISIBLE
                                    val itemList = list.map { qd ->
                                        QuestionItem(this@UserActivity, qd,
                                                onClick = {
                                                    val mIntent: Intent = Intent(this@UserActivity, DetailActivity::class.java)
                                                            .putExtra("question", qd)
                                                            .putExtra("likeState", qd)
                                                    startActivity(mIntent)
                                                }
                                        )
                                    }
                                    recController.refreshAll(itemList)
                                    recController.add(ButtonItem(ViewType.BOTTOM_TEXT))
                                    userTitle.text = "点赞的问题"
                                } else {
                                    stateText.visibility = View.VISIBLE
                                    questionRec.visibility = View.INVISIBLE
                                    stateText.text = "您还没有点了赞的问题"
                                }
                            }
                        }
                        else -> {
                            stateText.visibility = View.VISIBLE
                            questionRec.visibility = View.INVISIBLE
                            stateText.text = "服务出现异常"
                        }
                    }
                }
            }
        }
    }

    private fun loadMyQuestions() {
        swipeRefreshLayout.isRefreshing = true
        AnnoPreference.myId?.let { id ->
            GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                AnnoService.getMyQuestion(user_id = id, limits = 0).awaitAndHandle {
                    swipeRefreshLayout.isRefreshing = false
                    stateText.visibility = View.VISIBLE
                    questionRec.visibility = View.INVISIBLE
                    stateText.text = "网络异常"
                }?.let {
                    swipeRefreshLayout.isRefreshing = false
                    userBar.setExpanded(true)
                    questionRec.scrollToPosition(0)
                    when (it.ErrorCode) {
                        0 -> {
                            it.data?.let { list ->
                                if (list.isNotEmpty()) {
                                    stateText.visibility = View.INVISIBLE
                                    questionRec.visibility = View.VISIBLE
                                    val itemList = list.map { qd ->
                                        QuestionItem(this@UserActivity, qd, onClick = {
                                            val mIntent: Intent = Intent(this@UserActivity, DetailActivity::class.java)
                                                    .putExtra("question", qd)
                                                    .putExtra("likeState", qd)
                                            startActivity(mIntent)
                                        }, onLongClick = {
                                            AnnoPreference.myId?.let {
                                                questionEdit(qd.id, it)
                                            }
                                        })
                                    }
                                    recController.refreshAll(itemList)
                                    recController.add(ButtonItem(ViewType.BOTTOM_TEXT))

                                    userTitle.text = "提出的问题"
                                } else {
                                    stateText.visibility = View.VISIBLE
                                    questionRec.visibility = View.INVISIBLE
                                    stateText.text = "你还没有提出过问题"
                                }
                            }
                        }
                        else -> {
                            stateText.visibility = View.VISIBLE
                            questionRec.visibility = View.INVISIBLE
                            stateText.text = "服务出现异常"
                        }
                    }
                }
            }
        }
    }

    fun questionEdit(question_id: Int, user_id: Int) {
        val list = arrayOf<CharSequence>("删除问题", "取消")
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setItems(list) { dialog, item ->
            when (item) {
                0 -> {
                    confirmAgain {
                        dialog.dismiss()
                        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                            AnnoService.deleteQuestion(user_id = user_id, question_id = question_id).awaitAndHandle {
                                Toast.makeText(this@UserActivity, "连接不到服务器", Toast.LENGTH_SHORT).show()
                            }?.ErrorCode?.let {
                                when (it) {
                                    0 -> {
                                        Toasty.success(this@UserActivity, "删除问题成功", Toast.LENGTH_SHORT).show()
                                        loadMyQuestions()
                                    }
                                    else -> {
                                        Toast.makeText(this@UserActivity, "删除问题失败", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> dialog.dismiss()
            }
        }
        val alert = alertDialogBuilder.create()
        alert.show()
    }

    private fun confirmAgain(todo: () -> Unit) {
        AlertDialog.Builder(this)
                .setTitle("再次确认是否删除问题")
                .setPositiveButton("好的") { dialog, _ ->
                    dialog?.dismiss()
                    todo.invoke()
                }
                .setNegativeButton("算了") { dialog, _ ->
                    dialog?.dismiss()

                }
                .show()
    }

}