package com.twt.service.announcement.ui.detail

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import cn.edu.twt.retrox.recyclerviewdsl.withItems
import com.githang.statusbar.StatusBarCompat
import com.twt.service.announcement.R
import com.twt.service.announcement.service.*
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.support.v4.onRefresh

/**
 * DetailActivity
 * @author TranceDream
 * 点进问题后显示的问题详情页面，显示详细问题和简略评论
 * 进入该Activity将问题数据传入，是[Question]对象
 */
class DetailActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val replyList: MutableList<Reply> = mutableListOf()
    private val replyLikeList: MutableList<Boolean> = mutableListOf()
    private val commentList: MutableList<Comment> = mutableListOf()
    private val commentLikeList: MutableList<Boolean> = mutableListOf()
    private lateinit var question: Question
    private var likeState: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        question = intent.getSerializableExtra("question") as Question
        // 设置状态栏的颜色
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)
        findViews()
        setToolbar(toolbar, this, "问题详情") {
            finish()
        }
        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
            getData()
        }.invokeOnCompletion {
            runOnUiThread {
                setRecyclerView()
            }
        }
        setRefresh()
    }

    /**
     * findViewById罢了
     */
    private fun findViews() {
        toolbar = findViewById(R.id.annoCommonToolbar)
        recyclerView = findViewById(R.id.annoDetailRecyclerView)
        swipeRefreshLayout = findViewById(R.id.annoDetailSwipeRefreshLayout)
    }


    /**
     * 这里设置一下问题详情页面RecyclerView的相关逻辑
     */
    private fun setRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity)
            withItems {
                addDetailQuestionItem(
                        question,
                        likeState
                ) {
                    DetailReplyBottomFragment.showDetailReplyBottomFragment(this@DetailActivity, question.id) {
                        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                            getData()
                        }.invokeOnCompletion {
                            runOnUiThread {
                                setRecyclerView()
                            }
                        }
                    }
                }
                replyList.forEach {
                    addDetailReplyItem(question.name, it, likeState, it.likes)
                }
                commentList.forEach {
                    addDetailCommentItem(it.username, it.contain, it.created_at, false, it.likes)
                }
            }
        }
    }

    /**
     * 下拉刷新(真的)
     * TODO: 我也希望这是真的
     */
    private fun setRefresh() {
        swipeRefreshLayout.onRefresh {
            GlobalScope.launch {
                getData()
                runOnUiThread {
                    setRecyclerView()
                }
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    /**
     * 获取评论和回复还有点赞状态
     * 还有用户名
     */
    private suspend fun getData() {
        swipeRefreshLayout.isRefreshing = true
        // 这里获取问题回复
        AnnoService.getAnswer(question.id, AnnoPreference.myId!!).awaitAndHandle {
            Toasty.error(this@DetailActivity, "获取回复失败").show()
        }?.data?.let {
            replyList.clear()
            it.forEach { reply ->
                replyList.add(reply)
            }
            // 这里对问题回复获取点赞状态
                replyLikeList.clear()
                replyList.forEach { reply ->
                    AnnoService.getLikedState("answer", AnnoPreference.myId!!, reply.id).awaitAndHandle {
                        Toasty.error(this@DetailActivity, "获取回复点赞状态失败").show()
                    }?.data?.let { replyLike ->
                        replyLikeList.add(replyLike.is_liked)
                    }
                }
            }
            // 这里获取问题评论
            // 为什么是commit?
            AnnoService.getCommit(question.id, AnnoPreference.myId!!).awaitAndHandle {
                Toasty.error(this@DetailActivity, "获取评论失败").show()
            }?.data?.let {
                commentList.clear()
                it.forEach { comment ->
                    commentList.add(comment)
                }
                // 这里对问题评论获取点赞状态
                commentLikeList.clear()
                commentList.forEach { comment ->
                    AnnoService.getLikedState("commit", AnnoPreference.myId!!, comment.id).awaitAndHandle {
                        Toasty.error(this@DetailActivity, "获取评论点赞状态失败").show()
                    }?.data?.let { commentLike ->
                        commentLikeList.add(commentLike.is_liked)
                    }
                }
            }
            // 这里获取问题的点赞状态
            AnnoService.getLikedState("question", AnnoPreference.myId!!, question.id).awaitAndHandle {
                Toasty.error(this@DetailActivity, "获取点赞状态失败").show()
            }?.data?.let {
                likeState = it.is_liked
            }
            swipeRefreshLayout.isRefreshing = false
    }
}