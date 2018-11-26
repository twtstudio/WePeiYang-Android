package com.tjuwhy.yellowpages2.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import android.widget.TextView
import com.tjuwhy.yellowpages2.R
import com.tjuwhy.yellowpages2.service.Unit1
import com.tjuwhy.yellowpages2.service.YellowPagePreference
import com.tjuwhy.yellowpages2.utils.FIRST_INDEX_KEY
import com.tjuwhy.yellowpages2.utils.SECOND_INDEX_KEY
import com.twt.wepeiyang.commons.ui.rec.withItems

class DepartmentActivity : AppCompatActivity() {

    lateinit var title: String
    private lateinit var unitList: List<Unit1>
    private lateinit var arrowBack: ImageView
    private lateinit var departmentTv: TextView
    lateinit var recyclerView: RecyclerView
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.yp2_activity_department)

        toolbar = findViewById(R.id.toolbar)
        arrowBack = findViewById(R.id.department_arrow_back)
        departmentTv = findViewById(R.id.department_name)
        recyclerView = findViewById(R.id.recycler_view_department)

        val firstIndex = intent.getIntExtra(FIRST_INDEX_KEY, 0)
        val secondIndex = intent.getIntExtra(SECOND_INDEX_KEY, 0)

        YellowPagePreference.phoneBean?.apply {
            title = category_list[firstIndex].department_list[secondIndex].department_name
            unitList = category_list[firstIndex].department_list[secondIndex].unit_list
        }
        departmentTv.text = title
        arrowBack.setOnClickListener {
            onBackPressed()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.withItems(unitList.map { unit ->
            var flag = false
            YellowPagePreference.collectionList.forEach {
                if (it.thirdId == unit.id) {
                    flag = true
                }
            }
            ChildItem(this, unit.item_name, unit.item_phone, flag, unit.id)
        })
    }
}
