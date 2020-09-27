package com.twt.service.announcement.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
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
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator
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
    private var nextUrl: String? = null
    private lateinit var quesRecManager: LinearLayoutManager
    private val user_id = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anno)

        // AnnoViewModel中有两个LiveData
        annoViewModel = ViewModelProviders.of(this)[AnnoViewModel::class.java]

        //沉浸式任务栏
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)
        //当发生网络连接错误或对应tag下没有问题，就会显示这个TextView
        hintText = findViewById(R.id.cannot_connect_text)
        findViewById<ImageView>(R.id.anno_back).setOnClickListener {
            onBackPressed()
        }
        initFloatingActionMenu()
        initRecyclerView()

        //设置LiveData的观察方法
        bindLiveData(this)

        //进页面的时候，初始化TagTree
        initTagTree()
        //进页面是显示所有问题
        getAllQuestions()

        // 这里获取一下本机用户的用户ID，存储到缓存
        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
            if (AnnoPreference.myId == null) {
                AnnoService.getUserIDByName(CommonPreferences.studentid, CommonPreferences.realName).awaitAndHandle {
                    Toasty.error(this@AnnoActivity, "获取用户ID失败，请重试").show()
                }?.data?.let {
                    AnnoPreference.myId = it.user_id
                }
            }
        }
    }

    private fun initRecyclerView() {
        //对应tagTree路径的rec
        tagPathRecyclerView = findViewById<RecyclerView>(R.id.path_rec).apply {
            layoutManager = MyLinearLayoutManager(context).apply {
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

        //点击了某个tag后显示它对应的所有子tag
        tagListRecyclerView = findViewById<RecyclerView>(R.id.detail_rec).apply {
            layoutManager = MyLinearLayoutManager(context).apply {
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
                                val tagId = if (url.contains("tagList")) url.split("tagList")[1][1].toInt() else null
                                url.split("page=")[1].toIntOrNull()?.let { page ->
                                    AnnoService.getQuestion(
                                            mapOf("searchString" to "",
                                                    "tagList" to if (tagId != null) listOf(tagId) else emptyList(),
                                                    "limits" to 20,
                                                    "page" to page, "user_id" to user_id)
                                    ).awaitAndHandle {
                                        it.printStackTrace()


                                    }?.data
                                }?.apply {
                                    nextUrl = if (to != total) next_page_url else null
                                }?.data?.let { next ->
                                    takeIf { next.isNotEmpty() }?.apply {
                                        val items = next.map { ques ->
                                            QuestionItem(context, ques) {
                                                closeFloatingMenu()
                                                // 跳转至详情页面
                                                startDetailActivity(ques)

                                            }
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
                            }
                        }
                    }
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
            this.setOnClickListener {
                //initTagTree()
                startActivity(Intent(this@AnnoActivity, AskQuestionActivity::class.java))
            }
        }

        findViewById<FloatingActionButton>(R.id.fa_c).apply {
            this.setOnClickListener {
                initTagTree()
                getAllQuestions()
//                closeFloatingMenu()
            }
        }

        findViewById<FloatingActionButton>(R.id.fa_d).apply {

            //TODO(添加新的问题)
            this.setOnClickListener {

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
            listTags.refreshAll(bindTagPathWithDetailTag(it))
            closeFloatingMenu()
        }

        //每次设置quesId，就会更新数据
        annoViewModel.quesId.bindNonNull(this) { id ->
            Log.d("whataaa", id.toString())
            closeFloatingMenu()
            GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                AnnoService.getQuestion(
                        mapOf("searchString" to "",
                                "tagList" to if (id == 0) emptyList() else listOf<Int>(id),
                                "limits" to 20,
                                "page" to 1, "user_id" to user_id)
                ).awaitAndHandle {
                    hintText.visibility = View.VISIBLE
                    quesDetailRecyclerView.visibility = View.INVISIBLE
                }?.data?.apply {
                    nextUrl = if (to != total) next_page_url else null
                }?.data?.let { quesList ->
                    Log.d("quesList", quesList.size.toString())
                    when (quesList.size) {
                        0 -> {
                            hintText.visibility = View.VISIBLE
                            quesDetailRecyclerView.visibility = View.INVISIBLE
                            hintText.text = "此标签下暂时没有问题"
                        }
                        else -> {

                            val items = quesList.map { ques ->
                                QuestionItem(context, ques) {
                                    closeFloatingMenu()
                                    // 跳转至详情页面
                                    startDetailActivity(ques)

                                }
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

                if (index == 1) {
                    firstItem.onclick = {
                        tagTree.get(1)?.let { listTags.refreshAll(it) }
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
                                this.add(child.name + ":" + child.id.toString())
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

    /**
     * 进入[DetailActivity]，并传入一个[Question]
     */
    private fun startDetailActivity(question: Question) {
        val mIntent: Intent = Intent(this, DetailActivity::class.java)
                .putExtra("question", question)
        startActivity(mIntent)
    }
}