package com.twt.service.schedule2.view.custom

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.TextView
import com.twt.service.schedule2.R
import android.view.View
import android.widget.ImageView
import com.twt.wepeiyang.commons.experimental.color.getColorCompat

class AddCustomCourseActivity: AppCompatActivity() {
    /*为了实现在多节课程的底部栏进入自定义课程界面 */
    companion object {
        fun startAddCustomActivity(context: Context) {
            val customIntent = Intent(context, AddCustomCourseActivity::class.java)
            customIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            context.startActivity(customIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.schedule_act_add_custom)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.statusBarColor = getColorCompat(R.color.colorPrimary)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbar.setBackgroundColor(getColorCompat(R.color.colorPrimaryDark))

        //更改toolbar的标题，隐藏刷新和添加按钮
        val titleText: TextView = findViewById(R.id.tv_toolbar_title)
        titleText.apply {
            text = "自定义事件"
        }
        val refreshImg: ImageView = findViewById(R.id.iv_toolbar_refresh)
        refreshImg.apply {
            visibility = View.GONE
        }
        val addImg: ImageView = findViewById(R.id.iv_toolbar_add)
        addImg.visibility = View.GONE

        //设置tab
        val customTabLayout: TabLayout = findViewById(R.id.tab_layout)
        val customViewPager: ViewPager = findViewById(R.id.custom_view_pager)
        val myhomePagerAdapter = CustomPagerAdapter(supportFragmentManager)

        myhomePagerAdapter.apply {
            add(AddCustomFragment(), "设置自定义事件")
            add(MyCustomFragment(), "我的自定义事件")
        }
        customViewPager.adapter = myhomePagerAdapter
        customTabLayout.apply {
            setupWithViewPager(customViewPager)
            tabGravity = TabLayout.GRAVITY_FILL
            setBackgroundColor(Color.parseColor("#F5F5F5"))
            setTabTextColors(Color.DKGRAY, getColorCompat(R.color.colorPrimary))
            setSelectedTabIndicatorColor(getColorCompat(R.color.colorPrimary))
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_schedule2,menu)
//        return super.onCreateOptionsMenu(menu)
//    }
}