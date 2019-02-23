package com.twt.service.job.home

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.widget.ImageView
import android.support.design.widget.TabLayout
import com.twt.service.job.R
import com.twt.service.job.search.JobSearchActivity
import com.twt.service.job.service.*

class JobHomeActivity : AppCompatActivity() {

    private lateinit var backImageView: ImageView
    private lateinit var searchImageView: ImageView
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: JobHomePageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_activity_home)
        window.statusBarColor = resources.getColor(R.color.job_green)
        bindId()
        initView()
        backImageView.setOnClickListener { onBackPressed() }
        searchImageView.setOnClickListener {
            startActivity(Intent(this,JobSearchActivity::class.java))
        }
    }

    private fun bindId() {
        backImageView = findViewById(R.id.job_home_iv_back)
        searchImageView = findViewById(R.id.job_home_search)
        tabLayout = findViewById(R.id.job_home_tl_tabs)
        viewPager = findViewById(R.id.job_home_vp_content)
    }

    private fun initView() {
        viewPagerAdapter = JobHomePageAdapter(supportFragmentManager)
        viewPager.adapter = viewPagerAdapter
        viewPager.offscreenPageLimit = 0
        repeat(4) {
            viewPagerAdapter.addFragment(JobFragment.newInstance(listsOfHome[it]), listsOfHome[it])
            tabLayout.addTab(tabLayout.newTab().setText(listsOfHome[it])) // 设置 tab 的标题
        }
        tabLayout.setupWithViewPager(viewPager) // 将 tab 和 viewpager 关联
    }
}
