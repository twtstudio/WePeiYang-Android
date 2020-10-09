package com.twt.service.job.search

import com.twt.service.job.service.InfoOrMeeting
import com.twt.service.job.service.JobService
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object JobSearchModel {
    fun jobSearch(keyword: String, callBack: suspend (RefreshState<Unit>, List<InfoOrMeeting>?, List<InfoOrMeeting>?) -> Unit) {
        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
            JobService.jobSearch(keyword).awaitAndHandle {
                callBack(RefreshState.Failure(it), null, null)
            }?.data?.let {
                callBack(RefreshState.Success(Unit), it.info, it.meeting)
            }
        }
    }
}