package com.twt.service.job.search

import com.twt.service.job.service.InfoOrMeeting

class JobSearchContract {
    interface JobSearchView {
        fun onSuccess(info: List<InfoOrMeeting>,meeting:List<InfoOrMeeting>)
        fun onNull()
        fun onError(msg: String)
    }

    interface JobSearchPresenter {
        fun searchKeyword(keyword: String)
    }
}