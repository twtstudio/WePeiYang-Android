package com.twt.service.theory.view

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Window
import com.twt.service.theory.R
import com.youth.banner.Banner

class TheoryActivity : AppCompatActivity() {
    lateinit var list: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.theory_activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar).also {
            title = "理论答题"
            it.setBackgroundColor(Color.parseColor("#ffffff"))

            setSupportActionBar(it)
        }
        window.statusBarColor = Color.parseColor("#ffffff")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val banner = findViewById<Banner>(R.id.main_banner)
        banner.setImageLoader(object : GlideImageLoader() {})
        banner.setImages(list)

        val theoryTabLayout : TabLayout =  findViewById(R.id.main_tablayout)
        val theoryViewPager : ViewPager = findViewById(R.id.main_viewpager)
        val myhomePagerAdapter = TheoryPagerAdapter(supportFragmentManager)

        myhomePagerAdapter.apply {
            add(ExamFragment(),"考试")
            add(MessageFragement(),"通知")
        }

    }
}
