package com.yookiely.lostfond2.release

import android.util.Log
import com.yookiely.lostfond2.service.LostFoundService
import com.yookiely.lostfond2.service.MyListDataOrSearchBean
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class ReleasePresenterImpl(private var releaseView: ReleaseContract.ReleaseView) : ReleaseContract.ReleasePresenter {


    override fun uploadReleaseData(map: Map<String, Any>, lostOrFound: String) {

        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
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
            addFormDataPart("campus", map["campus"].toString())

            if (lostOrFound == "found") {
                addFormDataPart("recapture_place", map["recapture_place"].toString())
                addFormDataPart("recapture_entrance", map["recapture_entrance"].toString())
            }
        }
        val parts: List<MultipartBody.Part> = builder.build().parts()

        launch(UI + QuietCoroutineExceptionHandler) {
            val beanList = LostFoundService.updateReleaseWithPic(lostOrFound, parts).await()

            if (beanList.error_code == -1) {
                this@ReleasePresenterImpl.successCallBack(beanList.data!!)
            }
        }
    }

    override fun uploadReleaseDataWithPic(map: Map<String, Any>, lostOrFound: String, arrayOfFile: ArrayList<File?>) {

        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)

        builder.apply {
            for (i in arrayOfFile) {
                if (i != null) {
                    val imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), i)
                    addFormDataPart("pic[]", i.name, imageBody)
                }
            }

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
            addFormDataPart("campus", map["campus"].toString())

            if (lostOrFound == "found") {
                addFormDataPart("recapture_place", map["recapture_place"].toString())
                addFormDataPart("recapture_entrance", map["recapture_entrance"].toString())
            }
        }
        val parts: List<MultipartBody.Part> = builder.build().parts()

        launch(UI + QuietCoroutineExceptionHandler) {
            val beanList = LostFoundService.updateReleaseWithPic(lostOrFound, parts).await()

            if (beanList.error_code == -1) {
                this@ReleasePresenterImpl.successCallBack(beanList.data!!)
            }
        }
    }

    override fun successCallBack(beanList: List<MyListDataOrSearchBean>) = releaseView.successCallBack(beanList)

    override fun successEditCallback(beanList: List<MyListDataOrSearchBean>) = releaseView.successCallBack(beanList)

    override fun deleteSuccessCallBack(string: String) = releaseView.deleteSuccessCallBack()

    override fun delete(id: Int) {
        launch(UI + QuietCoroutineExceptionHandler) {
            val commonBodyOfId = LostFoundService.delete(id.toString()).await()

            if (commonBodyOfId.error_code == -1) {
                this@ReleasePresenterImpl.deleteSuccessCallBack(commonBodyOfId.data!!)
            }
        }
    }

    override fun uploadEditDataWithPic(map: Map<String, Any>, lostOrFound: String, arrayOfFile: ArrayList<File?>, arrayOfString: ArrayList<String?>, id: Int) {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)

        builder.apply {
            for (i in arrayOfFile) {
                if (i != null) {
                    val imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), i)
                    addFormDataPart("pic[]", i.name, imageBody)
                }
            }

            for (i in arrayOfString) {
                if (i != null) {
                    addFormDataPart("kept_picture", i)
                }
            }
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
            addFormDataPart("campus", map["campus"].toString())

            if (lostOrFound == "editFound") {
                addFormDataPart("recapture_place", map["recapture_place"].toString())
                addFormDataPart("recapture_enterance", map["recapture_enterance"].toString())
            }
        }
        val parts: List<MultipartBody.Part> = builder.build().parts()
        val anotherLostOrFound = if (Objects.equals(lostOrFound, "editLost")) "lost" else "found"

        launch(UI + QuietCoroutineExceptionHandler) {
            val beanList = LostFoundService.updateEditWithPic(anotherLostOrFound, id.toString(), parts).await()

            if (beanList.error_code == -1) {
                this@ReleasePresenterImpl.successEditCallback(beanList.data!!)
            }
        }
    }

    override fun loadDetailDataForEdit(id: Int, newReleaseView: ReleaseContract.ReleaseView) {
        launch(UI + QuietCoroutineExceptionHandler) {
            val detailData = LostFoundService.getDetailed(id).await()

            if (detailData.error_code == -1) {
                newReleaseView.setEditData(detailData.data!!)
            }
        }
    }
}