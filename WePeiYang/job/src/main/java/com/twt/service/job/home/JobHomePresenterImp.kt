package com.twt.service.job.home

import com.twt.service.job.service.*
import com.twt.service.job.service.JobHomeModel.getNotioces
import com.twt.service.job.service.JobHomeModel.getRecruits
import com.twt.wepeiyang.commons.experimental.cache.RefreshState

class JobHomePresenterImp(val jobHomeView: JobHomeContract.JobHomeView) : JobHomeContract.JobHomePresenter {
    //（招聘信息、招聘会）L；（公告、动态）R
    //如果是招聘会，因为没有在置顶项，只传list<CommonL>
    override fun getGeneral(kind: String, page: Int) {
        when (kind) {
            JOB_MESSAGE -> {
                getRecruits(0, page) { refreshState, importantBean, commonBean ->
                    when (refreshState) {
                        is RefreshState.Success -> {
                            if ((importantBean == null || importantBean.isEmpty()) && (commonBean == null || commonBean.isEmpty())) {
                                jobHomeView.onNull()
                            } else {
                                jobHomeView.showHomeMessage(importantBean, commonBean)
                            }
                        }
                        is RefreshState.Failure -> jobHomeView.onError("${refreshState.throwable}")
                    }
                }
            }
            JOB_FAIR -> {
                getRecruits(1, page) { refreshState, importantBean, commonBean ->
                    when (refreshState) {
                        is RefreshState.Success -> {
                            if (commonBean == null || commonBean.isEmpty()) {
                                jobHomeView.onNull()
                            } else {
                                jobHomeView.showHomeFair(commonBean)
                            }
                        }
                        is RefreshState.Failure -> jobHomeView.onError("${refreshState.throwable}")
                    }
                }
            }
            NOTICE, DYNAMIC -> {
                getNotioces(0, page) { refreshState, rotationBean,importantBean, commonBean ->
                    when (refreshState) {
                        is RefreshState.Success -> {
                            if ((rotationBean == null || rotationBean.isEmpty())&&(importantBean == null || importantBean.isEmpty()) && (commonBean == null || commonBean.isEmpty())) {
                                jobHomeView.onNull()
                            } else {
                                val importantR = mutableListOf<ImportantR>()
                                if (rotationBean != null) importantR.addAll(rotationBean)
                                if (importantBean != null) importantR.addAll(importantBean)
                                jobHomeView.showHomeRight(rotationBean,commonBean)
                            }
                        }
                        is RefreshState.Failure -> jobHomeView.onError("${refreshState.throwable}")
                    }
                }
            }
        }
    }
}