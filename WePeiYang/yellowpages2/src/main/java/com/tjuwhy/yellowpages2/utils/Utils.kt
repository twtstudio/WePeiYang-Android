package com.tjuwhy.yellowpages2.utils

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.tjuwhy.yellowpages2.service.*
import com.tjuwhy.yellowpages2.view.*
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import java.text.Collator

interface UpdateCallBack {
    fun collectionUpdate(id: Int)
}

interface Expandable {

    fun collapse(index: Int)

    fun expand(index: Int)
}

class ExpandableHelper(val context: Context, recyclerView: RecyclerView, var groupData: Array<GroupData>, private val childArray: Array<Array<SubData>>) : Expandable {

    var itemManager: ItemManager = ItemManager()
    val items = mutableListOf<Item>(HeaderItem(context))

    init {
        recyclerView.adapter = ItemAdapter(itemManager)
        groupData.map { it -> GroupItem(it, this) }
        items.addAll(groupData.map { it ->
            GroupItem(it, this)
        })
        itemManager.addAll(items)
        groupData.forEachIndexed { index, groupData ->
            if (groupData.isExpanded) {
                expand(index)
            }
        }
    }

    override fun collapse(index: Int) {
        var targetStart = 2 + index
        for (i in 0 until index) {
            targetStart += if (groupData[i].isExpanded) childArray[i].size else 0
        }
        for (i in 0 until childArray[index].size) {
            itemManager.removeAt(targetStart)
        }
    }

    override fun expand(index: Int) {
        var targetIndex = index
        for (i in 0 until index) {
            targetIndex += if (groupData[i].isExpanded) childArray[i].size else 0
        }
        itemManager.addAll(targetIndex + 2/*Header*/, childArray[index].map { it ->
            when (it.type) {
                ITEM_SECOND -> SubItem(context, it.title, it.groupIndex, it.childIndex, index)
                ITEM_COLLECTION -> ChildItem(context, it.title, it.phone, it.isStared, it.thirdId)
                ITEM_CHAR -> CharItem(it.firstChar)
                else -> {
                    CharItem(it.firstChar)
                }
            }
        }
        )
    }

}

class Selector(val content: String) : Comparable<Selector> {

    private val comparator = Collator.getInstance(java.util.Locale.CHINA)!!

    override fun compareTo(other: Selector): Int {
        return comparator.compare(content, other.content)
    }

}
