package com.twt.service.home

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.design.widget.TabLayout.OnTabSelectedListener
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.twt.service.R
import com.twt.service.home.tools.ToolsFragment
import com.twt.service.home.user.UserFragment
import com.twt.service.schedule2.view.custom.CustomPagerAdapter
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import com.twt.wepeiyang.commons.experimental.extensions.enableLightStatusBarMode
import pub.devrel.easypermissions.EasyPermissions


class HomeActivity : AppCompatActivity() {
    val titles = arrayOf("主页", "我的")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        EasyPermissions.requestPermissions(this, "微北洋需要外部存储来提供必要的缓存\n 需要位置信息和手机状态来获取校园网连接状态", 0,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE)

        setContentView(R.layout.activity_home)

        enableLightStatusBarMode(true)
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = getColorCompat(com.twt.service.R.color.white)
        }
//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        toolbar.setNavigationOnClickListener {
//            finish()
//        }
//        toolbar.setBackgroundColor(getColorCompat(R.color.colorPrimaryDark))

        //设置tab
        val customTabLayout: TabLayout = findViewById(R.id.tab_layout)
        val customViewPager: ViewPager = findViewById(R.id.custom_view_pager)
        val myhomePagerAdapter = CustomPagerAdapter(supportFragmentManager)

        myhomePagerAdapter.apply {
            add(HomeFragment(), "")
            add(ToolsFragment(), "")
            add(UserFragment(), "")
        }
//
//        for (i in titles.indices) {
//            val tab: TabLayout.Tab? = customTabLayout.getTabAt(i)
//            tab?.setCustomView(R.layout.view_tab) //给每一个tab设置自定义的标题布局
//            val tv: TextView = tab?.customView?.findViewById(R.id.tv_tab)!!
//            tv.text = titles[i]
//        }

        customViewPager.adapter = myhomePagerAdapter
        customTabLayout.apply {
            setupWithViewPager(customViewPager)
            tabGravity = TabLayout.GRAVITY_FILL
            setBackgroundColor(Color.parseColor("#F5F5F5"))
            setTabTextColors(Color.DKGRAY, getColorCompat(R.color.GRAY))
            setSelectedTabIndicatorColor(getColorCompat(R.color.GRAY))
        }

        customTabLayout.getTabAt(0)?.setIcon(R.drawable.ic_home_selected)
        customTabLayout.getTabAt(1)?.setIcon(R.drawable.ic_more_unselected)
        customTabLayout.getTabAt(2)?.setIcon(R.drawable.ic_user_unselected)

        customTabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            //监听tab当前选项
            override fun onTabSelected(tab: TabLayout.Tab) {

                for (i in titles.indices) {
                    val index = customTabLayout.selectedTabPosition
                    when (index) {
                        0 -> {
                            customTabLayout.getTabAt(0)?.setIcon(R.drawable.ic_home_selected)
                            customTabLayout.getTabAt(1)?.setIcon(R.drawable.ic_more_unselected)
                            customTabLayout.getTabAt(2)?.setIcon(R.drawable.ic_user_unselected)
                        }
                        1 -> {
                            customTabLayout.getTabAt(0)?.setIcon(R.drawable.ic_home_unselected)
                            customTabLayout.getTabAt(1)?.setIcon(R.drawable.ic_more_selected)
                            customTabLayout.getTabAt(2)?.setIcon(R.drawable.ic_user_unselected)
                        }
                        2 -> {
                            customTabLayout.getTabAt(0)?.setIcon(R.drawable.ic_home_unselected)
                            customTabLayout.getTabAt(1)?.setIcon(R.drawable.ic_more_unselected)
                            customTabLayout.getTabAt(2)?.setIcon(R.drawable.ic_user_selected)
                        }
                    }
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_schedule2,menu)
//        return super.onCreateOptionsMenu(menu)
//    }


}