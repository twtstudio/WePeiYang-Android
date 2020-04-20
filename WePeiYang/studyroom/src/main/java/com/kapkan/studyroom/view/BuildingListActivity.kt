package com.kapkan.studyroom.view

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
    var itemList:ArrayList<Item> = ArrayList()
    //查询列表
    val viewModel = ViewModel()
    val defaultmap = HashMap<String,Any>()
    var buildingID:String = ""
    var buildingName:String = ""
    var month:Int = 1
    var select:BooleanArray = BooleanArray(12){false}
    var selectstr:String = "当前选中时间:"
    var day:Int = 0
    var week:Int = 0
    var classrooms:List<Classroom> = ArrayList()
    var aclassrooms:List<Classroom> = ArrayList()
    lateinit var recyclerView: RecyclerView
    lateinit var itemManager: ItemManager
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
        init()
    }

    fun init(){

        current_building.text = buildingName
        for (i in 0 .. 11){
            if (select[i]){
                selectstr += "$i、"
            }
        }
        selectstr = selectstr.substring(0,selectstr.length) + "节"
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
                    classrooms = it.classrooms
                }
            }
            AvailableRoomListData.value?.data!!.forEach {
                if (it.building_id == buildingID){
                    aclassrooms = it.classrooms
                }
            }
            judgeFloor()
            itemManager.addAll(itemList)
            recyclerView.adapter.notifyDataSetChanged()
        }else{
            Toast.makeText(this,"网络错误",Toast.LENGTH_SHORT).show()
        }
    }

    fun judgeFloor(){
        var a = 0
        var curstr:String = ""
        while (true){

            val fclassrooms:ArrayList<Classroom> = ArrayList()
            val faclassrooms:ArrayList<Classroom> = ArrayList()
            for (i in a until  classrooms.size){
                val str:String = when{
                    classrooms[i].classroom_id.substring(2,3) =="1" -> "一楼"
                    classrooms[i].classroom_id.substring(2,3) =="2" -> "二楼"
                    classrooms[i].classroom_id.substring(2,3) =="3" -> "三楼"
                    classrooms[i].classroom_id.substring(2,3) =="4" -> "四楼"
                    classrooms[i].classroom_id.substring(2,3) =="5" -> "五楼"
                    classrooms[i].classroom_id.substring(2,3) =="6" -> "六楼"
                    else -> ""
                }
                if (i==0) curstr = str
                if (curstr == str && i != classrooms.size-1){
                    fclassrooms.add(classrooms[i])

                    if (aclassrooms.contains(classrooms[i])){
                        faclassrooms.add(classrooms[i])
                    }
                }else{
                    loadByFloor(curstr,fclassrooms,faclassrooms)
                    curstr = str
                    fclassrooms.clear()
                    faclassrooms.clear()
                    a = i
                    break
                }
            }
            if (a == classrooms.size-1) break
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
            val sizemap:HashMap<String,String> = HashMap()
            val str:String = when {
                fclassrooms[i].capacity.toInt()<100 -> "S"
                fclassrooms[i].capacity.toInt()<200 -> "M"
                else -> "L"
            }
            size.add(str)
            if (faclassrooms.contains(fclassrooms[i])){
                //可用
                map["aclassroomnum"] = fclassrooms[i].classroom_id.substring(2,5)
                map["aclassroomsize"] = str
                roomlist.add(map)
                posList.add(i)
            }else{
                //不可用
                map["aclassroomnum"] = fclassrooms[i].classroom_id.substring(2,5)
                map["aclassroomsize"] = str
                roomlist.add(map)
            }
        }
        val item = Flooritem()
        item.getMessage(floor,roomlist,posList,context,viewModel,month,classroomId,week, day,buildingName,size)
        itemList.add(item)
    }


    fun refresh(){

    }
}

