package com.twt.service.job.home

class JobHomePresenterImp : JobHomeContract.JobHomePresenter{
    //（招聘信息、招聘会）L；（公告、动态）R
    //如果是招聘会，因为没有在置顶项，只传list<CommonL>
    override fun getGeneral(kind: String, type: Int, page: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}