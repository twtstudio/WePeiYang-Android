package com.twt.service.announcement.ui.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import com.githang.statusbar.StatusBarCompat
import com.twt.service.announcement.R
import com.twt.service.announcement.model.AnnoViewModel
import com.twt.service.announcement.model.QuesViewModel
import com.twt.service.announcement.service.AnnoService
import com.twt.service.announcement.service.Question
import com.twt.service.announcement.service.Tag
import com.twt.service.announcement.ui.activity.detail.DetailActivity
import com.twt.service.announcement.ui.item.QuestionItem
import com.twt.service.announcement.ui.item.TagBottomItem
import com.twt.service.announcement.ui.item.TagsDetailItem
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import jp.wasabeef.recyclerview.animators.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick
//import java.lang.Exception
import kotlin.Exception


val str = "{\"ErrorCode\":0,\"msg\":\"\\u83b7\\u53d6tag\\u6811\\u6210\\u529f\\uff01\",\"data\":[{\"id\":1,\"name\":\"\\u536b\\u6d25\\u8def\",\"children\":[{\"id\":4,\"name\":\"\\u5bbf\\u820d\",\"children\":[{\"id\":8,\"name\":\"\\u70ed\\u6c34\",\"children\":[]}]},{\"id\":5,\"name\":\"\\u6559\\u5ba4\",\"children\":[]}]},{\"id\":2,\"name\":\"\\u5317\\u6d0b\\u56ed\",\"children\":[{\"id\":6,\"name\":\"\\u5bbf\\u820d\",\"children\":[{\"id\":9,\"name\":\"\\u70ed\\u6c34\",\"children\":[]}]},{\"id\":7,\"name\":\"\\u6559\\u5ba4\",\"children\":[]}]},{\"id\":3,\"name\":\"\\u5176\\u4ed6\",\"children\":[]}]}"


class AnnoActivity : AppCompatActivity() {
    private lateinit var tagTreeViewModel: AnnoViewModel
    private lateinit var quesViewModel: QuesViewModel
    private val tagTree by lazy { mutableMapOf<Int, List<Item>>() }
    private val pathTags by lazy { ItemManager() }
    private val detailTags by lazy { ItemManager() }
    private val quesRecController by lazy { ItemManager() }
    private val firstItem by lazy { TagBottomItem("天津大学", 0) {} }
    private val data
        get() = tagTreeViewModel.tagTree

    private val quesId
        get() = quesViewModel.quesId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anno)

        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        findViewById<Toolbar>(R.id.annoCommonToolbar).apply {
            this.title = "教务专区"
            setSupportActionBar(this)
            supportActionBar?.apply {
                setHomeButtonEnabled(true)
                setDisplayHomeAsUpEnabled(true)
            }
            this.setNavigationOnClickListener {
                finish()
            }
        }

        tagTreeViewModel = ViewModelProviders.of(this)[AnnoViewModel::class.java]
        data.observe(this, Observer<List<Tag>> {
            it?.let {
                detailTags.clear()
                detailTags.addAll(bindTagPathWithDetailTag(it))
            }
        })

        quesViewModel = ViewModelProviders.of(this)[QuesViewModel::class.java]
        quesId.observe(this, Observer<Int> {
            it?.let {
                Log.d("whataaa", it.toString())
                GlobalScope.launch {
                    try {
                        val data = AnnoService.getQuestion(mapOf("tagList" to listOf<Int>(it))).await().data
                        data?.let { quesList ->
                            Log.d("quesList", quesList.size.toString())
                            val items = quesList.map { ques ->
                                QuestionItem(ques.name) {}
                            }
                            runOnUiThread {
                                quesRecController.refreshAll(items)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })



        findViewById<FloatingActionButton>(R.id.fl_btn).apply {
            this.onClick {
                val testQuestion: Question = Question(
                        id = -1,
                        name = "如何评价天津大学补肾猫",
                        description = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                        user_id = 114514,
                        solved = 1,
                        no_commit = 0,
                        likes = 1919,
                        created_at = "1895年2月31日",
                        updated_at = "2020年20月12日"
                )
                startActivity(
                        Intent(this@AnnoActivity, DetailActivity::class.java)
                                .putExtra("title", testQuestion.name)
                                .putExtra("content", testQuestion.description)
                                .putExtra("userId", testQuestion.id)
                                .putExtra("status", testQuestion.solved)
                                .putExtra("time", testQuestion.created_at)
                                .putExtra("likeState", false)
                                .putExtra("likeCount", testQuestion.likes)
                )
            }
        }

        findViewById<RecyclerView>(R.id.path_rec).apply {
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

        findViewById<RecyclerView>(R.id.detail_rec).apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            adapter = ItemAdapter(detailTags).also {
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

        findViewById<RecyclerView>(R.id.ques_rec).apply {
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
        }

        pathTags.add(firstItem)
//        detailTags.addAll(bindTagPathWithDetailTag(data))

        GlobalScope.launch(Dispatchers.IO) {
            val tags = AnnoService.getTagTree().awaitAndHandle { it.printStackTrace() }?.data
            tags?.let {
                Log.d("tag_tree", it.toString())
                runOnUiThread { tagTreeViewModel.setTagTree(it) }
            }

        }
    }

    private fun bindTagPathWithDetailTag(_data: List<Tag>): List<Item> {

        return mutableListOf<Item>().apply {
            val index = pathTags.itemListSnapshot.size

            val items = _data.map { child ->
                TagsDetailItem(child.name, child.id) {
                    if (child.children.isNotEmpty()) {
                        if ((pathTags.itemListSnapshot.last() as TagBottomItem).content != child.name) {
                            pathTags.add(TagBottomItem(child.name, index) {
                                tagTree[index + 1]?.let { detailTags.refreshAll(it) }
                                (0 until pathTags.itemListSnapshot.size - index - 1).forEach { _ ->
                                    pathTags.removeAt(pathTags.size - 1)
                                    Log.e("delete tag", "de")
                                }
//                                quesViewModel.setQuesId(child.id)
                            })
                            detailTags.refreshAll(bindTagPathWithDetailTag(child.children))
                        }
                        quesViewModel.setQuesId(child.id)
                    } else {
                        // 跳转
                        val path = pathTags.itemListSnapshot.map {
                            (it as TagBottomItem).content
                        }.toMutableList().apply {
                            this.add(child.name)
                        }
                        Log.e("tag_path", path.toString())
                    }
                }
            }
            items.forEach { add(it) }
            tagTree[index] = items

            if (index == 1) firstItem.onclick = {
                tagTree.get(index)?.let { detailTags.refreshAll(it) }
                (0 until pathTags.itemListSnapshot.size - 1).forEach { _ ->
                    pathTags.removeAt(pathTags.size - 1)
                }
            }
        }
    }
}