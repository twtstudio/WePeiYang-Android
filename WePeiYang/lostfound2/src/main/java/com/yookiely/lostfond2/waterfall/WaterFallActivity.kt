package com.yookiely.lostfond2.waterfall

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.view.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.PopupWindow
import android.widget.TextView
import com.example.lostfond2.R
import com.example.lostfond2.R.id.waterfall_pager
import com.example.lostfond2.R.id.waterfall_tabLayout
import com.github.clans.fab.FloatingActionButton
import com.orhanobut.hawk.Hawk
import com.tencent.bugly.beta.global.e
import com.yookiely.lostfond2.mylist.MyListActivity
import com.yookiely.lostfond2.release.ReleaseActivity
import com.yookiely.lostfond2.search.SearchActivity
import kotlinx.android.synthetic.main.activity_water_fall.*
import org.jetbrains.anko.textColor
import java.lang.reflect.Field

class WaterFallActivity : AppCompatActivity() {

    private lateinit var lostFragment: WaterfallFragment
    private lateinit var foundFragment: WaterfallFragment
    private lateinit var pop_waterfall_type_recyclerview: RecyclerView
    private lateinit var pop_waterfall_types_all: TextView
    private lateinit var pop_waterfall_filter: TextView
    lateinit var window: PopupWindow
    private var layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    private var layoutManagerForFilter = LinearLayoutManager(this@WaterFallActivity)
    private var type = -1
    private var time = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_water_fall)
        val waterfallLost: FloatingActionButton = findViewById(R.id.waterfall_fab_lost)
        val popupWindowview: View = LayoutInflater.from(this).inflate(R.layout.lf_waterfall_cardview_types, null, false)
        pop_waterfall_types_all = popupWindowview.findViewById(R.id.waterfall_types_all) //全部分类
        pop_waterfall_filter = popupWindowview.findViewById(R.id.waterfall_filter) //筛选条件
        pop_waterfall_type_recyclerview = popupWindowview.findViewById(R.id.waterfall_type_recyclerview)
        pop_waterfall_type_recyclerview.layoutManager = layoutManager
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(pop_waterfall_type_recyclerview)


        if (!Hawk.contains("campus")) {
            val dialog = AlertDialog.Builder(this@WaterFallActivity)
                    .setTitle("同学选择一下校区呗～")
                    .setMessage("可以在“我的”修改嗷～")
                    .setCancelable(false)
                    .setPositiveButton("卫津路") { _, _ -> Hawk.put("campus", 2) }
                    .setNegativeButton("北洋园") { _, _ -> Hawk.put("campus", 1) }
                    .create()
            dialog.show()
            // 通过反射改变message颜色
            try {
                val mAlert: Field = AlertDialog::class.java.getDeclaredField("mAlert")
                mAlert.isAccessible = true
                val mAlertController = mAlert.get(dialog)
                val mMessage: Field = mAlertController.javaClass.getDeclaredField("mMessageView")
                mMessage.isAccessible = true
                val mMessageView = mMessage.get(mAlertController) as TextView
                mMessageView.setTextColor(Color.parseColor("#999999"))
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            }
        }

        lostFragment = WaterfallFragment.newInstance("lost")
        foundFragment = WaterfallFragment.newInstance("found")
        val waterfallPagerAdapter = WaterfallPagerAdapter(supportFragmentManager)
        val bundle = Bundle()
        val intent = Intent()
        val toolbar = findViewById<Toolbar>(R.id.toolbar1)
        waterfall_type_blue.visibility = View.GONE
        toolbar.title = "失物招领"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() } // toolbar的各种操作

        waterfallPagerAdapter.add(foundFragment, "捡到")
        waterfallPagerAdapter.add(lostFragment, "丢失")
        waterfall_pager.adapter = waterfallPagerAdapter

        val apply = waterfall_tabLayout.apply {
            setupWithViewPager(waterfall_pager)
            tabGravity = TabLayout.GRAVITY_FILL
            setSelectedTabIndicatorColor(Color.parseColor("#00a1e9"))
        }
        waterfall_type.setOnClickListener {
            if (waterfall_type_grey.visibility == View.VISIBLE) run {
                waterfall_type_blue.visibility = View.VISIBLE
                waterfall_type_grey.visibility = View.GONE

                window = PopupWindow(popupWindowview, WRAP_CONTENT, WRAP_CONTENT, true)
                window.apply {
                    setBackgroundDrawable(BitmapDrawable())
                    isOutsideTouchable = true
                    isTouchable = true
                    showAsDropDown(it)
                    setTouchInterceptor(View.OnTouchListener { v, event ->
                        if (event?.action == MotionEvent.ACTION_DOWN) {
                            waterfall_type_grey.visibility = View.VISIBLE
                            waterfall_type_blue.visibility = View.GONE
                        }
                        false
                    })
                }
            } else if (waterfall_type_blue.visibility == View.VISIBLE) {
                waterfall_type_grey.visibility = View.VISIBLE
                waterfall_type_blue.visibility = View.GONE
            }

            pop_waterfall_types_all.setOnClickListener {
                pop_waterfall_types_all.textColor = Color.parseColor("#666666")
                pop_waterfall_filter.textColor = Color.parseColor("#D3D3D3")
                pop_waterfall_type_recyclerview.layoutManager = layoutManager
                setWaterfallType(-1)
                waterfall_type_grey.visibility = View.VISIBLE
                waterfall_type_blue.visibility = View.GONE
            }

            pop_waterfall_filter.setOnClickListener {
                pop_waterfall_filter.textColor = Color.parseColor("#666666")
                pop_waterfall_types_all.textColor = Color.parseColor("#D3D3D3")
                pop_waterfall_type_recyclerview.apply {
                    layoutManager = layoutManagerForFilter
                    adapter = WaterfallFilterTableAdapter(this@WaterFallActivity, this@WaterFallActivity, -1)
                }
            }
        }

        waterfall_fab_found.setOnClickListener {
            bundle.putString("lostOrFound", "found")
            intent.putExtras(bundle)
            intent.setClass(this@WaterFallActivity, ReleaseActivity::class.java)
            startActivity(intent)
            waterfall_fab_menu.close(true)
        }

        waterfallLost.setOnClickListener {
            bundle.putString("lostOrFound", "lost")
            intent.putExtras(bundle)
            intent.setClass(this@WaterFallActivity, ReleaseActivity::class.java)
            startActivity(intent)
            waterfall_fab_menu.close(true)
        }
    }

    fun setWaterfallType(type: Int) {
        lostFragment.loadWaterfallDataWithCondition(type, time)
        foundFragment.loadWaterfallDataWithCondition(type, time)
        this.type = type
        pop_waterfall_type_recyclerview.adapter = WaterfallTypeTableAdapter(this, this, type)
        pop_waterfall_types_all.typeface = if (type == -1) {
            Typeface.defaultFromStyle(Typeface.BOLD)
        } else {
            Typeface.DEFAULT
        }
    }

    fun setWaterfallTime(time: Int) {
        lostFragment.loadWaterfallDataWithCondition(type, time)
        foundFragment.loadWaterfallDataWithCondition(type, time)
        this.time = time
        pop_waterfall_type_recyclerview.adapter = WaterfallFilterTableAdapter(this, this, time)
    }

    override fun onResume() {
        super.onResume()
        pop_waterfall_type_recyclerview.adapter = if (pop_waterfall_type_recyclerview.layoutManager == layoutManager) {
            WaterfallTypeTableAdapter(this, this, type)
        } else {
            WaterfallFilterTableAdapter(this, this, time)
        }

        if (type == -1) {
            pop_waterfall_types_all.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        } else {
            pop_waterfall_types_all.typeface = Typeface.DEFAULT
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.lf_waterfall_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val itemId = item!!.itemId
        waterfall_fab_menu.close(true)
        val intent = Intent()

        when (itemId) {
            R.id.waterfall_search -> intent.setClass(this@WaterFallActivity, SearchActivity::class.java)
            R.id.waterfall_indi -> intent.setClass(this@WaterFallActivity, MyListActivity::class.java)
        }

        startActivity(intent)
        return true
    }
}
