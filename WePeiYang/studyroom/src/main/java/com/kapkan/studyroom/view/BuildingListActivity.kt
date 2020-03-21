package com.kapkan.studyroom.view

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
    var select:BooleanArray = BooleanArray(12){false}
    var selectstr:String = "当前选中时间:"
    lateinit var classrooms:List<Classroom>
    lateinit var aclassrooms:List<Classroom>
    lateinit var recyclerView: RecyclerView
    lateinit var itemManager: ItemManager
    val context:Context = this

    companion object data{
        val Buildings = BuildingListData
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buildingdetails)
        val buildingName:String = intent.getStringExtra("buildingName")
        current_building.text = buildingName
        buildingID =  intent.getIntExtra("buildingID",0).toString()
        select = intent.getBooleanArrayExtra("course")
        init()
    }

    fun init(){

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
        val defaultmap = HashMap<String, Any>()
        val posList:ArrayList<Int> = ArrayList()
        val sizeList:MutableList<Map<String, Any>> = MutableList(0){defaultmap}
        val roomlist:MutableList<Map<String, Any>> = MutableList(0){defaultmap}
        for (i in 0 until fclassrooms.size){
            val map:HashMap<String,Any> = HashMap()
            val sizemap:HashMap<String,String> = HashMap()
            val str:String = when {
                fclassrooms[i].capacity.toInt()<100 -> "S"
                fclassrooms[i].capacity.toInt()<200 -> "M"
                else -> "L"
            }
            if (faclassrooms.contains(fclassrooms[i])){
                //可用
                map["aclassroomnum"] = fclassrooms[i].classroom_id.substring(2,5)
                sizemap["aclassroomsize"] = str
                sizeList.add(sizemap)
                roomlist.add(map)
                posList.add(i)
            }else{
                //不可用
                map["classroomnum"] = fclassrooms[i].classroom_id.substring(2,5)
                sizemap["classroomsize"] = str
                sizeList.add(sizemap)
                roomlist.add(map)
            }
        }
        val item = Flooritem()
        item.getMessage(floor,roomlist,sizeList,posList,context)
        itemList.add(item)
    }


    fun refresh(){

    }
}

