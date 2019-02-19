package com.twt.service.job.story

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.Toast
import com.twt.service.job.R
import com.twt.service.job.service.*
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.withItems
import es.dmoral.toasty.Toasty

class StoryActivity : AppCompatActivity(), JobStoryContract.JobStoryView {

    private lateinit var storyBack: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val jobStoryPresenter: JobStoryPresenterImp = JobStoryPresenterImp(this)
    private var items = mutableListOf<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_activity_story)
        window.statusBarColor = resources.getColor(R.color.job_green)
        initView()
        val intent = intent
        val id = intent.getStringExtra(KEY_ID)
        val kind = intent.getStringExtra(KEY_KIND)
        jobStoryPresenter.getDetail(id.toInt(), kind)
        storyBack.setOnClickListener { onBackPressed() }
    }

    fun initView() {
        storyBack = findViewById(R.id.job_story_iv_back)
        recyclerView = findViewById(R.id.job_story_rv_detail)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
    }

    override fun showThree(notice: NoticeAfter) {
        notice.apply {
            if (hasAttach) {
                items.add(TopItem(notice, true))
                attachs.entries.forEach {
                    items.add(AttachItem(it.key, it.value, this@StoryActivity))
                }
            } else {
                items.add(TopItem(notice, false))
            }
        }
        recyclerView.withItems(items)
    }

    override fun showFair(recruit: Recruit) {
        recruit.apply {
            val notice = Functions.convertNoticeAfter(Functions.convertNotice(recruit)!!)
            items.add(TopItem(notice, true))
            items.add(PDItem(recruit.date, recruit.place))
            notice.attachs.entries.forEach {
                items.add(AttachItem(it.key, it.value, this@StoryActivity))
            }
            recyclerView.withItems(items)
        }

    }

    override fun onNull(msg: String) {
        Toasty.error(this, "meiyou", Toast.LENGTH_LONG, true).show()
    }

    override fun onError(msg: String) {
        Toasty.error(this, "error T_T", Toast.LENGTH_LONG, true).show()
    }

}
