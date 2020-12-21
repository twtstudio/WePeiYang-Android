package com.twt.service.announcement.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import com.twt.service.announcement.service.AnnoPreference
import com.twt.service.announcement.service.AnnoService
import com.twt.service.announcement.service.Question
import com.twt.service.announcement.service.Tag
import com.twt.service.announcement.ui.detail.AskQuestionActivity
import com.twt.service.announcement.ui.detail.DetailActivity
import com.twt.service.announcement.ui.search.SearchActivity
import com.twt.service.announcement.ui.user.UserActivity
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.support.v4.onRefresh
import java.net.URLDecoder


class AnnoActivity : AppCompatActivity() {
    private lateinit var annoViewModel: AnnoViewModel
    private val tagTree = mutableMapOf<Int, List<Item>>()
    private val pathTags by lazy { ItemManager() }
    private val listTags = ItemManager()
    private val quesRecController by lazy { ItemManager() }
    private val firstItem by lazy { TagBottomItem("天津大学", 0) {} }

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var quesDetailRecyclerView: RecyclerView
    private lateinit var tagPathRecyclerView: RecyclerView
    private lateinit var tagListRecyclerView: RecyclerView
    private lateinit var appBar: AppBarLayout
    private lateinit var hintText: TextView
    private lateinit var searchButton: ImageView
    private lateinit var floatingActionMenu: FloatingActionMenu
    private var nextUrl: String? = null
    private lateinit var quesRecManager: LinearLayoutManager
    private var user_id = 0
    private var canRefresh = true
    private var currentTagId = 0

    private val selectedTagIdList = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anno)
        appBar = findViewById(R.id.toolbar)
        swipeRefreshLayout = findViewById(R.id.anno_refresh)
        // AnnoViewModel中有两个LiveData
        annoViewModel = ViewModelProviders.of(this)[AnnoViewModel::class.java]

        //沉浸式任务栏
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)
        //当发生网络连接错误或对应tag下没有问题，就会显示这个TextView
        hintText = findViewById(R.id.cannot_connect_text)
        findViewById<ImageView>(R.id.anno_back).setOnClickListener {
            onBackPressed()
        }
        findViewById<ImageView>(R.id.search_button).setOnClickListener {
            startActivity(Intent(this@AnnoActivity, SearchActivity::class.java))
        }
        initFloatingActionMenu()
        initSwipeRefreshLayout()
        initRecyclerView()

        //设置LiveData的观察方法
        bindLiveData(this)

        //进页面的时候，初始化TagTree
        initTagTree()
        //进页面是显示所有问题
        getAllQuestions()

        // 这里获取一下本机用户的用户ID，存储到缓存
        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
            AnnoService.getUserIDByName(CommonPreferences.studentid, CommonPreferences.realName).awaitAndHandle {
                //                Toast.makeText(this@AnnoActivity, "获取用户ID失败",Toast.LENGTH_SHORT).show()
            }?.data?.let {
                AnnoPreference.myId = it.user_id
                user_id = it.user_id
            }
        }
    }

    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout.onRefresh {
            swipeRefreshLayout.isRefreshing = true
            GlobalScope.launch {
                if (canRefresh) {
                    canRefresh = false
                    quesRecController.clear()
                    appBar.setExpanded(true)
                    initTagTree()
                    annoViewModel.searchQuestion(currentTagId)
//                closeFloatingMenu()
                }
            }.invokeOnCompletion {
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun initRecyclerView() {
        //点击了某个tag后显示它对应的所有子tag
        tagListRecyclerView = findViewById<RecyclerView>(R.id.detail_rec).apply {
            layoutManager = MyLinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            adapter = ItemAdapter(listTags).also {
                it.setHasStableIds(true)
            }
            setItemViewCacheSize(128)
            itemAnimator = SlideInDownAnimator()
            itemAnimator?.let {
                it.addDuration = 300
                it.removeDuration = 800
                it.moveDuration = 300
                it.changeDuration = 500
            }
        }

        quesDetailRecyclerView = findViewById<RecyclerView>(R.id.ques_rec).apply {
            layoutManager = MyLinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = ItemAdapter(quesRecController)
            itemAnimator = FadeInAnimator()
            itemAnimator?.let {
                it.addDuration = 100
                it.removeDuration = 400
                it.changeDuration = 300
                it.moveDuration = 200
            }

            quesRecManager = layoutManager as LinearLayoutManager

            //滑动到最后一条，会进行加载下一页
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    closeFloatingMenu()

                    if (newState == RecyclerView.SCROLL_STATE_IDLE && quesRecManager.findLastVisibleItemPosition() == quesRecController.itemListSnapshot.size - 1) {
                        nextUrl?.let { url ->
                            GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                                val currentUrl = URLDecoder.decode(url, "GBK")
                                val str1 = currentUrl.split("tagList=[")[1]
                                var tag: String? = null
                                if (!str1.startsWith("]")) {
                                    tag = str1.split("]")[1]
                                }
                                url.split("page=")[1].toIntOrNull()?.let { page ->
                                    AnnoService.getQuestion(
                                            mapOf("searchString" to "",
                                                    "tagList" to if (tag != null) listOf(tag) else emptyList(),
                                                    "limits" to 20,
                                                    "user_id" to user_id,
                                                    "page" to page)
                                    ).awaitAndHandle {
                                        it.printStackTrace()
                                        Log.d("getQuestionerror", "获取问题列表失败: " + tag)
                                        quesRecController.removeAt(quesRecController.itemListSnapshot.size - 1)
                                        Toast.makeText(this@AnnoActivity, "网络连接失败", Toast.LENGTH_SHORT).show()
                                    }
                                }?.let {
                                    if (it.ErrorCode == 0) {
                                        it.data?.apply {
                                            nextUrl = if (to != total) next_page_url else null
                                        }?.data?.let { next ->
                                            takeIf { next.isNotEmpty() }?.apply {
                                                val items = next.map { ques ->
                                                    QuestionItem(context, ques, onClick = {
                                                        closeFloatingMenu()
                                                        // 跳转至详情页面
                                                        startDetailActivity(ques)
                                                    })
                                                }
                                                hintText.visibility = View.INVISIBLE
                                                quesDetailRecyclerView.visibility = View.VISIBLE
                                                with(quesRecController) {
                                                    removeAt(quesRecController.itemListSnapshot.size - 1)
                                                    addAll(items)
                                                    if (nextUrl != null) {
                                                        add(ButtonItem(ViewType.PROCESS_BAR))
                                                    } else {
                                                        add(ButtonItem(ViewType.BOTTOM_TEXT))
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        quesRecController.removeAt(quesRecController.itemListSnapshot.size - 1)
                                        quesRecController.add(ButtonItem(ViewType.BOTTOM_TEXT))
                                        Toast.makeText(this@AnnoActivity, "服务器返回数据出错", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                }
            })
        }
    }

    private fun initFloatingActionMenu() {
        floatingActionMenu = findViewById(R.id.anno_fl_menu)

//        findViewById<FloatingActionButton>(R.id.fa_a).apply {
//            this.setOnClickListener {
//                closeFloatingMenu()
//                startActivity(Intent(this@AnnoActivity, SearchActivity::class.java))
//            }
//        }

        findViewById<FloatingActionButton>(R.id.fa_b).apply {

            //TODO(添加新的问题)
            this.setOnClickListener {
                //initTagTree()
                closeFloatingMenu()
                startActivity(Intent(this@AnnoActivity, AskQuestionActivity::class.java))
            }
        }

        findViewById<FloatingActionButton>(R.id.fa_d).apply {

            //TODO(添加新的问题)
            this.setOnClickListener {
                closeFloatingMenu()
                startActivity(Intent(this@AnnoActivity, UserActivity::class.java))
            }
        }
    }

    private fun closeFloatingMenu() = floatingActionMenu.close(true)

    private fun getAllQuestions() = annoViewModel.searchQuestion(0)

    private fun initTagTree() {
        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
            AnnoService.getTagTree().awaitAndHandle {
                Toast.makeText(this@AnnoActivity, "出了点问题", Toast.LENGTH_SHORT).show()
            }?.data?.let {
                pathTags.clear()
                pathTags.add(firstItem)
                Log.d("tag_tree", it.toString())
                annoViewModel.setTagTree(it)
            }
        }
    }

    private fun bindLiveData(context: Context) {
        annoViewModel.tagTree.bindNonNull(this) {
            listTags.refreshAll(bindTagPathWithDetailTag(it[0].children))
            closeFloatingMenu()
        }

        //每次设置quesId，就会更新数据
        // 什么事quesId?   (=^•⊥•^=)
        annoViewModel.quesId.bindNonNull(this) { id ->
            Log.d("whataaa", id.toString())
            closeFloatingMenu()
            GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                AnnoService.getQuestion(
                        mapOf("searchString" to "",
                                "tagList" to if (id == 0) emptyList() else selectedTagIdList.toList(),
                                "limits" to 20,
                                "page" to 1, "user_id" to user_id)
                ).awaitAndHandle {
                    Log.d("getQuestion error", it.message)
                    hintText.visibility = View.VISIBLE
                    quesDetailRecyclerView.visibility = View.INVISIBLE
                    canRefresh = true
                    throw it
                }?.data?.apply {
                    Log.d("whatquestion", this.data.toString())
                    nextUrl = if (to != total) next_page_url else null
                }?.data?.let { quesList ->
                    Log.d("whataaa", quesList.map { it.name }.toString())

                    GlobalScope.launch {
                        delay(1000)
                        canRefresh = true
                    }

                    Log.d("quesList", quesList.size.toString())
                    when (quesList.size) {
                        0 -> {
                            hintText.visibility = View.VISIBLE
                            quesDetailRecyclerView.visibility = View.INVISIBLE
                            hintText.text = "此标签下暂时没有问题"
                        }
                        else -> {

                            val items = quesList.map { ques ->
                                QuestionItem(context, ques, onClick = {
                                    closeFloatingMenu()
                                    // 跳转至详情页面
                                    startDetailActivity(ques)

                                })
                            }
                            hintText.visibility = View.INVISIBLE
                            quesDetailRecyclerView.visibility = View.VISIBLE
                            quesRecController.refreshAll(items)
                            quesRecController.add(
                                    if (nextUrl != null) {
                                        ButtonItem(ViewType.PROCESS_BAR)
                                    } else {
                                        ButtonItem(ViewType.BOTTOM_TEXT)
                                    }
                            )

                            quesDetailRecyclerView.scrollToPosition(0)
                            Log.d("bugwhat", "111")

                        }
                    }
                }
            }
        }
    }

    //将三个rec，通过LiveData等绑定在一起
    private fun bindTagPathWithDetailTag(_data: List<Tag>): List<Item> =
            mutableListOf<Item>().apply {
                val index = pathTags.itemListSnapshot.size
                tagTree[index] = _data.map { child ->
                    TagsDetailItem(child.name, child.id, selectedTagIdList.contains(child.id)) {
                        if (child.children.isNotEmpty()) {
                            try {
                                if ((pathTags.itemListSnapshot[pathTags.size - 2] as TagBottomItem).content == child.name)
                                    pathTags.removeAt(pathTags.itemListSnapshot.lastIndex)
                            } catch (e: Exception) {
                                // 越界
                            }
                        } else {

                            if (selectedTagIdList.contains(child.id)) {
                                selectedTagIdList.remove(child.id)
                            } else {
                                selectedTagIdList.add(child.id)
                            }
                        }
                        closeFloatingMenu()
                        currentTagId = child.id
                        annoViewModel.searchQuestion(child.id)
                    }
                }.also {
                    var str = ""
                    for (tag in it){
                        str += tag.content + "---"
                    }
                    Log.d("tagdetaillist",str)
                    addAll(it)
                }
            }

    /**
     * 进入[DetailActivity]，并传入一个[Question]
     */
    private fun startDetailActivity(question: Question) {
        val mIntent: Intent = Intent(this, DetailActivity::class.java)
                .putExtra("question", question)
        startActivity(mIntent)
    }
}