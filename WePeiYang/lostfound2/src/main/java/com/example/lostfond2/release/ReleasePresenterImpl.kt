package com.example.lostfond2.release

import com.example.lostfond2.service.InverseID
import com.example.lostfond2.service.LostFoundService
import com.example.lostfond2.service.MyListDataOrSearchBean
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

class ReleasePresenterImpl(var releaseView: ReleaseContract.ReleaseView) : ReleaseContract.ReleasePresenter {


    override fun uploadReleaseData(map: Map<String, Any>, lostOrFound: String) {
        launch(CommonPool + QuietCoroutineExceptionHandler) {
            val beanList = LostFoundService.updateRelease(map, lostOrFound).await()

            if (beanList.error_code == -1) {
                this@ReleasePresenterImpl.successCallBack(beanList.data!!)
            }
        }
    }

    override fun uploadReleaseDataWithPic(map: Map<String, Any>, lostOrFound: String, file: File) {

        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        builder.apply {
            addFormDataPart("pic[]", file.name, imageBody)
            addFormDataPart("title", map["title"].toString())
            addFormDataPart("time", map["time"].toString())
            addFormDataPart("place", map["place"].toString())
            addFormDataPart("detail_type", map["detail_type"].toString())
            addFormDataPart("card_number", map["card_number"].toString())
            addFormDataPart("card_name", map["card_name"].toString())
            addFormDataPart("name", map["name"].toString())
            addFormDataPart("phone", map["phone"].toString())
            addFormDataPart("item_description", map["item_description"].toString())
            addFormDataPart("other_tag", "")
            addFormDataPart("duration", map["duration"].toString())
        }
        val parts: List<MultipartBody.Part> = builder.build().parts()

        launch(CommonPool + QuietCoroutineExceptionHandler) {
            val beanList = LostFoundService.updateReleaseWithPic(lostOrFound, parts).await()
            if (beanList.error_code == -1) {
                this@ReleasePresenterImpl.successCallBack(beanList.data!!)
            }
        }
    }

    override fun successCallBack(beanList: List<MyListDataOrSearchBean>) = releaseView.successCallBack(beanList)

    override fun successEditCallback(beanList: List<MyListDataOrSearchBean>) = releaseView.successCallBack(beanList)

    override fun deleteSuccessCallBack(beanList: InverseID) = releaseView.deleteSuccessCallBack()

    override fun delete(id: Int) {
        launch(CommonPool + QuietCoroutineExceptionHandler) {
            val commonBodyOfId = LostFoundService.delete(id.toString()).await()

            if (commonBodyOfId.error_code == -1) {
                this@ReleasePresenterImpl.deleteSuccessCallBack(commonBodyOfId.data!!)
            }
        }
    }

    override fun uploadEditDataWithPic(map: Map<String, Any>, lostOrFound: String, file: File, id: Int) {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)

        if (file != null) {
            val imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            builder.addFormDataPart("pic[]", file.name, imageBody)
        }

        builder.apply {
            addFormDataPart("title", map["title"].toString())
            addFormDataPart("time", map["time"].toString())
            addFormDataPart("place", map["place"].toString())
            addFormDataPart("detail_type", map["detail_type"].toString())
            addFormDataPart("card_number", map["card_number"].toString())
            addFormDataPart("card_name", map["card_name"].toString())
            addFormDataPart("name", map["name"].toString())
            addFormDataPart("phone", map["phone"].toString())
            addFormDataPart("item_description", map["item_description"].toString())
            addFormDataPart("other_tag", "")
            addFormDataPart("duration", map["duration"].toString())
        }
        val parts: List<MultipartBody.Part> = builder.build().parts()
        val anotherLostOrFound = if (Objects.equals(lostOrFound, "editLost")) "lost" else "found"

        launch(CommonPool + QuietCoroutineExceptionHandler) {
            val beanList = LostFoundService.updateEditWithPic(anotherLostOrFound, id.toString(), parts).await()

            if (beanList.error_code == -1) {
                this@ReleasePresenterImpl.successEditCallback(beanList.data!!)
            }
        }
    }

    override fun loadDetailDataForEdit(id: Int, newReleaseView: ReleaseContract.ReleaseView) {
        launch(CommonPool + QuietCoroutineExceptionHandler) {
            val detailData = LostFoundService.getDetailed(id).await()

            if (detailData.error_code == -1) {
                newReleaseView.setEditData(detailData.data!!)
            }
        }
    }
}