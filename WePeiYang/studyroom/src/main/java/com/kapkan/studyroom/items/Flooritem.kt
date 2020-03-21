package com.kapkan.studyroom.items

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.SimpleAdapter
import android.widget.TextView
import com.twt.wepeiyang.commons.ui.rec.ItemController
import com.example.studyroom.R
import com.twt.wepeiyang.commons.ui.rec.Item
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.sdk27.coroutines.onItemClick
import java.util.ArrayList

class Flooritem(): Item {
    lateinit var context: Context
    lateinit var simpleAdapter: SimpleAdapter
    private companion object Controller:ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.item_floor, parent, false)
            return FloorViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            item as Flooritem
            holder as FloorViewHolder
            holder.floortext.text = item.floor
            val from: Array<String> = Array(2) { "classroomnum" }
            from[1] = "classroomsize"
            val to = IntArray(2) { R.id.classroomnum }
            to[1] = R.id.classroomsize
            item.simpleAdapter = SimpleAdapter(item.context,item.classroomlist,R.layout.item_classroom,from,to)
            holder.classgrid.adapter = item.simpleAdapter
            /*
            val params:ViewGroup.LayoutParams = holder.classgrid.layoutParams
            params.height = item.classroomlist.size*40
            holder.classgrid.layoutParams = params*/
            holder.classgrid.onItemClick { p0, p1, p2, p3 ->
                //点击跳转课表


            }
        }

        private class FloorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            val floortext:TextView = itemView.findViewById(R.id.floor)
            val classgrid:GridView = itemView.findViewById(R.id.floorgrid)
        }
    }


    override val controller: ItemController
        get() = Controller

    var sizeList:MutableList<Map<String, Any>> = MutableList(0){defaultmap}
    val defaultmap = HashMap<String,Any>()
    private var availablePos:ArrayList<Int> = ArrayList()
    lateinit var floor:String
    private var classroomlist:MutableList<Map<String, Any>> = MutableList(0){defaultmap}

    fun getMessage(floor:String,classrooms:MutableList<Map<String, Any>>,roomssize:MutableList<Map<String, Any>>,availablerooms:List<Int>,context:Context){
        this.floor = floor
        this.sizeList.addAll(roomssize)
        availablePos.addAll(availablerooms)
        classroomlist.addAll(classrooms)
        this.context = context
    }







}

