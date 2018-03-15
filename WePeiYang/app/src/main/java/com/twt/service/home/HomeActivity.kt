package com.twt.service.home

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.transition.Fade
import android.view.View
import com.twt.service.R
import com.twt.service.base.BaseActivity
import com.twt.service.home.common.CommonFragment
import com.twt.service.home.news.NewsFragment
import com.twt.service.home.tools.ToolsFragment
import com.twt.service.home.user.UserFragment
import com.twt.service.update.UpdateManager
import com.twt.service.view.BottomBar
import com.twt.service.view.BottomBarTab
import com.twt.service.widget.WidgetUpdateManger
import com.twt.wepeiyang.commons.experimental.pref.CommonPreferences

class HomeActivity : BaseActivity() {

    companion object {
        private const val TOS_URL = "https://support.twtstudio.com/topic/5/%E6%9C%89%E5%85%B3%E7%AC%AC%E4%B8%89%E6%96%B9%E5%B8%90%E5%8F%B7%E5%8F%8A%E6%95%B0%E6%8D%AE%E7%9A%84%E8%A1%A5%E5%85%85%E5%8D%8F%E8%AE%AE"
    }

    private lateinit var fragments: List<Fragment>

    private lateinit var viewPager: ViewPager
    private lateinit var bottomBar: BottomBar
    private lateinit var checkTosDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) with(window) {
            enterTransition = Fade()
            reenterTransition = Fade()
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            statusBarColor = Color.TRANSPARENT
        }

        fragments = listOf(CommonFragment(), NewsFragment(), ToolsFragment(), UserFragment())

        viewPager = findViewById<ViewPager>(R.id.fl_container).apply {
            adapter = object : FragmentPagerAdapter(supportFragmentManager) {
                override fun getItem(position: Int) = fragments[position]
                override fun getCount() = fragments.size
            }
            offscreenPageLimit = 4
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit
                override fun onPageSelected(position: Int) = bottomBar.setCurrentItem(position)
                override fun onPageScrollStateChanged(state: Int) = Unit
            })
        }

        bottomBar = findViewById<BottomBar>(R.id.bottomBar)
                .addItem(BottomBarTab(this, R.drawable.ic_common))
                .addItem(BottomBarTab(this, R.drawable.ic_news))
                .addItem(BottomBarTab(this, R.drawable.ic_tools))
                .addItem(BottomBarTab(this, R.drawable.ic_user)).apply {
                    setOnTabSelectedListener(object : BottomBar.OnTabSelectedListener {
                        override fun onTabSelected(position: Int, prePosition: Int) =
                                when (position - prePosition) {
                                    0 -> Unit
                                    -1, 1 -> viewPager.setCurrentItem(position, true)
                                    else -> viewPager.setCurrentItem(position, false)
                                }

                        override fun onTabUnselected(position: Int) = Unit
                        override fun onTabReselected(position: Int) = Unit
                    })
                }

        checkTosDialog = AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("微北洋用户协议")
                .setMessage("继续使用微北洋即代表我授权微北洋为我查询并存储我的办公网,图书馆,自行车等账号信息\n" + "详情请查看【有关第三方账号及数据的补充协议】")
                .setPositiveButton("同意") { dialog, _ ->
                    dialog.dismiss()
                    CommonPreferences.isAcceptTos = true
                }.setNeutralButton("查看条款详情") { _, _ ->
                    startActivity(Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(TOS_URL)
                    ))
                }.setNegativeButton("拒绝") { dialog, _ ->
                    dialog.dismiss()
                    finish()
                }.create()

        if (UpdateManager.getInstance().isAutoCheck) UpdateManager.getInstance().checkUpdate(this, false)
        WidgetUpdateManger.sendUpdateMsg(this)
    }

    override fun onResume() {
        super.onResume()
        if (!CommonPreferences.isAcceptTos) checkTosDialog.show()
    }

}
