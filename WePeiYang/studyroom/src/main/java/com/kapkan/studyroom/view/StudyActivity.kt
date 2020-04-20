package com.kapkan.studyroom.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.studyroom.R
import com.kapkan.studyroom.service.ViewModel
import com.kapkan.studyroom.service.collectionLiveData
import kotlinx.android.synthetic.main.mycalendar.view.*
import kotlinx.android.synthetic.main.newstudy_activity.*
import kotlinx.android.synthetic.main.study_activity.study_home_back
import kotlinx.android.synthetic.main.study_activity.studyroom_refresh
import kotlinx.android.synthetic.main.window_collection.view.*
import kotlinx.android.synthetic.main.window_timeselect.view.*
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onItemClick
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class StudyActivity : AppCompatActivity() {
    val calendar = Calendar.getInstance()
    //校区默认是北洋园
    var ispeiyang: Boolean = true
    val defaultmap = HashMap<String, Any>()
    var course: BooleanArray = BooleanArray(12) { false }
    var Buildinglist: MutableList<Map<String, Any>> = MutableList(0) { defaultmap }
    var datelist: MutableList<Map<String, Any>> = MutableList(45) { defaultmap }
    var month = calendar.get(Calendar.MONTH) + 1 //居然是从0开始的
    var courseday = calendar.get(Calendar.DAY_OF_MONTH)
    var dayofweek = calendar.get(Calendar.DAY_OF_WEEK)
    var year = calendar.get(Calendar.YEAR)
    private val viewModel = ViewModel()
    var peiyangBuildinglist: MutableList<Map<String, Any>> = MutableList(0) { defaultmap }
    var weijinBuildinglist: MutableList<Map<String, Any>> = MutableList(0) { defaultmap }
    val PYBASENUM = 1093
    val WJBASENUM = 15   //0015
    val buildingaryp = arrayOf(R.drawable.icon_building, "55楼", "43楼", "50楼", "33楼(文学院)", "31楼", "32楼", "44楼", "33楼", "37楼", "45楼", "46楼")
    val buildingaryw = arrayOf(R.drawable.icon_building, "23楼", "12楼", "19楼", "26楼A", "26楼B")
    val wId = arrayOf("0015","0026","0032","1084","1085")
    val pId = arrayOf("1093","1094","1095","1096","1097","1098","1099","1101","1102","1103","1004")
    val REQUEST_TS = 1
    val ts = "2020-02-17"
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    var termStart: Long = simpleDateFormat.parse(ts).time
    var week: Int = 0
    var blank: Int = 0
    var days:Int = 30

    lateinit var popupWindow:PopupWindow
    lateinit var mcontext: Context
    lateinit var dateView: View
    lateinit var gridView: GridView
    lateinit var buildingView: GridView
    lateinit var simpleAdapter: SimpleAdapter
    lateinit var buildingAdapter: SimpleAdapter

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.newstudy_activity)
        init()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun init() {

        when{
          month < 10 && this.courseday < 10 -> caculateWeek(year.toString() + "-0" + month.toString() + "-0" + (this.courseday).toString())
          month < 10 && this.courseday >= 10 -> caculateWeek(year.toString() + "-0" + month.toString() + "-" + (this.courseday).toString())
          month >= 10 && this.courseday < 10 -> caculateWeek(year.toString() + "-" + month.toString() + "-0" + (this.courseday).toString())
          else -> caculateWeek(year.toString() + "-" + month.toString() + "-" + (this.courseday).toString())
        }
        viewModel.login()
        //viewModel.getCollections()
        //viewModel.getTermDate(this)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        dayofweek = calendar.get(Calendar.DAY_OF_WEEK)
        mcontext = this
        initBuilding()
        viewModel.getBuildingList()
        viewModel.getAvailableRoom(this.courseday+1, week)

        block_time.setOnClickListener {
            val selectwindow = LayoutInflater.from(this).inflate(R.layout.window_timeselect, null, false)
            val popupWindow = PopupWindow(selectwindow, 230, 200, false)
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
                intent.putExtra("class", course)
                startActivityForResult(intent, REQUEST_TS)
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
            refresh()
        }

        block_collection.setOnClickListener {
            val collectionWindow = LayoutInflater.from(this).inflate(R.layout.window_collection, null, false)
            val collectionrec = collectionWindow.collection_rec
            val popupWindow = PopupWindow(collectionWindow, 500, 1200, false)
            popupWindow.isOutsideTouchable = true
            popupWindow.isTouchable = true
            popupWindow.isOutsideTouchable = true
            popupWindow.showAtLocation(layoutInflater.inflate(R.layout.newstudy_activity, null), Gravity.RIGHT, 0, 0)
            if (collectionLiveData.value?.data!=null){
                collectionLiveData.value!!.data[0].classrooms.forEach { i ->

                }
            }

        }
        Buildinglist.addAll(peiyangBuildinglist)
        loadBuilding()
    }

    fun refresh() {
        Toast.makeText(this,"刷新中",Toast.LENGTH_SHORT).show()
        viewModel.getBuildingList()
        viewModel.getAvailableRoom(this.courseday+1, week)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun calendarInit() {
        viewModel.login()
        dateView = LayoutInflater.from(this).inflate(R.layout.mycalendar, null, false)
        gridView = dateView.gview
        popupWindow = PopupWindow(dateView, 900, 1000, true)
        popupWindow.isOutsideTouchable = true
        popupWindow.isTouchable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.showAtLocation(dateView, Gravity.CENTER, 0, 0)
        dateView.month.text = year.toString() + "年" + month.toString() + "月"
        dayofweek = calendar.get(Calendar.DAY_OF_WEEK)
        initDateList(month)
        dateView.month.text = year.toString() + "年" + month.toString() + "月"
        loadDate()

        dateView.backone.onClick {
            if (month == 1) {
                month = 12
                year--
            } else {
                month--
            }
            calendar.set(Calendar.MONTH, month - 1)
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            dayofweek = calendar.get(Calendar.DAY_OF_WEEK)
            initDateList(month)
            dateView.month.text = year.toString() + "年" + month.toString() + "月"
            loadDate()
        }

        dateView.forwardone.onClick {
            if (month == 12) {
                month = 1
                year++
            } else {
                month++
            }
            calendar.set(Calendar.MONTH, month - 1)
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            dayofweek = calendar.get(Calendar.DAY_OF_WEEK)
            initDateList(month)
            dateView.month.text = year.toString() + "年" + month.toString() + "月"
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
                mintent.putExtra("buildingID", pId[p2])
                mintent.putExtra("buildingName", buildingaryp[p2 + 1].toString())
            } else {
                mintent.putExtra("buildingID", wId[p2])
                mintent.putExtra("buildingName", buildingaryw[p2 + 1].toString())
            }
            mintent.putExtra("courseday", this@StudyActivity.courseday)
            mintent.putExtra("week",week)
            mintent.putExtra("course", course)
            mintent.putExtra("month",month)
            startActivity(mintent)
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun loadDate() {
        val from: Array<String> = Array(1) { "courseday" }
        val to = IntArray(1) { R.id.day }
        simpleAdapter = SimpleAdapter(this, datelist, R.layout.item_date, from, to)
        gridView.adapter = simpleAdapter
        gridView.onItemClick { p0, p1, p2, p3 ->
           // gridView.adapter.getView(p2,p1,p0).background = getDrawable(R.drawable.top)
            if (p2>=blank&&p2<days+blank){
                Toast.makeText(mcontext,"选中的时间："+year.toString() + "-" + month.toString() + "-" + (p2 - blank+1).toString(),Toast.LENGTH_SHORT).show()
                when {
                    month < 10 && p2 - blank < 10 -> caculateWeek(year.toString() + "-0" + month.toString() + "-0" + (p2 - blank+1).toString())
                    month < 10 && p2 - blank >= 10 -> caculateWeek(year.toString() + "-0" + month.toString() + "-" + (p2 - blank+1).toString())
                    month >= 10 && p2 - blank < 10 -> caculateWeek(year.toString() + "-" + month.toString() + "-0" + (p2 - blank+1).toString())
                    else -> caculateWeek(year.toString() + "-" + month.toString() + "-" + (p2 - blank+1).toString())
                }
                val linearLayout:LinearLayout = gridView.getChildAt(p2) as LinearLayout
                linearLayout.background = getDrawable(R.drawable.classroom_selected)
                popupWindow.dismiss()
                refreshAList()
            }
        }
    }

    fun initDateList(month: Int) {
        days = getMonthLastDay(year, month)
        if (dayofweek - 1 == 0) {
            dayofweek = 7
        }
        blank = dayofweek - 1
        for (i in 0..41) {
            val map: HashMap<String, String> = HashMap()
            if (i < blank) {
                map.put("courseday", "")
                datelist[i] = map
            } else if (i < blank + days) {
                map.put("courseday", (i - blank + 1).toString())
                datelist[i] = map
            } else {
                map.put("courseday", "")
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
        if (requestCode == REQUEST_TS && resultCode == Activity.RESULT_OK) {
            val select = data!!.getBooleanArrayExtra("class")
            for (i in 0..11) {
                course[i] = select[i]
            }
        }
    }

    fun initBuilding() {
        val strary: Array<String> = arrayOf("img_building", "text_building")
        for (i in 1..11) {
            val map: HashMap<String, Any> = HashMap()
            map[strary[0]] = buildingaryp[0]
            map[strary[1]] = buildingaryp[i]
            peiyangBuildinglist.add(map)
        }

        for (i in 1..5) {
            val map: HashMap<String, Any> = HashMap()
            map[strary[0]] = buildingaryw[0]
            map[strary[1]] = buildingaryw[i]
            weijinBuildinglist.add(map)
        }
    }

    fun caculateWeek(sdate: String) {
        val selectedtime: Long = simpleDateFormat.parse(sdate).time
        val days: Int = ((selectedtime - termStart) / (1000 * 60 * 60 * 24)).toInt()
        week = days / 7
        this.courseday = days % 7
    }

    fun gotDate(date: Long) {
        termStart = date
    }

    fun refreshAList(){
        
        viewModel.getAvailableRoom(this.courseday, week)
    }
}