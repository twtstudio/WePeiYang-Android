package com.twt.service.announcement.ui.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.twt.service.announcement.R
import com.twt.service.announcement.detail.DetailActivity
import com.twt.service.announcement.model.AnnoViewModel
import com.twt.service.announcement.service.AnnoService
import com.twt.service.announcement.service.Tag
import com.twt.service.announcement.ui.item.TagBottomItem
import com.twt.service.announcement.ui.item.TagsDetailItem
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick
//import java.lang.Exception
import kotlin.Exception


val str = "{\"ErrorCode\":0,\"msg\":\"\\u83b7\\u53d6tag\\u6811\\u6210\\u529f\\uff01\",\"data\":[{\"id\":1,\"name\":\"\\u536b\\u6d25\\u8def\",\"children\":[{\"id\":4,\"name\":\"\\u5bbf\\u820d\",\"children\":[{\"id\":8,\"name\":\"\\u70ed\\u6c34\",\"children\":[]}]},{\"id\":5,\"name\":\"\\u6559\\u5ba4\",\"children\":[]}]},{\"id\":2,\"name\":\"\\u5317\\u6d0b\\u56ed\",\"children\":[{\"id\":6,\"name\":\"\\u5bbf\\u820d\",\"children\":[{\"id\":9,\"name\":\"\\u70ed\\u6c34\",\"children\":[]}]},{\"id\":7,\"name\":\"\\u6559\\u5ba4\",\"children\":[]}]},{\"id\":3,\"name\":\"\\u5176\\u4ed6\",\"children\":[]}]}"


class AnnoActivity : AppCompatActivity() {
    private lateinit var tagTreeViewModel: AnnoViewModel
    private val tagTree by lazy { mutableMapOf<Int, List<Item>>() }
    private val pathTags by lazy { ItemManager() }
    private val detailTags by lazy { ItemManager() }
    private val firstItem by lazy { TagBottomItem("天津大学", 0) {} }
    private val data
        get() = tagTreeViewModel.tagTree

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anno)

        tagTreeViewModel = ViewModelProviders.of(this)[AnnoViewModel::class.java]
        data.observe(this, Observer<List<Tag>> {
            it?.let {
                detailTags.clear()
                detailTags.addAll(bindTagPathWithDetailTag(it))
            }
        })



        findViewById<FloatingActionButton>(R.id.fl_btn).apply {
            this.onClick {
                startActivity(Intent(this@AnnoActivity, DetailActivity::class.java))
            }
        }

        findViewById<RecyclerView>(R.id.path_rec).apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            adapter = ItemAdapter(pathTags)
        }

        findViewById<RecyclerView>(R.id.detail_rec).apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
                itemAnimator = SlideInDownAnimator()
                itemAnimator?.let {
                    it.addDuration = 1000
                    it.removeDuration = 1000
                    it.moveDuration = 1000
                    it.changeDuration = 1000
                }
            }
            adapter = ItemAdapter(detailTags)
        }

        pathTags.add(firstItem)
//        detailTags.addAll(bindTagPathWithDetailTag(data))

        GlobalScope.launch(Dispatchers.IO) {
//            val tags = AnnoService.getTagTree().awaitAndHandle { it.printStackTrace() }?.data
//            tags?.let {
//                Log.d("tag_tree",it.toString())
//                runOnUiThread {  tagTreeViewModel.setTagTree(it)}
//            }

//            这里有个转码的问题
//            try {
//                val question = AnnoService.getQuestion(mapOf("search_string" to "水")).awaitAndHandle { it.printStackTrace() }?.data
//                question?.let {
//                    Log.d("questions",it.toString())
//                }
//            }catch (e:Exception){
//              e.printStackTrace()
//            }

//            try{
//                val answer = AnnoService.getAnswer(2).awaitAndHandle { it.printStackTrace() }?.data
//                answer?.let {
//                    Log.d("answer",it.toString())
//                }
//            }catch (e:Exception){
//                e.printStackTrace()
//            }

//            点赞暂时有点问题，网络可以连上，okhttp 的 logginginterceptor 拦截到了返回的数据，但是这里获取不到
//            try {
//                val isLike = AnnoService.postThumbUp(2,1).awaitAndHandle { it.printStackTrace() }?.message
//                isLike?.let{
//                    Log.d("isLike",it)
//                }
//            }catch (e:Exception){
//                Log.d("problemss","what")
//                e.printStackTrace()
//            }

//            这个也是null
//            try {
//                val isLike = AnnoService.postThumDown(2,1).awaitAndHandle { it.printStackTrace() }?.message
//                isLike?.let{
//                    Log.d("isLike",it)
//                }
//            }catch (e:Exception){
//                Log.d("problemss","what")
//                e.printStackTrace()
//            }






        }
    }

    private fun bindTagPathWithDetailTag(_data: List<Tag>): List<Item> {

        return mutableListOf<Item>().apply {
            val index = pathTags.itemListSnapshot.size

            val items = _data.map { child ->
                TagsDetailItem(child.name, child.id) {
                    if (child.children.isNotEmpty()) {
                        pathTags.add(TagBottomItem(child.name, index) {
                            tagTree[index + 1]?.let { detailTags.refreshAll(it) }
                            (0 until pathTags.itemListSnapshot.size - index - 1).forEach { _ ->
                                pathTags.remove(pathTags.itemListSnapshot.last())
                                Log.e("delete tag", "de")
                            }
                        })
                        detailTags.refreshAll(bindTagPathWithDetailTag(child.children))
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
                    pathTags.remove(pathTags.itemListSnapshot.last())
                }
            }
        }
    }
}