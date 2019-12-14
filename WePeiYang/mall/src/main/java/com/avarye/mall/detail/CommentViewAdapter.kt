package com.avarye.mall.detail

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.avarye.mall.R
import com.avarye.mall.service.CommentList
import kotlinx.android.synthetic.main.mall_item_comment_main.view.*

class CommentViewAdapter(val context: Context, private val commentList: List<CommentList>) : BaseExpandableListAdapter() {

    var inflater: LayoutInflater = LayoutInflater.from(context)

    private val replyList = mutableListOf<CommentList>()

    @SuppressLint("InflateParams")
    override fun getGroupView(p0: Int, p1: Boolean, convertView: View?, p3: ViewGroup?): View {
        val view = inflater.inflate(R.layout.mall_item_comment_main, null)
        view.comment_content.text = commentList[p0].content
        return view
    }

    override fun getChildView(p0: Int, p1: Int, p2: Boolean, convertView: View?, p4: ViewGroup?): View {
        val view = inflater.inflate(R.layout.mall_item_comment_main, null)
        view.comment_content.text = commentList[p0].content
        return view
    }

    override fun getGroup(p0: Int): List<CommentList>? {
        return commentList
    }

    override fun getChild(p0: Int, p1: Int): Any {
        return replyList
    }

    override fun getGroupId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getChildId(p0: Int, p1: Int): Long {
        return p1.toLong()
    }

    override fun getGroupCount(): Int {
        return commentList.size
    }

    override fun getChildrenCount(p0: Int): Int {
        return replyList.size
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }
}