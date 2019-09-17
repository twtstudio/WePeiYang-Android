package com.twt.service.theory.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Window
import android.widget.Toast
import com.twt.service.theory.R
import com.twt.service.theory.model.TheoryApi
import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.youth.banner.Banner
import com.youth.banner.listener.OnBannerListener
import kotlinx.android.synthetic.main.theory_common_toolbar.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class TheoryActivity : AppCompatActivity(), OnBannerListener {
    override fun OnBannerClick(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var list: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch(UI) {
            val data = TheoryApi.getTests().await()
           Toast.makeText(CommonContext.application, "取得${data.data?.size}场考试",Toast.LENGTH_SHORT).show()
        }
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.theory_activity_main)
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
        list.add("http://img2.imgtn.bdimg.com/it/u=667315488,3650645478&fm=26&gp=0.jpg")
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1562234279114&di=ed79c6a1f531d80673ebe34ea6ed697e&imgtype=0&src=http%3A%2F%2Fimg5q.duitang.com%2Fuploads%2Fitem%2F201502%2F23%2F20150223131655_ffVMV.jpeg")
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
            add(ExamFragment(), "考试")
            add(MessageFragement(), "通知")
        }
        theoryViewPager.adapter = myhomePagerAdapter
        theoryTabLayout.apply {
            setupWithViewPager(theoryViewPager)
            tabGravity = TabLayout.GRAVITY_FILL
            setSelectedTabIndicatorColor(Color.parseColor("#1E90FF"))
        }

    }
}
