package com.twt.service.job.story

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.Toast
import com.twt.service.job.R
import com.twt.service.job.service.Notice
import com.twt.service.job.service.Recruit
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import es.dmoral.toasty.Toasty

class StoryActivity : AppCompatActivity(),JobStoryContract.JobStoryView {

    private lateinit var storyBack : ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var itemManager: ItemManager
    private val jobStoryPresenter: JobStoryPresenterImp = JobStoryPresenterImp(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_activity_story)
        window.statusBarColor = resources.getColor(R.color.job_green)
        initView()
    }

    fun initView(){
        storyBack = findViewById(R.id.job_story_iv_back)
        recyclerView = findViewById(R.id.job_story_rv_detail)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
    }

    override fun showThree(notice: Notice) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showFair(recruit: Recruit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNull(msg: String) {
        Toasty.error(this, "meiyou", Toast.LENGTH_LONG, true).show()
    }

    override fun onError(msg: String) {
        Toasty.error(this, "cuowu", Toast.LENGTH_LONG, true).show()
    }

}
