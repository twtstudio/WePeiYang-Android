package com.yookiely.lostfond2.mylist

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.example.lostfond2.R
import com.orhanobut.hawk.Hawk
import com.yookiely.lostfond2.service.Utils
import org.jetbrains.anko.textColor

class MyListActivity : AppCompatActivity() {

    private lateinit var popupWindow: PopupWindow
    private lateinit var imageView: ImageView
    private lateinit var beiyangyuan: TextView
    private lateinit var weijinlu: TextView
    private var campus: Int = 1// 1 北洋园 ，2 卫津路

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.lf2_activity_my_list)
        window.statusBarColor = resources.getColor(R.color.statusBarColor)
        val toolbar: Toolbar = findViewById(R.id.tb_mylist)
        toolbar.apply {
            title = "我的"
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }

        imageView = toolbar.findViewById(R.id.iv_list_campus)

        val popupWindowView = LayoutInflater.from(this).inflate(R.layout.lf2_popupwindow_campus, null, false)

        toolbar.setNavigationOnClickListener { onBackPressed() }
        imageView.setOnClickListener { initPopupWindow(popupWindowView) }

        val myListPager: ViewPager = findViewById(R.id.vp_mylist)
        val myListTabLayout: TabLayout = findViewById(R.id.tl_mylist)
        val myListPagerAdapter = MyListPagerAdapter(supportFragmentManager)
        myListPagerAdapter.apply {
            add(MyListFragment.newInstance(Utils.STRING_FOUND), "我捡到的")
            add(MyListFragment.newInstance(Utils.STRING_LOST), "我丢失的")
        }
        myListPager.adapter = myListPagerAdapter
        myListTabLayout.apply {
            setupWithViewPager(myListPager)
            tabGravity = TabLayout.GRAVITY_FILL
            setSelectedTabIndicatorColor(Color.parseColor("#00a1e9"))
        }
    }

    private fun initPopupWindow(view: View) {
        popupWindow = PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true)
        popupWindow.apply {
            isFocusable = true
            isOutsideTouchable = false// 设置PopupWindow是否能响应外部点击事件
            isTouchable = true// 设置PopupWindow是否能响应点击事件
            showAsDropDown(imageView, Gravity.LEFT, 0, 30)
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@MyListActivity, R.color.white_color)))
        }
        bgAlpha(0.5f)
        popupWindow.setOnDismissListener {
            // popupWindow隐藏时恢复屏幕正常透明度
            bgAlpha(1f)
        }

        beiyangyuan = view.findViewById(R.id.tv_mylist_byy)
        weijinlu = view.findViewById(R.id.tv_mylist_wjl)

        // 根据原始校区值，设置颜色，选中的校区为蓝色
        campus = Utils.campus ?: 1
        if (campus == Utils.CAMPUS_BEI_YANG_YUAN) {
            beiyangyuan.textColor = Color.parseColor("#4894d5")
            weijinlu.textColor = Color.parseColor("#656565")
        } else {
            weijinlu.textColor = Color.parseColor("#4894d5")
            beiyangyuan.textColor = Color.parseColor("#656565")
        }

        // 设置弹窗中的textview的监听事件，修改校区，并修改数据库中的校区对应值
        beiyangyuan.setOnClickListener {
            Utils.campus = Utils.CAMPUS_BEI_YANG_YUAN
            beiyangyuan.textColor = Color.parseColor("#4894d5")
            weijinlu.textColor = Color.parseColor("#656565")
            popupWindow.dismiss()
        }
        weijinlu.setOnClickListener {
            Utils.campus = Utils.CAMPUS_WEI_JIN_LU
            weijinlu.textColor = Color.parseColor("#4894d5")
            beiyangyuan.textColor = Color.parseColor("#656565")
            popupWindow.dismiss()
        }
    }

    private fun bgAlpha(bgAlpha: Float) {
        // 修改屏幕背景色
        val lp = window.attributes
        lp.alpha = bgAlpha // 0.0-1.0
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.attributes = lp
    }
}
