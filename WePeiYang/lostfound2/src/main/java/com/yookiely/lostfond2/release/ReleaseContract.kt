package com.yookiely.lostfond2.release

import com.yookiely.lostfond2.service.DetailData
import com.yookiely.lostfond2.service.MyListDataOrSearchBean
import java.io.File

interface ReleaseContract {

    interface ReleaseView {

        fun successCallBack(beanList: List<MyListDataOrSearchBean>)

        fun failCallBack(message: String)

        fun setEditData(detailData: DetailData)

        fun deleteSuccessCallBack()

        fun drawRecyclerView(position: Int)

        fun onTypeItemSelected(position: Int)
    }

    interface ReleasePresenter {

        fun uploadReleaseData(map: Map<String, Any>, lostOrFound: String)

        fun uploadReleaseDataWithPic(map: Map<String, Any>, lostOrFound: String, listOfFile: MutableList<File?>)

        fun successCallBack(beanList: List<MyListDataOrSearchBean>)

        fun successEditCallBack(beanList: List<MyListDataOrSearchBean>)

        fun failCallback(message: String)

        fun delete(id: Int)

        fun deleteSuccessCallBack()

        fun uploadEditDataWithPic(map: Map<String, Any>, lostOrFound: String, listOfFile: MutableList<File?>, listOfString: MutableList<String?>, id: Int)

        fun loadDetailDataForEdit(id: Int, releaseView: ReleaseView)
    }
}