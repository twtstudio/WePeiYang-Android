package com.yookiely.lostfond2.waterfall

import android.annotation.SuppressLint
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
import com.github.clans.fab.FloatingActionButton
import com.orhanobut.hawk.Hawk
import com.yookiely.lostfond2.mylist.MyListActivity
import com.yookiely.lostfond2.release.ReleaseActivity
import com.yookiely.lostfond2.search.SearchActivity
import com.yookiely.lostfond2.service.Utils
import kotlinx.android.synthetic.main.lf2_activity_water_fall.*
import org.jetbrains.anko.textColor
import java.lang.reflect.Field

class WaterfallActivity : AppCompatActivity() {

    private lateinit var lostFragment: WaterfallFragment
    private lateinit var foundFragment: WaterfallFragment
    private lateinit var popWaterfallRecyclerView: RecyclerView
    private lateinit var popWaterfallTypesAll: TextView
    private var campus: Int = 0
    lateinit var popWaterfallFilter: TextView
    lateinit var window: PopupWindow
    private var layoutManagerForType = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    private var layoutManagerForTime = LinearLayoutManager(this@WaterfallActivity)
    private var type = Utils.ALL_TYPE
    private var time = Utils.ALL_TIME

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.lf2_activity_water_fall)
        val waterfallLost: FloatingActionButton = findViewById(R.id.waterfall_fab_lost)
        val popupWindowView: View = LayoutInflater.from(this).inflate(R.layout.lf2_waterfall_cardview_types, null, false)
        val snapHelper = LinearSnapHelper()
        popWaterfallTypesAll = popupWindowView.findViewById(R.id.waterfall_types_all) //全部分类
        popWaterfallFilter = popupWindowView.findViewById(R.id.waterfall_filter) //筛选条件
        popWaterfallRecyclerView = popupWindowView.findViewById(R.id.waterfall_type_recyclerview)
        popWaterfallRecyclerView.apply {
            layoutManager = layoutManagerForType
            adapter = WaterfallTypeTableAdapter(this@WaterfallActivity, this@WaterfallActivity, type)
            snapHelper.attachToRecyclerView(this)
        }

        if (!Hawk.contains("campus")) {
            showDialog()
        } else {
            campus = Hawk.get("campus")
        }

        lostFragment = WaterfallFragment.newInstance("lost")
        foundFragment = WaterfallFragment.newInstance("found")
        val waterfallPagerAdapter = WaterfallPagerAdapter(supportFragmentManager)
        val bundle = Bundle()
        val intent = Intent()
        val toolbar = findViewById<Toolbar>(R.id.toolbar1)
        waterfall_type_blue.visibility = View.GONE
        toolbar.apply {
            title = "失物招领"
            setSupportActionBar(this)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }

        waterfallPagerAdapter.add(foundFragment, "捡到")
        waterfallPagerAdapter.add(lostFragment, "丢失")
        waterfall_pager.adapter = waterfallPagerAdapter

        waterfall_tabLayout.apply {
            setupWithViewPager(waterfall_pager)
            tabGravity = TabLayout.GRAVITY_FILL
            setSelectedTabIndicatorColor(Color.parseColor("#00a1e9"))
        }
        waterfall_type.setOnClickListener { it ->
            if (waterfall_type_grey.visibility == View.VISIBLE) run {
                waterfall_type_blue.visibility = View.VISIBLE
                waterfall_type_grey.visibility = View.GONE

                window = PopupWindow(popupWindowView, WRAP_CONTENT, WRAP_CONTENT, true)
                window.apply {
                    setBackgroundDrawable(BitmapDrawable())
                    isOutsideTouchable = true
                    isTouchable = true
                    isFocusable = true
                    showAsDropDown(it)
                    setOnDismissListener {
                        waterfall_type_grey.visibility = View.VISIBLE
                        waterfall_type_blue.visibility = View.GONE
                    }
                }
            } else if (waterfall_type_blue.visibility == View.VISIBLE) {
                waterfall_type_grey.visibility = View.VISIBLE
                waterfall_type_blue.visibility = View.GONE
            }

            popWaterfallTypesAll.typeface = Typeface.DEFAULT
            popWaterfallTypesAll.setOnClickListener {
                popWaterfallTypesAll.textColor = Color.parseColor("#666666")
                popWaterfallFilter.textColor = Color.parseColor("#D3D3D3")
                popWaterfallRecyclerView.layoutManager = layoutManagerForType
                popWaterfallRecyclerView.adapter = WaterfallTypeTableAdapter(this, this, type)
            }

            popWaterfallFilter.setOnClickListener {
                popWaterfallFilter.textColor = Color.parseColor("#666666")
                popWaterfallTypesAll.textColor = Color.parseColor("#D3D3D3")
                popWaterfallRecyclerView.apply {
                    layoutManager = layoutManagerForTime
                    adapter = WaterfallTimeTableAdapter(this@WaterfallActivity, this@WaterfallActivity, time)
                }
            }
        }

        waterfall_fab_found.setOnClickListener {
            bundle.putString("lostOrFound", "found")
            intent.putExtras(bundle)
            intent.setClass(this@WaterfallActivity, ReleaseActivity::class.java)
            startActivity(intent)
            waterfall_fab_menu.close(true)
        }

        waterfallLost.setOnClickListener {
            bundle.putString("lostOrFound", "lost")
            intent.putExtras(bundle)
            intent.setClass(this@WaterfallActivity, ReleaseActivity::class.java)
            startActivity(intent)
            waterfall_fab_menu.close(true)
        }
    }

    fun setWaterfallType(type: Int) {
        lostFragment.loadWaterfallDataWithCondition(type, time)
        foundFragment.loadWaterfallDataWithCondition(type, time)
        this.type = type
        popWaterfallRecyclerView.adapter = WaterfallTypeTableAdapter(this, this, type)
    }

    fun setWaterfallTime(time: Int) {
        lostFragment.loadWaterfallDataWithCondition(type, time)
        foundFragment.loadWaterfallDataWithCondition(type, time)
        this.time = time
        popWaterfallRecyclerView.adapter = WaterfallTimeTableAdapter(this, this, time)
    }

    override fun onResume() {
        super.onResume()

        if (Hawk.contains("campus")) {
            if (campus == Hawk.get("campus")) {
                popWaterfallRecyclerView.adapter = if (popWaterfallRecyclerView.layoutManager == layoutManagerForType) {
                    WaterfallTypeTableAdapter(this, this, type)
                } else {
                    WaterfallTimeTableAdapter(this, this, time)
                }
            } else {
                this.type = Utils.ALL_TYPE
                this.time = Utils.ALL_TIME
                campus = Hawk.get("campus")
                popWaterfallRecyclerView.layoutManager = layoutManagerForType
                popWaterfallRecyclerView.adapter = WaterfallTypeTableAdapter(this, this, type)
            }
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
            R.id.waterfall_search -> intent.setClass(this@WaterfallActivity, SearchActivity::class.java)
            R.id.waterfall_indi -> intent.setClass(this@WaterfallActivity, MyListActivity::class.java)
        }

        startActivity(intent)
        return true
    }

    private fun showDialog() {
        val dialog = AlertDialog.Builder(this@WaterfallActivity)
                .setTitle("同学选择一下校区呗～")
                .setMessage("可以在“我的”修改嗷～")
                .setCancelable(false)
                .setPositiveButton("卫津路") { _, _ ->
                    Hawk.put("campus", 2)
                    campus = Hawk.get("campus")
                    lostFragment.loadWaterfallDataWithCondition(type, time)
                    foundFragment.loadWaterfallDataWithCondition(type, time)
                }
                .setNegativeButton("北洋园") { _, _ ->
                    Hawk.put("campus", 1)
                    campus = Hawk.get("campus")
                    lostFragment.loadWaterfallDataWithCondition(type, time)
                    foundFragment.loadWaterfallDataWithCondition(type, time)
                }
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
}
