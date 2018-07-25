package com.example.lostfond2.waterfall

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.support.design.widget.TabLayout
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.PopupWindow
import com.example.lostfond2.R
import com.example.lostfond2.R.attr.tabGravity
import com.example.lostfond2.R.id.*
import com.example.lostfond2.mylist.MyListActivity
import com.example.lostfond2.release.ReleaseActivity
import com.example.lostfond2.search.SearchActivity
import com.github.clans.fab.FloatingActionButton
import kotlinx.android.synthetic.main.activity_water_fall.*
import kotlinx.android.synthetic.main.lf_waterfall_cardview_types.*

class WaterFallActivity : AppCompatActivity() {

    lateinit var lostFragment: WaterfallFragment
    lateinit var foundFragment: WaterfallFragment

    var layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    var type = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_water_fall)
        val waterfallLost: FloatingActionButton = findViewById(R.id.waterfall_fab_lost)

        lostFragment = WaterfallFragment.Companion.newInstance("lost")
        foundFragment = WaterfallFragment.Companion.newInstance("found")
        val waterfallPagerAdapter = WaterfallPagerAdapter(supportFragmentManager)
        val bundle = Bundle()
        val intent = Intent()
        val toolbar = findViewById<Toolbar>(R.id.toolbar1)
        waterfallPagerAdapter.add(lostFragment, "丢失")
        waterfallPagerAdapter.add(foundFragment, "捡到")
        waterfall_pager.adapter = waterfallPagerAdapter
        val apply = waterfall_tabLayout.apply {
            setupWithViewPager(waterfall_pager)
            tabGravity = TabLayout.GRAVITY_FILL
            setSelectedTabIndicatorColor(Color.parseColor("#00a1e9"))
        }
        waterfall_type_recyclerview.layoutManager = layoutManager
        setWaterfallType(0)//加载popupwindow布局
        waterfall_type_blue.visibility = View.GONE
//        waterfall_cardview_types.visibility = View.GONE
        waterfall_cover.visibility = View.GONE

        toolbar.title = "失物招领"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        waterfall_type.setOnClickListener {
            if (waterfall_type_grey.visibility == View.VISIBLE) run {
                waterfall_type_blue.visibility = View.VISIBLE
                waterfall_type_grey.visibility = View.GONE
                waterfall_cover.visibility = View.VISIBLE
            } else if (waterfall_type_blue.visibility == View.VISIBLE) {
                waterfall_type_grey.visibility = View.VISIBLE
                waterfall_type_blue.visibility = View.GONE
                waterfall_cover.visibility = View.GONE

                val newContentView = LayoutInflater.from(this@WaterFallActivity).inflate(R.layout.lf_waterfall_cardview_types, null, false)
                val window = PopupWindow().apply {
                    contentView = newContentView
                    width = 168
                    height = 100
                    isFocusable = true
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    isOutsideTouchable = true
                    isTouchable = true
                    showAsDropDown(it, 100, 100, 20)
                }
            }


            waterfall_types_all.setOnClickListener {
                setWaterfallType(-1)
            }

            waterfall_cover.setOnClickListener {
                waterfall_cover.visibility = View.GONE
                waterfall_type_grey.visibility = View.VISIBLE
                waterfall_type_blue.visibility = View.GONE
                waterfall_fab_menu.close(true)
            }
//        setToolbarMenuClickEvent()
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
        lostFragment.loadWaterfallDataWithType(type)
        foundFragment.loadWaterfallDataWithType(type)
        waterfall_type_recyclerview.adapter = WaterfallTypeTableAdapter(this, this, type)
        waterfall_types_all.apply {
            if (type == -1) {
                setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
            } else {
                setTypeface(Typeface.DEFAULT)
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        waterfall_cardview_types.visibility = View.GONE
        waterfall_type_recyclerview.adapter = WaterfallTypeTableAdapter(this, this, type)

        if (type == -1) {
            waterfall_types_all.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        } else {
            waterfall_types_all.typeface = Typeface.DEFAULT
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
            R.id.waterfall_search -> {
                intent.setClass(this@WaterFallActivity, SearchActivity::class.java)
            }
            R.id.waterfall_indi -> {
                intent.setClass(this@WaterFallActivity, MyListActivity::class.java)
            }
        }

        startActivity(intent)
        return true
    }
}
