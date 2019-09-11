package com.avarye.mall.detail

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CommentPresenter(private val view: CommentActivity) {

    lateinit var uid: String//获取评论列表时需要传的参数
    lateinit var cid: String//获取回复列表时需要传的参数
    lateinit var commentContent: String
    lateinit var replyContent: String


    fun getUid(uid: String) {
        this.uid = uid
    }

    fun getCid(cid: String) {
        this.cid = cid
    }


    fun getCommentList() {
        GlobalScope.launch(Dispatchers.Main) {
            /*MallManager.getCommentListAsync(uid, 1).awaitAndHandle {
                Toasty.error(view, "评论获取失败")
            }.let {
                commentLiveData.postValue(it)
            }*/
        }
    }

    fun reply() {

    }

    fun getReplyList() {
        GlobalScope.launch(Dispatchers.Main) {
            /*MallManager.getReplyListAsync(cid).awaitAndHandle {
                Toasty.error(view, "回复列表获取失败")
            }.let {
                commentLiveData.postValue(it)
            }*/
        }

    }
}
