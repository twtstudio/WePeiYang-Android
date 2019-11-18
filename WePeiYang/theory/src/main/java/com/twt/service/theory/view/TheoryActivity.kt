package com.twt.service.theory.view

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Window
import com.twt.service.theory.R
import com.twt.service.theory.model.TheoryApi
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.youth.banner.Banner
import com.youth.banner.listener.OnBannerListener
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.theory_common_toolbar.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class TheoryActivity : AppCompatActivity(), OnBannerListener {
    private val examFragment = ExamFragment()
    private val messageFragement = MessageFragement()

    override fun onResume() {//每次显示都更新
        super.onResume()
            launch(UI) {
            //获取考试
            try {
                val dat = TheoryApi.getTests().await()
                examFragment.setTestList(dat.data)
            } catch (e: Exception) {
                //理论答题中第一个网络请求
                Toasty.error(this@TheoryActivity, "获取试卷失败, 请检查是否已连接校园网").show()
            }
        }
        launch(UI) {
            //获取通知
            try {
                val dat = TheoryApi.getNotice().await()
                messageFragement.setNoticeList(dat.data)
            } catch (e: Exception) {
                Toasty.error(this@TheoryActivity, "获取通知失败").show()
            }
        }
    }

    override fun OnBannerClick(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var list: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.theory_activity_main)
        // Log.d("TTTTT", CommonPreferences.token)
        theory_person_profile.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }
        theory_back.setOnClickListener {
            finish()
        }
        theory_search.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        window.statusBarColor = Color.parseColor("#FFFFFF")
        enableLightStatusBarMode(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        list.add("https://theory-new.twt.edu.cn/static/media/banner.cae4e845.png")
        val banner = findViewById<Banner>(R.id.main_banner)
        banner.apply {
            setImageLoader(GlideImageLoader())
            setImages(list)
            this.start()
        }
        val theoryTabLayout: TabLayout = findViewById(R.id.main_tablayout)
        val theoryViewPager: ViewPager = findViewById(R.id.main_viewpager)
        val myhomePagerAdapter = TheoryPagerAdapter(supportFragmentManager)

        myhomePagerAdapter.apply {
            add(examFragment, "考试")
            add(messageFragement, "通知")
        }
        theoryViewPager.adapter = myhomePagerAdapter
        theoryTabLayout.apply {
            setupWithViewPager(theoryViewPager)
            tabGravity = TabLayout.GRAVITY_FILL
            setSelectedTabIndicatorColor(Color.parseColor("#1E90FF"))
        }

    }
}
