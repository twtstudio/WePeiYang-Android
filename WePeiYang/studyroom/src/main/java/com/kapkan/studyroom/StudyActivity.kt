package com.example.studyroom

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.studyroom.service.BuildingList
import com.example.studyroom.service.BuildingListData
import com.example.studyroom.service.StudyServiceManager
import com.example.studyroom.service.ViewModel
import kotlinx.android.synthetic.main.mycalendar.*
import kotlinx.android.synthetic.main.mycalendar.view.*
import kotlinx.android.synthetic.main.mycalendar.view.confirm
import kotlinx.android.synthetic.main.newstudy_activity.*
import kotlinx.android.synthetic.main.study_activity.*
import kotlinx.android.synthetic.main.study_activity.study_home_back
import kotlinx.android.synthetic.main.study_activity.studyroom_refresh
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class StudyActivity : AppCompatActivity() {
    val calendar = Calendar.getInstance()
    //校区默认是北洋园
    var ispeiyang: Boolean = true
    val defaultmap = HashMap<String,Any>()
    var courses = Array(6) { false }
    var Buildinglist:MutableList<Map<String, Any>> = MutableList(20) {defaultmap}
    var datelist:MutableList<Map<String, Any>> = MutableList(45) {defaultmap}
    var month = calendar.get(Calendar.MONTH)+1 //居然是从0开始的
    var day = calendar.get(Calendar.DAY_OF_MONTH)
    var dayofweek = calendar.get(Calendar.DAY_OF_WEEK)
    var year = calendar.get(Calendar.YEAR)
    private val viewModel = ViewModel()

    lateinit var popupWindow: PopupWindow
    lateinit var dateView: View
    lateinit var gridView: GridView
    lateinit var simpleAdapter: SimpleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.newstudy_activity)
        init()
        BuildingListData.value
    }

    fun init() {
        viewModel.getBuildingList()
        block_time.setOnClickListener {
            dateView = LayoutInflater.from(this).inflate(R.layout.mycalendar, null, false)
            gridView = dateView.gview
            popupWindow = PopupWindow(dateView, 900, 1000, true)
            popupWindow.isOutsideTouchable = true
            popupWindow.isTouchable = true
            popupWindow.isOutsideTouchable = true
            popupWindow.showAtLocation(dateView, Gravity.CENTER,0,0)
            calendarInit(dateView)
        }

        block_area.setOnClickListener {
            if (ispeiyang) {
                current_camp.text = "当前校区：卫津路"
                peiyang_buildings.visibility = View.GONE
                weijin_buildings.visibility = View.VISIBLE
                ispeiyang =false
            } else {
                current_camp.text = "当前校区：北洋园"
                weijin_buildings.visibility = View.GONE
                peiyang_buildings.visibility = View.VISIBLE
                ispeiyang = true
            }
        }

        study_home_back.setOnClickListener {
            onBackPressed()
        }

        studyroom_refresh.setOnClickListener {

        }
    }

    fun refresh() {

    }

    fun search() {

    }

    fun calendarInit(dateview:View){

        dateview.confirm.onClick {
            popupWindow.dismiss()
        }
        dateview.month.text = month.toString()+"月"
        dayofweek = calendar.get(Calendar.DAY_OF_WEEK)
        initDateList(month)
        dateview.month.text = month.toString()+"月"
        loadDate()

        dateview.backone.onClick {
            if (month==1){
                month = 12
                year--
            }else{
                month--
            }
            calendar.set(Calendar.MONTH,month)
            calendar.set(Calendar.YEAR,year)
            dayofweek = calendar.get(Calendar.DAY_OF_WEEK)
            initDateList(month)
            dateview.month.text = month.toString()+"月"
            loadDate()
        }

        dateview.forwardone.onClick {
            if (month==12){
                month = 1
                year++
            }else{
                month++
            }
            calendar.set(Calendar.MONTH,month)
            calendar.set(Calendar.YEAR,year)
            dayofweek = calendar.get(Calendar.DAY_OF_WEEK)
            initDateList(month)
            dateview.month.text = month.toString()+"月"
            loadDate()
        }
    }



    fun loadBuilding(){

    }

    fun loadDate(){
        val from:Array<String> = Array(1) {"day"}
        val to:IntArray = IntArray(1){R.id.day}
        simpleAdapter = SimpleAdapter(this,datelist,R.layout.item_date,from,to)
        gridView.adapter = simpleAdapter
    }

    fun initDateList(month:Int){
        val days = getMonthLastDay(year, month)
        val blank = dayofweek-1
        for (i in 0 .. 41){
            val map :HashMap<String,String> = HashMap()
            if (i<=blank){
                map.put("day","")
                datelist[i] = map
            }else if (i<=blank+days){
                map.put("day",(i - blank).toString())
                datelist[i] = map
            }else {
                map.put("day","")
                datelist[i] = map
            }
        }
    }

    fun getMonthLastDay(year:Int,month: Int):Int{
        val a = Calendar.getInstance()
        a.set(Calendar.YEAR, year)
        a.set(Calendar.MONTH, month - 1)
        a.set(Calendar.DATE, 1)
        a.roll(Calendar.DATE, -1)
        return a.get(Calendar.DATE)
    }

}