package com.twt.service.job.story

import com.twt.service.job.service.Notice
import com.twt.service.job.service.Recruit

interface JobStoryContract {
    interface JobStoryView {
        fun showThree(notice: Notice)
        fun showFair(recruit: Recruit)
        fun onNull(msg:String)
        fun onError(msg: String)
    }

    interface JobStoryPresenter {
        fun getDetail(id: Int, kind: String)
    }
}