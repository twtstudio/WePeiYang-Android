package com.twt.service.job.story

import com.twt.service.job.service.JobService
import com.twt.service.job.service.Notice
import com.twt.service.job.service.Recruit
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.android.Main
import kotlinx.coroutines.android.UI
import kotlinx.coroutines.launch

object JobStoryModel {
    fun getRecruitDetail(id: Int, type: Int, callback: suspend (RefreshState<Unit>, Recruit?, String) -> Unit) {
        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
            JobService.getRecruitDetail(id, type).awaitAndHandle {
                callback(RefreshState.Failure(it), null, "")
            }?.let {
                callback(RefreshState.Success(Unit), it.data, it.message)
            }
        }
    }

    fun getNoticeDetail(id: Int, type: Int, callback: suspend (RefreshState<Unit>, Notice?, String) -> Unit) {
        launch(UI + QuietCoroutineExceptionHandler) {
            JobService.getNoticeDetail(id, type).awaitAndHandle {
                callback(RefreshState.Failure(it), null, "")
            }?.let {
                callback(RefreshState.Success(Unit), it.data, it.message)
            }
        }
    }
}