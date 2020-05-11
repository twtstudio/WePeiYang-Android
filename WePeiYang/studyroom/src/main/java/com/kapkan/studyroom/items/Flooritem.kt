package com.kapkan.studyroom.items

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.twt.wepeiyang.commons.ui.rec.ItemController
import com.example.studyroom.R
import com.kapkan.studyroom.Common.MyGirdView
import com.kapkan.studyroom.service.*
import com.twt.wepeiyang.commons.ui.rec.Item
import kotlinx.android.synthetic.main.window_classroom.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.image
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onItemClick
import java.util.ArrayList

class Flooritem(): Item {

    var sizeList:MutableList<Map<String, Any>> = MutableList(0){defaultmap}
    val defaultmap = HashMap<String,Any>()
    private var availablePos:ArrayList<Int> = ArrayList()
    lateinit var floor:String
    private var classroomlist:MutableList<Map<String, Any>> = MutableList(0){defaultmap}
    var load = true
    lateinit var item:Flooritem
    lateinit var gridView:MyGirdView
    var a:String? = "001000000000"
    var b:String? = "100000000000"
    var c:String? = "100000000000"
    var d:String? = "000000000000"
    var e:String? = "100000000000"
    var month:Int =1
    var week:Int = 1
    var day:Int = 1
    var buildingName = ""
    lateinit var size: ArrayList<String>
    lateinit var roomid: ArrayList<String>
    lateinit var viewModel: ViewModel
    lateinit var context: Context
    lateinit var simpleAdapter: SimpleAdapter

    private companion object Controller:ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.item_floor, parent, false)
            return FloorViewHolder(view)
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            item as Flooritem
            holder as FloorViewHolder
            holder.floortext.text = item.floor
            val from: Array<String> = Array(2) { "aclassroomnum" }
            from[1] = "aclassroomsize"
            val to = IntArray(2) { R.id.aclassroomnum }
            to[1] = R.id.aclassroomsize
            item.simpleAdapter = SimpleAdapter(item.context,item.classroomlist,R.layout.item_availableclassroom,from,to)
            holder.classgrid.adapter = item.simpleAdapter

            holder.classgrid.onItemClick { p0, p1, p2, p3 ->
                //点击弹出课表
                if (item.load){
                    //加载还有bug
                    for (i in 0 until item.availablePos.size) {
                        //是有教室没加载出来吗
                        holder.classgrid.post {
                            val card:CardView = holder.classgrid.getChildAt(item.availablePos[i]) as CardView
                            val linearLayout = card.getChildAt(0) as ConstraintLayout
                            linearLayout.background = item.context.getDrawable(R.drawable.classroom_selected)
                        }
                    }
                    item.load =false
                }
                else{
                    item.viewModel.getClassroomWeekInfo(item.roomid[p2],item.week,item.item)
                    val classtable:View = LayoutInflater.from(item.context).inflate(R.layout.window_classroom,null,false)
                    classtable.classroomname.text = item.buildingName+ item.classroomlist[p2]["aclassroomnum"]
                    classtable.classroomsize.text = item.size[p2]
                    classtable.classroom_time.text = item.month.toString() + "月" + (item.day+1).toString() +"日"
                    val popupWindow = PopupWindow(classtable, 740, 1400, true)
                    popupWindow.isOutsideTouchable = true
                    popupWindow.isTouchable = true
                    popupWindow.isOutsideTouchable = true
                    popupWindow.showAtLocation(classtable, Gravity.CENTER, 0, 0)
                    item.gridView = classtable.classtable
                    if (!collectionLiveData.value?.data.isNullOrEmpty()){
                        val data = collectionLiveData.value!!.data
                        for (i in 0 until data.size){
                            if (item.roomid[p2]==data[i].classroom_ID){
                                val image = classtable.findViewById<ImageView>(R.id.iconstar)
                                image.imageResource = R.drawable.icon_love
                                break
                            }
                        }
                    }


                    item.loadClasstable(item.gridView)
                    classtable.iconstar.onClick {
                        //收藏
                        if (!collectionLiveData.value?.data.isNullOrEmpty()){
                            val data = collectionLiveData.value!!.data
                            for (i in 0 until data.size){
                                if (item.roomid[p2]==data[i].classroom_ID){
                                    item.viewModel.unStar(item.roomid[p2])
                                    val image = classtable.findViewById<ImageView>(R.id.iconstar)
                                    image.image = item.context.getDrawable(R.drawable.icon_unlove)
                                    break
                                }
                            }
                            val image = classtable.findViewById<ImageView>(R.id.iconstar)
                            image.image = item.context.getDrawable(R.drawable.icon_love)
                            item.viewModel.star(item.roomid[p2])
                        }else{
                            val image = classtable.findViewById<ImageView>(R.id.iconstar)
                            image.image = item.context.getDrawable(R.drawable.icon_love)
                            item.viewModel.star(item.roomid[p2])

                        }
                    }
                }
            }
            holder.classgrid.performItemClick(holder.classgrid,0,holder.classgrid.getItemIdAtPosition(0))
        }

        private class FloorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            val floortext:TextView = itemView.findViewById(R.id.floor)
            val classgrid:GridView = itemView.findViewById(R.id.floorgrid)
        }
    }


    override val controller: ItemController
        get() = Controller



    fun getMessage(floor:String,classrooms:MutableList<Map<String, Any>>,availablerooms:List<Int>,context:Context,viewModel: ViewModel,month:Int,roomid:ArrayList<String>,week:Int,day:Int,name:String,sizeList:ArrayList<String>){
        item = this
        this.floor = floor
        availablePos.addAll(availablerooms)
        classroomlist.addAll(classrooms)
        this.context = context
        this.viewModel = viewModel
        this.month = month
        this.roomid = roomid
        this.week = week
        this.day = day
        buildingName = name
        size = sizeList
    }

    fun loadClasstable(gridView: GridView){
        val defaultmap = HashMap<String, Any>()
        var classtablelist: MutableList<Map<String, Any>> = MutableList(0) { defaultmap }
        val str = "coursetext"
        for (i in 0..41){
            val map:HashMap<String, Any> = HashMap()
            when{
                i == 0 -> map[str] = month.toString() + "月"
                i in 1..5 -> map[str] = when{
                    i==1-> "Mon"
                    i==2-> "Tue"
                    i==3-> "Wed"
                    i==4-> "Thu"
                    else -> "Fri"
                }
                i == 6 -> map[str] = "1~2"
                i == 12 -> map[str] = "3~4"
                i == 18 -> map[str] = "5~6"
                i == 24 -> map[str] = "7~8"
                i == 30 -> map[str] = "9~10"
                i == 36 -> map[str] = "11~12"
                else -> map[str] = ""
            }
            classtablelist.add(map)
        }


        val from = Array(1){"coursetext"}
        val to = IntArray(1){R.id.coursetext}
        gridView.adapter = SimpleAdapter(context,classtablelist,R.layout.item_course,from,to)

        gridView.onItemClick { p0, p1, p2, p3 ->
            gridView.post {
            for (i in 0..4){
                for (j in 0..5)
                {
                    if (i==0&&a?.substring(2*j,2*j+1)=="1"&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        //周一的
                        val linearLayout:LinearLayout = gridView.getChildAt(6*j+7+i) as LinearLayout
                        linearLayout.backgroundColor = context.getColor(R.color.selected)
                    }

                    if (i==1&&b?.substring(2*j,2*j+1)=="1"&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        //周2的

                        val linearLayout:LinearLayout = gridView.getChildAt(6*j+7+i) as LinearLayout
                        linearLayout.backgroundColor = context.getColor(R.color.selected)
                    }
                    if (i==2&&c?.substring(2*j,2*j+1)=="1"&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        //周3的
                        val linearLayout:LinearLayout = gridView.getChildAt(6*j+7+i) as LinearLayout
                        linearLayout.backgroundColor = context.getColor(R.color.selected)
                    }
                    if (i==3&&d?.substring(2*j,2*j+1)=="1"&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        //周4的
                        val linearLayout:LinearLayout = gridView.getChildAt(6*j+7+i) as LinearLayout
                        linearLayout.backgroundColor = context.getColor(R.color.selected)
                    }
                    if (i==4&&e?.substring(2*j,2*j+1)=="1"&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        //周5的
                        val linearLayout:LinearLayout = gridView.getChildAt(6*j+7+i) as LinearLayout
                        linearLayout.backgroundColor = context.getColor(R.color.selected)
                    }
                }
             }
            }
        }
        gridView.performItemClick(gridView,0,gridView.adapter.getItemId(1))
    }

    fun getWeekInfo(wd:weekdata){
        a = wd.`1`
        b = wd.`2`
        c = wd.`3`
        d = wd.`4`
        e = wd.`5`
        gridView.performItemClick(gridView,7,gridView.adapter.getItemId(1))
    }



}

