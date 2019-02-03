package com.twt.service.job.service

import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.preference.hawk
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

object JobHomeModel {

    // 分别存四个碎片的最大页数
    var pagesOfMsg: Int by hawk(JOB_MESSAGE, 0)
    var pagesOfFair: Int by hawk(JOB_FAIR, 0)
    var pagesOfNotice: Int by hawk(NOTICE, 0)
    var pagesOfDynamic: Int by hawk(DYNAMIC, 0)

    fun getRecruits(type: Int, page: Int, callback: suspend (RefreshState<Unit>, List<ImportantL>?, List<CommonL>?) -> Unit) {
        launch(UI + QuietCoroutineExceptionHandler) {
            JobService.getRecruits(type, page).awaitAndHandle {
                callback(RefreshState.Failure(it), null, null)
            }?.data?.let {
                when (type) {
                // 把最大页数存下来，便于上拉刷新的时候判断
                    0 -> pagesOfMsg = it.page.toInt()
                    1 -> pagesOfFair = it.page.toInt()
                }
                callback(RefreshState.Success(Unit), it.important, it.common)
            }
        }
    }

    fun getNotioces(type: Int, page: Int, callback: suspend (RefreshState<Unit>, List<ImportantR>?, List<ImportantR>?, List<CommonR>?) -> Unit) {
        launch(UI + QuietCoroutineExceptionHandler) {
            JobService.getNotioces(type, page).awaitAndHandle {
                callback(RefreshState.Failure(it), null, null, null)
            }?.data?.let {
                when (type) {
                // 把最大页数存下来，便于上拉刷新的时候判断
                    0 -> pagesOfNotice = it.page_count
                    1 -> pagesOfDynamic = it.page_count
                }
                callback(RefreshState.Success(Unit), it.rotation, it.important, it.common)
            }
        }
    }
}