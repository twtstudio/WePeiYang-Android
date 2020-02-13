package com.example.studyroom.service

import com.twt.wepeiyang.commons.experimental.CommonContext
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class ViewModel {



    fun getBuildingList() {
        GlobalScope.launch(Dispatchers.Main) {
            StudyServiceManager.getBuildingList().awaitAndHandle {
                Toasty.info(CommonContext.application, it.message.toString()).show()
            }?.let {
                if (it.error_code == -1) {
                    BuildingListData.postValue(it)
                } else Toasty.error(CommonContext.application, it.message).show()
            }
        }
    }
}