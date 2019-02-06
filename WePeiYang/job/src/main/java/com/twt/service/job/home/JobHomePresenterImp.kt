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
                getRecruits(JOB_MESSAGE_TYPE, page) { refreshState, importantBean, commonBean ->
                    when (refreshState) {
                        is RefreshState.Success -> {
                            if ((importantBean == null || importantBean.isEmpty()) && (commonBean == null || commonBean.isEmpty())) {
                                jobHomeView.onNull()
                            } else {
                                jobHomeView.showThree(funs.convert(importantBean.orEmpty()), funs.convert(commonBean.orEmpty()))
                            }
                        }
                        is RefreshState.Failure -> jobHomeView.onError("${refreshState.throwable}")
                    }
                }
            }
            JOB_FAIR -> {
                getRecruits(JOB_FAIR_TYPE, page) { refreshState, _, commonBean ->
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
                getNotioces(NOTICE_TYPE, page) { refreshState, rotationBean, importantBean, commonBean ->
                    when (refreshState) {
                        is RefreshState.Success -> {
                            if ((rotationBean == null || rotationBean.isEmpty())&&(importantBean == null || importantBean.isEmpty()) && (commonBean == null || commonBean.isEmpty())) {
                                jobHomeView.onNull()
                            } else {
                                (rotationBean as MutableList<HomeDataR>).addAll(importantBean as MutableList<HomeDataR>)
                                jobHomeView.showThree(rotationBean.orEmpty(),commonBean.orEmpty())
                            }
                        }
                        is RefreshState.Failure -> jobHomeView.onError("${refreshState.throwable}")
                    }
                }
            }
            DYNAMIC->{
                getNotioces(DYNAMIC_TYPE, page) { refreshState, rotationBean, importantBean, commonBean ->
                    when (refreshState) {
                        is RefreshState.Success -> {
                            if ((rotationBean == null || rotationBean.isEmpty())&&(importantBean == null || importantBean.isEmpty()) && (commonBean == null || commonBean.isEmpty())) {
                                jobHomeView.onNull()
                            } else {
                                (rotationBean as MutableList<HomeDataR>).addAll(importantBean as MutableList<HomeDataR>)
                                jobHomeView.showThree(rotationBean.orEmpty(),commonBean.orEmpty())
                            }
                        }
                        is RefreshState.Failure -> jobHomeView.onError("${refreshState.throwable}")
                    }
                }
            }
        }
    }
}