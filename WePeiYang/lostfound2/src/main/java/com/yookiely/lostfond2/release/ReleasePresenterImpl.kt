package com.yookiely.lostfond2.release

import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.yookiely.lostfond2.service.LostFoundService
import com.yookiely.lostfond2.service.MyListDataOrSearchBean
import com.yookiely.lostfond2.service.Utils
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

class ReleasePresenterImpl(private var releaseView: ReleaseContract.ReleaseView) : ReleaseContract.ReleasePresenter {


    override fun uploadReleaseData(map: Map<String, Any>, lostOrFound: String) {

        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", map["title"].toString())
                .addFormDataPart("time", map["time"].toString())
                .addFormDataPart("place", map["place"].toString())
                .addFormDataPart("detail_type", map["detail_type"].toString())
                .addFormDataPart("card_number", map["card_number"].toString())
                .addFormDataPart("card_name", map["card_name"].toString())
                .addFormDataPart("name", map["name"].toString())
                .addFormDataPart("phone", map["phone"].toString())
                .addFormDataPart("item_description", map["item_description"].toString())
                .addFormDataPart("other_tag", "")
                .addFormDataPart("duration", map["duration"].toString())
                .addFormDataPart("campus", map["campus"].toString())

        if (lostOrFound == Utils.STRING_FOUND) {
            builder.addFormDataPart("recapture_place", map["recapture_place"].toString())
                    .addFormDataPart("recapture_entrance", map["recapture_entrance"].toString())
        }
        val list = builder.build().parts()

        launch(UI + QuietCoroutineExceptionHandler) {
            LostFoundService.updateReleaseWithPic(lostOrFound, list).awaitAndHandle {
                this@ReleasePresenterImpl.failCallback("上传失败")
            }?.let {
                if (it.error_code == -1 && it.data != null) {
                    this@ReleasePresenterImpl.successCallBack(it.data!!)
                }
            }
        }
    }

    override fun uploadReleaseDataWithPic(map: Map<String, Any>, lostOrFound: String, listOfFile: MutableList<File?>) {

        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", map["title"].toString())
                .addFormDataPart("time", map["time"].toString())
                .addFormDataPart("place", map["place"].toString())
                .addFormDataPart("detail_type", map["detail_type"].toString())
                .addFormDataPart("card_number", map["card_number"].toString())
                .addFormDataPart("card_name", map["card_name"].toString())
                .addFormDataPart("name", map["name"].toString())
                .addFormDataPart("phone", map["phone"].toString())
                .addFormDataPart("item_description", map["item_description"].toString())
                .addFormDataPart("other_tag", "")
                .addFormDataPart("duration", map["duration"].toString())
                .addFormDataPart("campus", map["campus"].toString())

        listOfFile.forEach {
            if (it != null) {
                val imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), it)
                builder.addFormDataPart("pic[]", it.name, imageBody)
            }
        }

        if (lostOrFound == Utils.STRING_FOUND) {
            builder.addFormDataPart("recapture_place", map["recapture_place"].toString())
                    .addFormDataPart("recapture_entrance", map["recapture_entrance"].toString())
        }

        val list = builder.build().parts()

        launch(UI + QuietCoroutineExceptionHandler) {
            LostFoundService.updateReleaseWithPic(lostOrFound, list).awaitAndHandle {
                this@ReleasePresenterImpl.failCallback("上传失败")
            }?.let {
                if (it.error_code == -1 && it.data != null) {
                    this@ReleasePresenterImpl.successCallBack(it.data!!)
                }
            }

        }
    }

    override fun successCallBack(beanList: List<MyListDataOrSearchBean>) = releaseView.successCallBack(beanList)

    override fun successEditCallBack(beanList: List<MyListDataOrSearchBean>) = releaseView.successCallBack(beanList)

    override fun deleteSuccessCallBack() = releaseView.deleteSuccessCallBack()

    override fun failCallback(message: String) = releaseView.failCallBack(message)

    override fun delete(id: Int) {
        launch(UI + QuietCoroutineExceptionHandler) {
            LostFoundService.delete(id.toString()).awaitAndHandle {
                this@ReleasePresenterImpl.failCallback("删除失败")
            }?.let {
                if (it.error_code == -1) {
                    this@ReleasePresenterImpl.deleteSuccessCallBack()
                }
            }
        }
    }

    override fun uploadEditDataWithPic(map: Map<String, Any>, lostOrFound: String, listOfFile: MutableList<File?>, listOfString: MutableList<String?>, id: Int) {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("kept_picture", listOfString.toString())
                .addFormDataPart("title", map["title"].toString())
                .addFormDataPart("time", map["time"].toString())
                .addFormDataPart("place", map["place"].toString())
                .addFormDataPart("detail_type", map["detail_type"].toString())
                .addFormDataPart("card_number", map["card_number"].toString())
                .addFormDataPart("card_name", map["card_name"].toString())
                .addFormDataPart("name", map["name"].toString())
                .addFormDataPart("phone", map["phone"].toString())
                .addFormDataPart("item_description", map["item_description"].toString())
                .addFormDataPart("other_tag", "")
                .addFormDataPart("duration", map["duration"].toString())
                .addFormDataPart("campus", map["campus"].toString())

        listOfFile.forEach {
            if (it != null) {
                val imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), it)
                builder.addFormDataPart("pic[]", it.name, imageBody)
            }
        }

        if (lostOrFound == Utils.STRING_FOUND) {
            builder.addFormDataPart("recapture_place", map["recapture_place"].toString())
                    .addFormDataPart("recapture_entrance", map["recapture_entrance"].toString())
        }

        val list = builder.build().parts()
        val anotherLostOrFound = if (Objects.equals(lostOrFound, Utils.STRING_EDIT_LOST)) Utils.STRING_LOST else Utils.STRING_FOUND

        launch(UI + QuietCoroutineExceptionHandler) {
            LostFoundService.updateEditWithPic(anotherLostOrFound, id.toString(), list).awaitAndHandle {
                this@ReleasePresenterImpl.failCallback("编辑失败")
            }?.let {
                if (it.error_code == -1 && it.data != null) {
                    this@ReleasePresenterImpl.successEditCallBack(it.data!!)
                }
            }
        }
    }

    override fun loadDetailDataForEdit(id: Int, releaseView: ReleaseContract.ReleaseView) {
        launch(UI + QuietCoroutineExceptionHandler) {
            LostFoundService.getDetailed(id).awaitAndHandle {
                this@ReleasePresenterImpl.failCallback("数据拉取失败了")
            }?.let {
                if (it.error_code == -1 && it.data != null) {
                            releaseView.setEditData(it.data!!)
                        }
                    }
        }
    }
}
