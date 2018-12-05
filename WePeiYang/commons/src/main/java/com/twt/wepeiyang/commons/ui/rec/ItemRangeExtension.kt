package com.twt.wepeiyang.commons.ui.rec

import android.support.v7.widget.RecyclerView

/**
 * experimental 实验性功能
 */

//class ItemWrapper(item: Item, tag: Any? = null, tag2: Any? = null)

class RecDSLRange(private val list: MutableList<Item>) {
    //    val wrappedRange = mutableListOf<ItemWrapper>()
//    val code = hashCode()
    var itemRangeManager: ItemRangeManager? = null

    fun getItemList() = list

    fun refreshAll(block: MutableList<Item>.() -> Unit) {
        list.clear()
        list.apply(block)
        itemRangeManager?.refresh()
    }

    fun autoRefresh(block: MutableList<Item>.() -> Unit) {
        list.apply(block)
        itemRangeManager?.refresh()
    }
}

fun itemRange(init: MutableList<Item>.() -> Unit): RecDSLRange {
    val itemList = mutableListOf<Item>().apply(init)
    val dslRange = RecDSLRange(itemList)
    return dslRange
}

class ItemRangeManager(private val itemManager: ItemManager, private val list: List<RecDSLRange>) {
    private val merge: MutableList<Item>.() -> Unit = {
        list.forEach {
            addAll(it.getItemList())
            it.itemRangeManager = this@ItemRangeManager
        }
    }

    fun refresh() {
        itemManager.refreshAll(merge)
    }
}

fun RecyclerView.bindWithRanges(list: List<RecDSLRange>) {
    val itemManager = withItems {}
    val itemRangeManager = ItemRangeManager(itemManager, list)
    itemRangeManager.refresh()
}