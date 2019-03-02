package com.twt.service.job.home

import com.twt.service.job.service.*
import com.twt.service.job.home.JobHomeModel.getNotices
import com.twt.service.job.home.JobHomeModel.getRecruits
import com.twt.wepeiyang.commons.experimental.cache.RefreshState

class JobHomePresenterImp(val jobHomeView: JobHomeContract.JobHomeView) : JobHomeContract.JobHomePresenter {
    //（招聘信息、招聘会）L；（公告、动态）R
    // 如果是招聘会，因为没有在置顶项，只传list<CommonL>
    override fun getGeneral(kind: String, page: Int) {
        when (kind) {
            JOB_MESSAGE -> {
                getRecruits(JOB_MESSAGE_TYPE, page) { refreshState, importantBean, commonBean ->
                    when (refreshState) {
                        is RefreshState.Success -> {
                            if ((importantBean == null || importantBean.isEmpty()) && (commonBean == null || commonBean.isEmpty())) {
                                jobHomeView.onNull()
                            } else {
                                if (page == 1) {// first time
                                    jobHomeView.showThree(merge(null, importantBean.orEmpty().convertR(), commonBean.orEmpty().convertR()))
                                } else { // up load more
                                    jobHomeView.loadMoreOther(commonBean.orEmpty().convertR())
                                }
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
                                if (page == 1) {
                                    jobHomeView.showHomeFair(commonBean)
                                } else {
                                    jobHomeView.loadMoreFair(commonBean)
                                }
                            }
                        }
                        is RefreshState.Failure -> jobHomeView.onError("${refreshState.throwable}")
                    }
                }
            }
            NOTICE -> {
                getNotices(NOTICE_TYPE, page) { refreshState, rotationBean, importantBean, commonBean ->
                    when (refreshState) {
                        is RefreshState.Success -> {
                            if ((rotationBean == null || rotationBean.isEmpty()) && (importantBean == null || importantBean.isEmpty()) && (commonBean == null || commonBean.isEmpty())) {
                                jobHomeView.onNull()
                            } else {
                                if (page == 1) {
                                    jobHomeView.showThree(merge(rotationBean, importantBean, commonBean))
                                } else {
                                    jobHomeView.loadMoreOther(commonBean.orEmpty())
                                }
                            }
                        }
                        is RefreshState.Failure -> jobHomeView.onError("${refreshState.throwable}")
                    }
                }
            }
            DYNAMIC -> {
                getNotices(DYNAMIC_TYPE, page) { refreshState, rotationBean, importantBean, commonBean ->
                    when (refreshState) {
                        is RefreshState.Success -> {
                            if ((rotationBean == null || rotationBean.isEmpty()) && (importantBean == null || importantBean.isEmpty()) && (commonBean == null || commonBean.isEmpty())) {
                                jobHomeView.onNull()
                            } else {
                                if (page == 1) {
                                    jobHomeView.showThree(merge(topRotationBean(rotationBean.orEmpty()), importantBean, commonBean))
                                } else {
                                    jobHomeView.loadMoreOther(commonBean.orEmpty())
                                }
                            }
                        }
                        is RefreshState.Failure -> jobHomeView.onError("${refreshState.throwable}")
                    }
                }
            }
        }
    }

    private fun merge(rotationBean: List<HomeDataR>?, importantBean: List<HomeDataR>?, commonBean: List<HomeDataR>?): MutableList<HomeDataR> {
        val dataRBean = mutableListOf<HomeDataR>()
        dataRBean.addAll(rotationBean.orEmpty())
        dataRBean.addAll(importantBean.orEmpty())
        dataRBean.addAll(commonBean.orEmpty())
        return dataRBean
    }

    private fun topRotationBean(rotationBean: List<HomeDataR>): List<HomeDataR> {
        val newRotationBean = mutableListOf<HomeDataR>()
        repeat(rotationBean.size) {
            rotationBean[it].apply { newRotationBean.add(HomeDataR(click, date, id, "1", title)) }
        }
        return newRotationBean
    }

    private fun List<HomeDataL>.convertR(): List<HomeDataR> {
        return this.map { HomeDataR(it.click, it.date, it.id, it.important, it.title) }
    }
}