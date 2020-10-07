package com.twt.service.announcement.ui.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.twt.service.announcement.R
import com.twt.service.announcement.service.AnnoPreference
import com.twt.service.announcement.service.AnnoService
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * DetailReplyBottomFragment
 * @author TranceDream
 * 这个是评论按钮的点击弹出Fragment
 */
class DetailReplyBottomFragment : BottomSheetDialogFragment() {
    companion object {
        private const val TAG_DETAIL_REPLY_BOTTOM_FRAGMENT = "DetailReplyBottomFragment"

        fun showDetailReplyBottomFragment(activity: AppCompatActivity, questionId: Int, onRefresh: () -> Unit) {
            val fragmentManager = activity.supportFragmentManager
            var fragment = fragmentManager.findFragmentByTag(TAG_DETAIL_REPLY_BOTTOM_FRAGMENT) as? DetailReplyBottomFragment
            if (fragment == null) {
                fragment = DetailReplyBottomFragment()
            }
            if (fragment.isAdded) return
            fragment.questionId = questionId
            fragment.onRefresh = onRefresh
            fragment.show(fragmentManager, TAG_DETAIL_REPLY_BOTTOM_FRAGMENT)
        }
    }

    var questionId: Int = 0
    var commentContent: String = ""
    var isCommentable: Boolean = true
    var onRefresh: () -> Unit = {}

    @SuppressLint("InflateParams")
    override fun setupDialog(dialog: Dialog?, style: Int) {
        val itemView: View = LayoutInflater.from(context).inflate(R.layout.anno_detail_bottom, null)
        dialog?.setContentView(itemView)
        val commentEditText: EditText = dialog!!.findViewById(R.id.annoDetailBottomEditText)
        val commentButton: ImageButton = dialog.findViewById(R.id.annoDetailBottomButton)
        commentButton.onClick {
            if (isCommentable) {
                isCommentable = false
                GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                    sendComment()
                }.invokeOnCompletion {
                    if (commentContent != "") {
                        isCommentable = true
                        onRefresh.invoke()
                    }
                }
            }
        }
        commentEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                commentContent = s.toString()
            }
        })
    }

    /**
     * 发表评论
     */
    private suspend fun sendComment() {
        if (commentContent == "") {
            Toast.makeText(this.context!!, "内容不能为空", Toast.LENGTH_SHORT).show()
        } else {
            AnnoService.addCommit(questionId, AnnoPreference.myId!!, commentContent).awaitAndHandle {
                Toast.makeText(this.context!!, "发送评论失败", Toast.LENGTH_SHORT).show()
            }?.ErrorCode?.let {
                if (it == 0) {
                    Toasty.success(this.context!!, "发送成功").show()
                    dismiss()
                }
            }
        }
    }
}