package com.twt.service.job.home

import com.orhanobut.hawk.Hawk
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
                                if (page == 1) {// first time
                                    jobHomeView.showThree(merge(null, funs.convert(importantBean.orEmpty()), funs.convert(commonBean.orEmpty())))
                                } else { // up load more
                                    jobHomeView.loadMoreOther(merge(null, null, funs.convert(commonBean.orEmpty())))
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
                                }else{
                                    jobHomeView.loadMoreOther(merge(null,null,commonBean))
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
                                    jobHomeView.showThree(merge(rotationBean, importantBean, commonBean))
                                }else{
                                    jobHomeView.loadMoreOther(merge(null,null,commonBean))
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
}