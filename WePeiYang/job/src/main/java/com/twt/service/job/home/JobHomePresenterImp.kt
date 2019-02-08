package com.twt.service.job.home

import com.twt.service.job.service.*
import com.twt.service.job.service.JobHomeModel.getNotioces
import com.twt.service.job.service.JobHomeModel.getRecruits
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
                                    jobHomeView.showThree(merge(null, convert(importantBean.orEmpty()), convert(commonBean.orEmpty())))
                                } else { // up load more
                                    jobHomeView.loadMoreOther(convert(commonBean.orEmpty()))
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
                getNotioces(NOTICE_TYPE, page) { refreshState, rotationBean, importantBean, commonBean ->
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
                getNotioces(DYNAMIC_TYPE, page) { refreshState, rotationBean, importantBean, commonBean ->
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

    fun merge(rotationBean: List<HomeDataR>?, importantBean: List<HomeDataR>?, commonBean: List<HomeDataR>?): MutableList<HomeDataR> {
        val dataRBean = mutableListOf<HomeDataR>()
        dataRBean.addAll(rotationBean.orEmpty())
        dataRBean.addAll(importantBean.orEmpty())
        dataRBean.addAll(commonBean.orEmpty())
        return dataRBean
    }

    fun topRotationBean(rotationBean: List<HomeDataR>): List<HomeDataR> {
        val newRotationBean = mutableListOf<HomeDataR>()
        repeat(rotationBean.size) {
            rotationBean[it].apply { newRotationBean.add(HomeDataR(click, date, id, "1", title)) }
        }
        return newRotationBean
    }

    fun convert(commonL: List<HomeDataL>): List<HomeDataR> {
        val commomRs: MutableList<HomeDataR> = mutableListOf()
        repeat(commonL.size) { i ->
            commonL[i].apply {
                commomRs.add(HomeDataR(click, date, id, important, title))
            }
        }
        return commomRs
    }
}