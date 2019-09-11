package com.avarye.mall.detail

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.avarye.mall.R
import com.avarye.mall.service.CommentList

class CommentAdapter(private val context: Context,private val commentList: List<CommentList>?)  : BaseExpandableListAdapter() {


    private val commentReplyList: List<CommentList>?=null

    inner class GroupHolder(view: View) {
        //父列表，要绑的有用户id和评论内容和时间
        internal var firstCommentUser: TextView? = null
        internal var firstCOmmentContent: TextView? = null
        internal var firstCommentTime: TextView? = null

        init {
            firstCommentTime = view.findViewById<View>(R.id.comment_time) as TextView
            firstCommentUser = view.findViewById(R.id.coment_user_tv) as TextView
            firstCOmmentContent = view.findViewById(R.id.comment_content) as TextView
        }


    }

    inner class ChildHolder(view: View) {
        //子列表，有用户id和评论内容
        internal var secondCommentUser: TextView? = null
        internal var secondCommentContent: TextView? = null

        init {
            secondCommentUser = view.findViewById(R.id.reply_item_user) as TextView
            secondCommentContent = view.findViewById(R.id.reply_item_content) as TextView
        }
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }

    override fun getGroup(p0: Int): List<CommentList>? {
        return commentList
    }


    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return commentList!!.size
    }

    override fun getGroupView(p0: Int, p1: Boolean, convertView: View?, p3: ViewGroup?): View {

//        var convertView = convertView
//        val groupHolder: GroupHolder
//
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.mall_comment_item, p3, false)
//            groupHolder = GroupHolder(convertView!!)
//            convertView.setTag(groupHolder)
//        } else {
//            groupHolder = convertView.getTag() as GroupHolder
//        }
//
//        groupHolder.firstCommentUser?.text = commentList!![p0].name
//        groupHolder.firstCOmmentContent?.text = commentList[p0].content
//        groupHolder.firstCommentTime?.text = commentList[p0].content
//
        return convertView!!
    }


    override fun getChild(p0: Int, p1: Int): Any {
        return commentReplyList!!
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, p2: Boolean, convertView: View?, p4: ViewGroup?): View {
//        var convertView = convertView
//        val childHolder: ChildHolder
//        if (convertView == null) {
//            convertView=LayoutInflater.from(context).inflate(R.layout.mall_comment_reply_item,p4,false)
//            childHolder=ChildHolder(convertView)
//            convertView!!.setTag(childHolder)
//
//        } else {
//            childHolder = convertView.getTag() as ChildHolder
//        }
//
//        childHolder.secondCommentUser?.text=commentReplyList!![childPosition].name
//        childHolder.secondCommentContent?.text=commentReplyList[childPosition].content
        return convertView!!
    }

    override fun getChildId(p0: Int, p1: Int): Long {
        return getCombinedChildId(p0.toLong(), p1.toLong())
    }

    override fun getChildrenCount(p0: Int): Int {
        return commentReplyList!!.size
    }


    override fun hasStableIds(): Boolean {
        return true
    }

    //添加评论，回复评论




    companion object {
        private val TAG = "CommentExpandAdapter"
    }
}