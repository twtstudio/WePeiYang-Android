package com.twt.service.announcement.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.twt.service.announcement.service.Tag

class AnnoViewModel : ViewModel() {
    private val tagLiveData = MutableLiveData<List<Tag>>()
    private val quesLiveData = MutableLiveData<Int>()

    val quesId
        get() = quesLiveData

    val tagTree
        get() = tagLiveData

    fun setQuesId(id: Int) {
        quesLiveData.postValue(id)
    }

    fun setTagTree(data: List<Tag>) {
        tagLiveData.postValue(data)
    }
}
