package com.kapkan.studyroom.items

import com.example.studyroom.R

object Building {
    val defaultmap = HashMap<String,Any>()
    val peiyangBuildinglist:MutableList<Map<String, Any>> = MutableList(0){ defaultmap}
    val weijinBuildinglist:MutableList<Map<String, Any>> = MutableList(0){ defaultmap}
    const val PYBASENUM = 1093
    const val WJBASENUM = 15   //0015
    fun initBuilding(){
        val map:HashMap<String,Any> = HashMap()
        map.put("img_building", R.drawable.icon_building)
        map.put("text_building","55楼")
        peiyangBuildinglist.add(map)
        map.put("img_building", R.drawable.icon_building)
        map.put("text_building","55楼")
        peiyangBuildinglist.add(map)
        map.put("img_building", R.drawable.icon_building)
        map.put("text_building","43楼")
        peiyangBuildinglist.add(map)
        map.put("img_building", R.drawable.icon_building)
        map.put("text_building","50楼")
        peiyangBuildinglist.add(map)
        map.put("img_building", R.drawable.icon_building)
        map.put("text_building","33楼(文学院)")
        peiyangBuildinglist.add(map)
        map.put("img_building", R.drawable.icon_building)
        map.put("text_building","31楼")
        peiyangBuildinglist.add(map)
        map.put("img_building", R.drawable.icon_building)
        map.put("text_building","32楼")
        peiyangBuildinglist.add(map)
        map.put("img_building", R.drawable.icon_building)
        map.put("text_building","44楼")
        peiyangBuildinglist.add(map)
        map.put("img_building", R.drawable.icon_building)
        map.put("text_building","45、46楼")
        peiyangBuildinglist.add(map)
        map.put("img_building", R.drawable.icon_building)
        map.put("text_building","33楼")
        peiyangBuildinglist.add(map)
        map.put("img_building", R.drawable.icon_building)
        map.put("text_building","37楼")
        peiyangBuildinglist.add(map)
        map.put("img_building", R.drawable.icon_building)

        map.put("text_building","23楼")
        weijinBuildinglist.add(map)
        map.put("img_building", R.drawable.icon_building)
        map.put("text_building","12楼")
        weijinBuildinglist.add(map)
        map.put("img_building", R.drawable.icon_building)
        map.put("text_building","19楼")
        weijinBuildinglist.add(map)
        map.put("img_building", R.drawable.icon_building)
        map.put("text_building","26楼A")
        weijinBuildinglist.add(map)
        map.put("img_building", R.drawable.icon_building)
        map.put("text_building","26楼B")
        weijinBuildinglist.add(map)
    }

}