package com.twt.service.job.home

import com.twt.service.job.service.*
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import kotlinx.coroutines.experimental.Deferred

class JobHomePresenterImp : JobHomeContract.JobHomePresenter{
    //（招聘信息、招聘会）L；（公告、动态）R
    //如果是招聘会，因为没有在置顶项，只传list<CommonL>
    override fun getGeneral(kind: String,page:Int) {
        when(kind){
            JOB_MESSAGE -> {
//                JobService.getRecruits(0,page).awaitAndHandle {
//                    it.printStackTrace()
//                }
            }
            JOB_FAIR -> {
                JobService.getRecruits(1,page)
            }
            NOTICE -> {
                JobService.getNotioces(0,page)
            }
            DYNAMIC -> {
                JobService.getNotioces(1,page)
            }
        }
    }
}