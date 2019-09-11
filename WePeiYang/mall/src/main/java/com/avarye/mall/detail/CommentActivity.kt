package com.avarye.mall.detail

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ExpandableListView
import android.widget.TextView
import com.avarye.mall.R
import com.avarye.mall.service.commentLiveData
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import es.dmoral.toasty.Toasty
import org.w3c.dom.Comment

class CommentActivity : AppCompatActivity() {

    private var bt_comment: TextView? = null//底部评论点击框
    private var expandableListView: ExpandableListView? = null
    private var adapter: CommentAdapter? = null
    private var commentsList: List<Comment>? = null
    private var dialog: BottomSheetDialog? = null
    private var presenter = CommentPresenter(this)
    lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mall_comment)

        id = intent.getStringExtra("id")
        presenter.getUid(id)

        expandableListView = findViewById(R.id.detail_page_lv_comment) as ExpandableListView
        bt_comment = findViewById(R.id.detail_page_do_comment) as TextView
        bt_comment!!.setOnClickListener{
            showCommentDialog()
        }

        presenter.getCommentList()
        commentLiveData.bindNonNull(this){

            expandableListView!!.setGroupIndicator(null)
            //默认展开所有回复
            adapter = CommentAdapter(this, it)
            expandableListView!!.setAdapter(adapter)
            for (i in 0 until it!!.size) {
                expandableListView!!.expandGroup(i)
            }


        }



    }


    /**
     * by moos on 2018/04/20
     * func:弹出评论框
     */
    private fun showCommentDialog() {
        dialog = BottomSheetDialog(this)
        val commentView = LayoutInflater.from(this).inflate(R.layout.mall_comment_dialog_two, null)
        val commentText = commentView.findViewById(R.id.dialog_comment_et) as EditText
        val bt_comment = commentView.findViewById(R.id.dialog_comment_bt) as Button
        dialog!!.setContentView(commentView)

        val parent = commentView.getParent() as View
        val behavior = BottomSheetBehavior.from(parent)
        commentView.measure(0, 0)
        behavior.setPeekHeight(commentView.getMeasuredHeight())

        bt_comment.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val commentContent = commentText.getText().toString().trim()
                if (!TextUtils.isEmpty(commentContent)) {

                    //commentOnWork(commentContent);
                    dialog!!.dismiss()
                    /*val detailBean = CommentDetailBean("小明", commentContent, "刚刚")
                    adapter!!.addTheCommentData(detailBean)*/

                    Toasty.success(this@CommentActivity,"评论成功")


                } else {
                    Toasty.error(this@CommentActivity,"评论失败")
                }
            }
        })
        commentText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (!TextUtils.isEmpty(charSequence) && charSequence.length > 2) {
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"))
                } else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"))
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
        dialog!!.show()
    }


}
