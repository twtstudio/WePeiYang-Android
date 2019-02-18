package com.twt.service.job.search

import com.twt.wepeiyang.commons.experimental.cache.RefreshState

class JobSearchPresenter(val searchView: JobSearchContract.JobSearchView) : JobSearchContract.JobSearchPresenter {

    override fun searchKeyword(keyword: String) {
        JobSearchModel.jobSearch(keyword) { refresh, info, meeting ->
            when (refresh) {
                is RefreshState.Failure -> searchView.onError("${refresh.throwable}")
                is RefreshState.Success -> {
                    if (info.orEmpty().isEmpty() && meeting.orEmpty().isEmpty()) {
                        searchView.onNull()
                    } else {
                        searchView.onSuccess(info.orEmpty(),meeting.orEmpty())
                    }
                }
            }
        }
    }
}