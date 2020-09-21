package com.twt.service.announcement.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.githang.statusbar.StatusBarCompat
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionMenu
import com.twt.service.announcement.R
import com.twt.service.announcement.model.AnnoViewModel
import com.twt.service.announcement.service.AnnoService
import com.twt.service.announcement.service.Tag
import com.twt.service.announcement.ui.search.SearchActivity
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import jp.wasabeef.recyclerview.animators.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AnnoActivity : AppCompatActivity() {
    private lateinit var annoViewModel: AnnoViewModel
    private val tagTree by lazy { mutableMapOf<Int, List<Item>>() }
    private val pathTags by lazy { ItemManager() }
    private val listTags by lazy { ItemManager() }
    private val quesRecController by lazy { ItemManager() }
    private val firstItem by lazy { TagBottomItem("天津大学", 0) {} }

    private lateinit var quesDetailRecyclerView: RecyclerView
    private lateinit var tagPathRecyclerView: RecyclerView
    private lateinit var tagListRecyclerView: RecyclerView
    private lateinit var hintText: TextView
    private lateinit var floatingActionMenu: FloatingActionMenu
//    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anno)

        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        hintText = findViewById(R.id.cannot_connect_text)
        findViewById<ImageView>(R.id.anno_back).setOnClickListener {
            onBackPressed()
        }

//        toolbar = findViewById(R.id.toolbar)


//        findViewById<FloatingActionButton>(R.id.fl_btn).apply {
//            this.onClick {
//                val testQuestion: Question = Question(
//                        id = -1,
//                        name = "如何评价天津大学补肾猫",
//                        description = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
//                        user_id = 114514,
//                        solved = 1,
//                        no_commit = 0,
//                        likes = 1919,
//                        created_at = "1895年2月31日",
//                        updated_at = "2020年20月12日"
//                )
//                startActivity(
//                        Intent(this@AnnoActivity, DetailActivity::class.java)
//                                .putExtra("title", testQuestion.name)
//                                .putExtra("content", testQuestion.description)
//                                .putExtra("userId", testQuestion.id)
//                                .putExtra("status", testQuestion.solved)
//                                .putExtra("time", testQuestion.created_at)
//                                .putExtra("likeState", false)
//                                .putExtra("likeCount", testQuestion.likes)
//                )
//            }
//        }


        initFloatingActionMenu()

        initRecyclerView()

        bindLiveData()

        initTagTree()

        getAllQuestions()
    }

    private fun initRecyclerView() {
        tagPathRecyclerView = findViewById<RecyclerView>(R.id.path_rec).apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            adapter = ItemAdapter(pathTags)
            itemAnimator = SlideInDownAnimator()
            itemAnimator?.let {
                it.addDuration = 300
                it.removeDuration = 800
                it.changeDuration = 300
                it.moveDuration = 500
            }

        }

        tagListRecyclerView = findViewById<RecyclerView>(R.id.detail_rec).apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            adapter = ItemAdapter(listTags).also {
                it.setHasStableIds(true)
            }
            itemAnimator = SlideInDownAnimator()
            itemAnimator?.let {
                it.addDuration = 300
                it.removeDuration = 800
                it.moveDuration = 300
                it.changeDuration = 500
            }
        }

        quesDetailRecyclerView = findViewById<RecyclerView>(R.id.ques_rec).apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = ItemAdapter(quesRecController)
            itemAnimator = FadeInAnimator()
            itemAnimator?.let {
                it.addDuration = 300
                it.removeDuration = 800
                it.changeDuration = 300
                it.moveDuration = 500
            }
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    closeFloatingMenu()
                }
            })
        }
    }

    private fun initFloatingActionMenu() {
        floatingActionMenu = findViewById(R.id.anno_fl_menu)

        findViewById<FloatingActionButton>(R.id.fa_a).apply {
            this.setOnClickListener {
                closeFloatingMenu()
                startActivity(Intent(this@AnnoActivity, SearchActivity::class.java))
            }
        }

        findViewById<FloatingActionButton>(R.id.fa_b).apply {

            //TODO(添加新的问题)

        }

        findViewById<FloatingActionButton>(R.id.fa_c).apply {
            this.setOnClickListener {
                initTagTree()
                getAllQuestions()
                closeFloatingMenu()
            }
        }
    }

    private fun closeFloatingMenu() = floatingActionMenu.close(true)

    private fun getAllQuestions() = annoViewModel.searchQuestion(0)

    private fun initTagTree() {
        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
            AnnoService.getTagTree().awaitAndHandle {
//                it.printStackTrace()
                Toast.makeText(this@AnnoActivity, "出了点问题", Toast.LENGTH_SHORT).show()
            }?.data?.let {
                pathTags.clear()
                pathTags.add(firstItem)
                Log.d("tag_tree", it.toString())
                annoViewModel.setTagTree(it)
            }
        }
    }

    private fun bindLiveData() {
        annoViewModel = ViewModelProviders.of(this)[AnnoViewModel::class.java]
        annoViewModel.tagTree.bindNonNull(this) {
            listTags.clear()
            listTags.addAll(bindTagPathWithDetailTag(it))
            closeFloatingMenu()
        }

        annoViewModel.quesId.bindNonNull(this) {
            Log.d("whataaa", it.toString())
            GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                val data = if (it == 0) {
                    AnnoService.getQuestion(mapOf()).awaitAndHandle {
                        hintText.visibility = View.VISIBLE
                        quesDetailRecyclerView.visibility = View.INVISIBLE
                    }?.data
                } else {
                    AnnoService.getQuestion(mapOf("tagList" to listOf(it))).awaitAndHandle {
                        hintText.visibility = View.VISIBLE
                        quesDetailRecyclerView.visibility = View.INVISIBLE
                    }?.data
                }
                data?.let { quesList ->
                    Log.d("quesList", quesList.size.toString())
                    when (quesList.size) {
                        0 -> {
                            hintText.visibility = View.VISIBLE
                            quesDetailRecyclerView.visibility = View.INVISIBLE
                            hintText.text = "此标签下暂时没有问题"
                        }
                        else -> {
                            val items = quesList.map { ques ->
                                QuestionItem(ques) {
                                    closeFloatingMenu()

                                    //TODO(问题详情跳转)

                                }
                            }
                            hintText.visibility = View.INVISIBLE
                            quesDetailRecyclerView.visibility = View.VISIBLE
                            quesRecController.refreshAll(items)
                            quesDetailRecyclerView.scrollToPosition(0)

//                    Toast.makeText(this@AnnoActivity, "获取数据成功", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            closeFloatingMenu()
        }
    }

    private fun bindTagPathWithDetailTag(_data: List<Tag>): List<Item> =
            mutableListOf<Item>().apply {
                val index = pathTags.itemListSnapshot.size

                index.takeIf { it == 1 }?.apply {
                    firstItem.onclick = {
                        tagTree.get(index)?.let { listTags.refreshAll(it) }
                        (0 until pathTags.itemListSnapshot.size - 1).forEach { _ ->
                            pathTags.removeAt(pathTags.size - 1)
                        }

                        getAllQuestions()
                    }
                }

                tagTree[index] = _data.map { child ->
                    TagsDetailItem(child.name, child.id) {
                        if (child.children.isNotEmpty()) {
                            pathTags.add(TagBottomItem(child.name, index) {
                                tagTree[index + 1]?.let { listTags.refreshAll(it) }
                                (0 until pathTags.itemListSnapshot.size - index - 1).forEach { _ ->
                                    pathTags.removeAt(pathTags.size - 1)
                                    Log.e("delete tag", "de")
                                }
                                closeFloatingMenu()
                            })
                            listTags.refreshAll(bindTagPathWithDetailTag(child.children))
                        } else {

                            // 到最后一层标签后，打印当前路径或其他操作
                            val path = pathTags.itemListSnapshot.map {
                                (it as TagBottomItem).content
                            }.toMutableList().apply {
                                this.add(child.name)
                            }
                            Log.e("tag_path", path.toString())
                        }
                        closeFloatingMenu()
                        annoViewModel.searchQuestion(child.id)
                    }
                }.also {
                    addAll(it)
                }
            }
}