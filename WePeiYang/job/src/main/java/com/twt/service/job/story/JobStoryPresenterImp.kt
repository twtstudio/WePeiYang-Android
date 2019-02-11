package com.twt.service.job.story

import com.twt.service.job.service.*
import com.twt.wepeiyang.commons.experimental.cache.RefreshState

class JobStoryPresenterImp(val jobStoryView: JobStoryContract.JobStoryView) : JobStoryContract.JobStoryPresenter {
    override fun getDetail(id: Int, kind: String) {
        when (kind) {
            JOB_MESSAGE -> {
                JobStoryModel.getRecruitDetail(id, JOB_MESSAGE_TYPE) { refreshState, recruit, msg ->
                    process(refreshState, Functions.convertNotice(recruit), msg)
                }
            }
            JOB_FAIR -> {
                JobStoryModel.getRecruitDetail(id, JOB_FAIR_TYPE) { refreshState, recruit, msg ->
                    when (refreshState) {
                        is RefreshState.Success -> {
                            if (recruit == null) jobStoryView.onNull(msg)
                            else jobStoryView.showFair(recruit)
                        }
                        is RefreshState.Failure -> jobStoryView.onError("${refreshState.throwable}")
                    }
                }
            }
            NOTICE -> {
                JobStoryModel.getNoticeDetail(id, NOTICE_TYPE) { refreshState, notice, msg ->
                    process(refreshState, notice, msg)
                }
            }
            DYNAMIC -> {
                JobStoryModel.getNoticeDetail(id, DYNAMIC_TYPE) { refreshState, notice, msg ->
                    process(refreshState, notice, msg)
                }
            }
        }
    }

    private fun process(refreshState: RefreshState<Unit>, notice: Notice?, msg: String) {
        when (refreshState) {
            is RefreshState.Success -> {
                if (notice == null) jobStoryView.onNull(msg)
                else jobStoryView.showThree(Functions.convertNoticeAfter(notice))
            }
            is RefreshState.Failure -> jobStoryView.onError("${refreshState.throwable}")
        }
    }
}

