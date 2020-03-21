package com.kapkan.studyroom.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.studyroom.R
import com.kapkan.studyroom.items.Building
import com.kapkan.studyroom.service.BuildingListData
import com.kapkan.studyroom.service.ViewModel
import kotlinx.android.synthetic.main.mycalendar.view.*
import kotlinx.android.synthetic.main.newstudy_activity.*
import kotlinx.android.synthetic.main.study_activity.study_home_back
import kotlinx.android.synthetic.main.study_activity.studyroom_refresh
import kotlinx.android.synthetic.main.window_collection.view.*
import kotlinx.android.synthetic.main.window_timeselect.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onItemClick
import java.util.*
import kotlin.collections.HashMap

class StudyActivity : AppCompatActivity() {
    val calendar = Calendar.getInstance()
    //校区默认是北洋园
    var ispeiyang: Boolean = true
    val defaultmap = HashMap<String, Any>()
    var course:BooleanArray = BooleanArray(12){false}
    var Buildinglist: MutableList<Map<String, Any>> = MutableList(0) { defaultmap }
    var datelist: MutableList<Map<String, Any>> = MutableList(45) { defaultmap }
    var month = calendar.get(Calendar.MONTH) + 1 //居然是从0开始的
    var day = calendar.get(Calendar.DAY_OF_MONTH)
    var dayofweek = calendar.get(Calendar.DAY_OF_WEEK)
    var year = calendar.get(Calendar.YEAR)
    private val viewModel = ViewModel()
    var peiyangBuildinglist: MutableList<Map<String, Any>> = MutableList(0) { defaultmap }
    var weijinBuildinglist: MutableList<Map<String, Any>> = MutableList(0) { defaultmap }
    val PYBASENUM = 1093
    val WJBASENUM = 15   //0015
    val buildingaryp = arrayOf(R.drawable.icon_building,"55楼","43楼","50楼","33楼(文学院)","31楼", "32楼", "44楼", "45、46楼", "33楼","37楼")
    val buildingaryw = arrayOf(R.drawable.icon_building,"23楼","12楼","19楼","26楼A","26楼B")
    val REQUEST_TS = 1
    var termStart:Long = 0



    lateinit var mcontext: Context
    lateinit var popupWindow: PopupWindow
    lateinit var dateView: View
    lateinit var gridView: GridView
    lateinit var buildingView: GridView
    lateinit var simpleAdapter: SimpleAdapter
    lateinit var buildingAdapter: SimpleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.newstudy_activity)
        init()
    }

    fun init() {
        viewModel.login()
        viewModel.getTermDate(this)
        calendar.set(Calendar.DAY_OF_MONTH,1)
        dayofweek = calendar.get(Calendar.DAY_OF_WEEK)
        mcontext = this
        initBuilding()
        viewModel.getBuildingList()
        viewModel.getAvailableRoom(1,3)

        block_time.setOnClickListener {
            val selectwindow = LayoutInflater.from(this).inflate(R.layout.window_timeselect, null, false)
            val popupWindow = PopupWindow(selectwindow,230,200,false)
            popupWindow.isOutsideTouchable = true
            popupWindow.isTouchable = true
            popupWindow.isOutsideTouchable = true
            val dateselect = selectwindow.date_select
            val classselect = selectwindow.class_select
            dateselect.onClick {
                calendarInit()
                popupWindow.dismiss()
            }
            classselect.onClick {
                //选择课程时间
                val intent = Intent(mcontext, TimeSelcetActivity::class.java)
                intent.putExtra("class",course)
                startActivityForResult(intent,REQUEST_TS)
                popupWindow.dismiss()
            }
            popupWindow.showAsDropDown(block_time)
        }

        block_area.setOnClickListener {
            if (ispeiyang) {
                area.text = "卫津路"
                Buildinglist = weijinBuildinglist
                loadBuilding()
                ispeiyang = false
            } else {
                area.text = "北洋园"
                Buildinglist = peiyangBuildinglist
                loadBuilding()
                ispeiyang = true
            }
        }

        study_home_back.setOnClickListener {
            onBackPressed()
        }

        studyroom_refresh.setOnClickListener {
            viewModel.getBuildingList()
            //viewModel.getAvailableRoom()
            viewModel.getCollections()
        }

        block_collection.onClick {
            val collectionWindow= LayoutInflater.from(mcontext).inflate(R.layout.window_collection,null,false)
            val collectionrec = collectionWindow.collection_rec


        }

        Buildinglist.addAll(peiyangBuildinglist)
        loadBuilding()
    }

    fun refresh() {

    }


    fun calendarInit() {
        dateView = LayoutInflater.from(this).inflate(R.layout.mycalendar, null, false)
        gridView = dateView.gview
        val popupWindow = PopupWindow(dateView, 900, 1000, true)
        popupWindow.isOutsideTouchable = true
        popupWindow.isTouchable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.showAtLocation(dateView, Gravity.CENTER, 0, 0)

        dateView.month.text = year.toString()+"年"+ month.toString() + "月"
        dayofweek = calendar.get(Calendar.DAY_OF_WEEK)
        initDateList(month)
        dateView.month.text = year.toString()+"年"+ month.toString() + "月"
        loadDate()

        dateView.backone.onClick {
            if (month == 1) {
                month = 12
                year--
            } else {
                month--
            }
            calendar.set(Calendar.MONTH, month-1)
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.DAY_OF_MONTH,1)
            dayofweek = calendar.get(Calendar.DAY_OF_WEEK)
            initDateList(month)
            dateView.month.text =year.toString()+"年"+ month.toString() + "月"
            loadDate()
        }

        dateView.forwardone.onClick {
            if (month == 12) {
                month = 1
                year++
            } else {
                month++
            }
            calendar.set(Calendar.MONTH, month-1)
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.DAY_OF_MONTH,1)
            dayofweek = calendar.get(Calendar.DAY_OF_WEEK)
            initDateList(month)
            dateView.month.text =year.toString()+"年"+ month.toString() + "月"
            loadDate()
        }
    }

    fun loadBuilding() {
        buildingView = buildings
        val from: Array<String> = Array(2) { "img_building" }
        from[1] = "text_building"
        val to = IntArray(2) { R.id.img_building }
        to[1] = R.id.text_building
        buildingAdapter = SimpleAdapter(this, Buildinglist, R.layout.item_building, from, to)
        buildingView.adapter = buildingAdapter
        buildingView.onItemClick { p0, p1, p2, p3 ->
            val mintent = Intent(mcontext, BuildingListActivity::class.java)
            if (ispeiyang) {
                mintent.putExtra("buildingID", p2 + PYBASENUM)
                mintent.putExtra("buildingName", buildingaryp[p2+1].toString())
            } else {
                mintent.putExtra("buildingID", p2 + PYBASENUM)
                mintent.putExtra("buildingName", buildingaryw[p2+1].toString())
            }
            mintent.putExtra("course",course)
            startActivity(mintent)
        }
    }

    fun loadDate() {
        val from: Array<String> = Array(1) { "day" }
        val to = IntArray(1) { R.id.day }
        simpleAdapter = SimpleAdapter(this, datelist, R.layout.item_date, from, to)
        gridView.adapter = simpleAdapter
    }

    fun initDateList(month: Int) {

        val days = getMonthLastDay(year, month)
        if (dayofweek-1==0){dayofweek=7}
        val blank = dayofweek - 1
        for (i in 0..41) {
            val map: HashMap<String, String> = HashMap()
            if (i < blank) {
                map.put("day", "")
                datelist[i] = map
            } else if (i < blank + days) {
                map.put("day", (i - blank+1).toString())
                datelist[i] = map
            } else {
                map.put("day", "")
                datelist[i] = map
            }
        }
    }

    fun getMonthLastDay(year: Int, month: Int): Int {
        val a = Calendar.getInstance()
        a.set(Calendar.YEAR, year)
        a.set(Calendar.MONTH, month - 1)
        a.set(Calendar.DATE, 1)
        a.roll(Calendar.DATE, -1)
        return a.get(Calendar.DATE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==REQUEST_TS&&resultCode== Activity.RESULT_OK){
            val select  = data!!.getBooleanArrayExtra("class")
            for (i in 0 .. 11){
                course[i] = select[i]
            }
        }
    }

    fun initBuilding() {
        val strary:Array<String> = arrayOf("img_building","text_building")
        for(i in 1 .. 10){
            val map: HashMap<String, Any> = HashMap()
            map[strary[0]] = buildingaryp[0]
            map[strary[1]] = buildingaryp[i]
            peiyangBuildinglist.add(map)
        }
        for (i in 1 .. 5){
            val map: HashMap<String, Any> = HashMap()
            map[strary[0]] = buildingaryw[0]
            map[strary[1]] = buildingaryw[i]
            weijinBuildinglist.add(map)
        }
    }

    fun gotDate(date:Long){
        termStart = date
    }
}