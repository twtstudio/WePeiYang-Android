package com.kapkan.studyroom.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.WindowManager
import android.widget.Toast
import com.example.studyroom.R
import com.kapkan.studyroom.items.Flooritem
import com.kapkan.studyroom.service.*
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import kotlinx.android.synthetic.main.activity_buildingdetails.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.ArrayList

class BuildingListActivity: AppCompatActivity() {

    val roomManager = RoomManager()
    var itemList:ArrayList<Item> = ArrayList()
    //查询列表
    val viewModel = ViewModel()
    val defaultmap = HashMap<String,Any>()
    var buildingID:String = ""
    var buildingName:String = ""
    var month:Int = 1
    var select:BooleanArray = BooleanArray(12){false}
    var selectstr:String = "当前选中时间:"
    var dayofweek = 0
    var day:Int = 0
    var week:Int = 0
    var classrooms:ArrayList<Classroom> = ArrayList()
    var aclassrooms:ArrayList<Classroom> = ArrayList()
    lateinit var recyclerView: RecyclerView
    lateinit var itemManager: ItemManager
    lateinit var array:BooleanArray
    val context:Context = this

    companion object data{
        val Buildings = BuildingListData
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buildingdetails)
        current_building.text = buildingName
        buildingID =  intent.getStringExtra("buildingID").toString()
        select = intent.getBooleanArrayExtra("course")
        day = intent.getIntExtra("day",0)
        week = intent.getIntExtra("week",0)
        month = intent.getIntExtra("month",0)
        buildingName = intent.getStringExtra("buildingName")
        array = intent.getBooleanArrayExtra("courseselect")
        dayofweek = intent.getIntExtra("dayofweek",1)
        val window = this.window
        val color = Color.parseColor("#6B9DA3")
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(color)
        init()
    }

    fun init(){

        current_building.text = buildingName
        for (i in 0 .. 11){
            if (select[i]){
                val j = i+1
                selectstr += "$j,"
            }
        }

        selectstr = selectstr.substring(0,selectstr.length-1) + "节"
        buildings_time.text = selectstr

        study__back.onClick {
            onBackPressed()
        }

        recyclerView = findViewById(R.id.buildings_rec)
        recyclerView.layoutManager = LinearLayoutManager(this)
        itemManager = ItemManager()
        recyclerView.adapter = ItemAdapter(itemManager)

        if (BuildingListData.value?.data!=null){
            BuildingListData.value?.data!!.forEach {
                if (it.building_id == buildingID){
                    classrooms.addAll(it.classrooms)
                }
            }
        roomManager.getAvailableRoomList(viewModel,array,dayofweek,week,classrooms,this)
            /*判断教室是否可用（待修改
            */
        }else{
            Toast.makeText(this,"网络错误",Toast.LENGTH_SHORT).show()
        }
    }

    fun receiveArooms(aclassrooms:ArrayList<Classroom>){
        this.aclassrooms = aclassrooms
        itemManager.clear()
        judgeFloor()
        itemManager.addAll(itemList)
        recyclerView.adapter.notifyDataSetChanged()
    }

    fun judgeFloor(){
        val firstfloorlist = ArrayList<Classroom>()
        val firstalist = ArrayList<Classroom>()
        val secondfloorlist = ArrayList<Classroom>()
        val secondalist = ArrayList<Classroom>()
        val thirdfloorlist = ArrayList<Classroom>()
        val thirdalist = ArrayList<Classroom>()
        val fourthfloorlist = ArrayList<Classroom>()
        val fourthalist = ArrayList<Classroom>()
        val fifthfloorlist = ArrayList<Classroom>()
        val fifthalist = ArrayList<Classroom>()


            classrooms.forEach {
                when{
                    it.classroom_id.substring(2,3) == "1" -> {
                        firstfloorlist.add(it)
                        if (aclassrooms.contains(it)){
                            firstalist.add(it)
                        }
                    }
                    it.classroom_id.substring(2,3) == "2" -> {
                        secondfloorlist.add(it)
                        if (aclassrooms.contains(it)){
                            secondalist.add(it)
                        }
                    }
                    it.classroom_id.substring(2,3) == "3" ->  {
                    thirdfloorlist.add(it)
                    if (aclassrooms.contains(it)){
                        thirdalist.add(it)
                    }
                }
                    it.classroom_id.substring(2,3) == "4" -> {
                        fourthfloorlist.add(it)
                        if (aclassrooms.contains(it)){
                            fourthalist.add(it)
                        }
                    }
                    it.classroom_id.substring(2,3) == "5" -> {
                        fifthfloorlist.add(it)
                        if (aclassrooms.contains(it)){
                            fifthalist.add(it)
                        }
                    }
                    else -> ""
                }
            }
        for (i in 1..5){
            when (i) {
                1 -> loadByFloor("一楼",firstfloorlist,firstalist)
                2 -> loadByFloor("二楼",secondfloorlist,secondalist)
                3 -> loadByFloor("三楼",thirdfloorlist,thirdalist)
                4 -> loadByFloor("四楼",fourthfloorlist,fourthalist)
                5 -> loadByFloor("五楼",fifthfloorlist,fifthalist)
            }
        }
    }

    fun loadByFloor(floor:String,fclassrooms:ArrayList<Classroom>,faclassrooms:ArrayList<Classroom>){
        //加载一层楼的数据
        val size:ArrayList<String> =ArrayList()
        val classroomId:ArrayList<String> =ArrayList()
        val defaultmap = HashMap<String, Any>()
        val posList:ArrayList<Int> = ArrayList()
        val sizeList:MutableList<Map<String, Any>> = MutableList(0){defaultmap}
        val roomlist:MutableList<Map<String, Any>> = MutableList(0){defaultmap}

        for (i in 0 until fclassrooms.size){
            classroomId.add(fclassrooms[i].classroom_id)
            val map:HashMap<String,Any> = HashMap()
            val str:String = when {
                fclassrooms[i].capacity.toInt()<100 -> "S"
                fclassrooms[i].capacity.toInt()<200 -> "M"
                else -> "L"
            }
            size.add(str)
            map["aclassroomnum"] = fclassrooms[i].classroom.substring(fclassrooms[i].classroom.length-3,fclassrooms[i].classroom.length)
            map["aclassroomsize"] = str
            roomlist.add(map)
            if (!faclassrooms.contains(fclassrooms[i])){
                //不可用
                posList.add(i)
            }
        }
        val item = Flooritem()
        item.getMessage(floor,roomlist,posList,context,viewModel,month,classroomId,week, day,buildingName,size)
        itemList.add(item)
    }

    fun refresh(){
        roomManager.getAvailableRoomList(viewModel,array,dayofweek,week,classrooms,this)
    }


}

