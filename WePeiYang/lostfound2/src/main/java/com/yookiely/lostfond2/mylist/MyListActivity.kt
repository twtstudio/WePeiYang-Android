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
import org.jetbrains.anko.textColor

class MyListActivity : AppCompatActivity() {

    private lateinit var popupWindow: PopupWindow
    private lateinit var imageView: ImageView
    private lateinit var beiyangyuan: TextView
    private lateinit var weijinlu: TextView
    private var campus: Int = 1//1 北洋园 ，2 卫津路

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.lf2_activity_my_list)
        var toolbar: Toolbar = findViewById(R.id.mylist_toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = "我的"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imageView = toolbar.findViewById(R.id.list_img)

        val popupwindowView = LayoutInflater.from(this).inflate(R.layout.lf2_popupwindow_campus, null, false)

        toolbar.setNavigationOnClickListener { onBackPressed() }
        imageView.setOnClickListener { v -> initPopupwindow(popupwindowView) }

        val mylist_pager: ViewPager = findViewById(R.id.mylist_pager)

        val mylist_tabLayout: TabLayout = findViewById(R.id.mylist_tabLayout)
        val mylistPagerAdapter = MylistPagerAdapter(supportFragmentManager)
        mylistPagerAdapter.add(MylistFragement.newInstance("found"), "我捡到的")
        mylistPagerAdapter.add(MylistFragement.newInstance("lost"), "我丢失的")
        mylist_pager.adapter = mylistPagerAdapter
        mylist_tabLayout.setupWithViewPager(mylist_pager)
        mylist_tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        mylist_tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#00a1e9"))
    }

    private fun initPopupwindow(view: View) {
        popupWindow = PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true)
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = false// 设置PopupWindow是否能响应外部点击事件
        popupWindow.isTouchable = true// 设置PopupWindow是否能响应点击事件
        popupWindow.showAsDropDown(imageView, Gravity.LEFT, 0, 30)
        popupWindow.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@MyListActivity, R.color.white_color)))
        bgAlpha(0.5f)
        popupWindow.setOnDismissListener {
            // popupWindow隐藏时恢复屏幕正常透明度
            bgAlpha(1f)
        }

        beiyangyuan = view.findViewById(R.id.tv_byy)
        weijinlu = view.findViewById(R.id.tv_wjl)

        //根据原始校区值，设置颜色，选中的校区为蓝色
        campus = Hawk.get("campus")
        if (campus == 1) {
            beiyangyuan.textColor = Color.parseColor("#4894d5")
            weijinlu.textColor = Color.parseColor("#656565")
        } else {
            weijinlu.textColor = Color.parseColor("#4894d5")
            beiyangyuan.textColor = Color.parseColor("#656565")
        }

        //设置弹窗中的textview的监听事件，修改校区，并修改数据库中的校区对应值
        beiyangyuan.setOnClickListener {
            Hawk.delete("campus")
            Hawk.put("campus", 1)
            beiyangyuan.textColor = Color.parseColor("#4894d5")
            weijinlu.textColor = Color.parseColor("#656565")
            popupWindow.dismiss()
        }
        weijinlu.setOnClickListener {
            Hawk.delete("campus")
            Hawk.put("campus", 2)
            weijinlu.textColor = Color.parseColor("#4894d5")
            beiyangyuan.textColor = Color.parseColor("#656565")
            popupWindow.dismiss()
        }
    }

    private fun bgAlpha(bgAlpha: Float) {
        //修改屏幕背景色
        val lp = window.attributes
        lp.alpha = bgAlpha //0.0-1.0
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.attributes = lp
    }
}
