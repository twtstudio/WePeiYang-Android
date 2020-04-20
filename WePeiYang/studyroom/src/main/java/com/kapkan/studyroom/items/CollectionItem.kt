package com.kapkan.studyroom.items

import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.studyroom.R
import com.kapkan.studyroom.service.ViewModel
import com.kapkan.studyroom.service.weekdata
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import kotlinx.android.synthetic.main.window_classroom.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onItemClick

class CollectionItem(): Item{
    override val controller: ItemController
        get() = Controller

    lateinit var context: Context
    lateinit var item:CollectionItem
    lateinit var gridView: GridView
    var roomid: String = ""
    var roomname: String  = ""
    var month:Int = 1
    var day = 1
    var week = 1
    var size = "M"
    var buildingName = ""
    var classroomname = ""


    lateinit var viewModel: ViewModel

    fun getMessage(context: Context,roomid: String,roomname: String, month:Int,viewModel: ViewModel){
        this.context = context
        this.roomid = roomid
        this.roomname = roomname
        this.month = month
        this.viewModel = viewModel
    }

    var a:String? = "001000000000"
    var b:String? = "100000000000"
    var c:String? = "100000000000"
    var d:String? = "000000000000"
    var e:String? = "100000000000"

    private companion object Controller:ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.item_star,parent,false)
            return CollectViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            item as CollectionItem
            holder as CollectViewHolder
            holder.roomid.text = item.roomname
            holder.starBtn.onClick {
                //取消收藏
                item.viewModel.unStar(item.roomid)
            }

            holder.roomid.onClick {
                item.viewModel.getClassroomWeekInfo(item.roomid,item.week,item.item)
                val classtable:View = LayoutInflater.from(item.context).inflate(R.layout.window_classroom,null,false)
                classtable.classroomname.text = item.buildingName+ item.classroomname
                classtable.classroomsize.text = item.size
                classtable.classroom_time.text = item.month.toString() + "月" + (item.day+1).toString() +"日"
                val popupWindow = PopupWindow(classtable, 940, 1480, true)
                popupWindow.isOutsideTouchable = true
                popupWindow.isTouchable = true
                popupWindow.isOutsideTouchable = true
                popupWindow.showAtLocation(classtable, Gravity.CENTER, 0, 0)
                item.gridView = classtable.classtable
                item.loadClasstable(item.gridView)
            }
        }

        private class CollectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val roomid = itemView.findViewById<TextView>(R.id.roomId)
            val starBtn = itemView.findViewById<ImageView>(R.id.star)

        }
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
            for (i in 0..4){
                for (j in 0..5)
                {
                    if (i==0&&a?.substring(2*j,2*j+1)=="1"&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        //周一的
                        val linearLayout: LinearLayout = gridView.getChildAt(6*j+7+i) as LinearLayout
                        linearLayout.backgroundColor = context.getColor(R.color.selected)
                    }

                    if (i==1&&b?.substring(2*j,2*j+1)=="1"&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        //周2的
                        val linearLayout: LinearLayout = gridView.getChildAt(6*j+7+i) as LinearLayout
                        linearLayout.backgroundColor = context.getColor(R.color.selected)
                    }
                    if (i==2&&c?.substring(2*j,2*j+1)=="1"&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        //周3的
                        val linearLayout: LinearLayout = gridView.getChildAt(6*j+7+i) as LinearLayout
                        linearLayout.backgroundColor = context.getColor(R.color.selected)
                    }
                    if (i==3&&d?.substring(2*j,2*j+1)=="1"&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        //周4的
                        val linearLayout: LinearLayout = gridView.getChildAt(6*j+7+i) as LinearLayout
                        linearLayout.backgroundColor = context.getColor(R.color.selected)
                    }
                    if (i==4&&e?.substring(2*j,2*j+1)=="1"&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        //周5的
                        val linearLayout: LinearLayout = gridView.getChildAt(6*j+7+i) as LinearLayout
                        linearLayout.backgroundColor = context.getColor(R.color.selected)
                    }
                }
            }
        }
    }

    fun getWeekInfo(wd: weekdata){
        a = wd.`1`
        b = wd.`2`
        c = wd.`3`
        d = wd.`4`
        e = wd.`5`
        gridView.performItemClick(gridView,7,gridView.adapter.getItemId(1))
    }

}