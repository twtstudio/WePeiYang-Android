package com.avarye.mall.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.avarye.mall.R
import com.avarye.mall.service.Menu
import kotlinx.android.synthetic.main.mall_item_menu_child.view.*
import kotlinx.android.synthetic.main.mall_item_menu_main.view.*

class MenuViewAdapter(val context: Context, private val category: List<Menu>) : BaseExpandableListAdapter() {

    var inflater: LayoutInflater = LayoutInflater.from(context)

    @SuppressLint("InflateParams")
    override fun getGroupView(p0: Int, p1: Boolean, p2: View?, p3: ViewGroup?): View {
        val view = inflater.inflate(R.layout.mall_item_menu_main, null)
        val textView = view.tv_menu_main
        textView.text = category[p0].name
        return view
    }

    @SuppressLint("InflateParams")
    override fun getChildView(p0: Int, p1: Int, p2: Boolean, p3: View?, p4: ViewGroup?): View {
        val view = inflater.inflate(R.layout.mall_item_menu_child, null)
        view.tv_menu_child.text = category[p0].smalllist[p1].name

        //gridView莫名不好用
        /*val gridView = view.gv_menu
        val dataList = mutableListOf<String>()
        for (item in category[p0].smalllist) {
            dataList.add(item.name)
        }
        val adapter = ArrayAdapter(context, R.layout.mall_item_menu_child, dataList)
        gridView.adapter = adapter
        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
            val intent = Intent(context, SearchActivity::class.java).putExtra("key", category[p0].smalllist[p1].id)
            context.startActivity(intent)
        }*/
        return view
    }


    override fun getGroup(p0: Int): Any {
        return category[p0]
    }

    override fun getChild(p0: Int, p1: Int): Any {
        return category[p0].smalllist[p1]
    }

    override fun getGroupId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getChildId(p0: Int, p1: Int): Long {
        return p1.toLong()
    }

    override fun getGroupCount(): Int {
        return category.size
    }

    override fun getChildrenCount(p0: Int): Int {
        return category[p0].smalllist.size
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }

}