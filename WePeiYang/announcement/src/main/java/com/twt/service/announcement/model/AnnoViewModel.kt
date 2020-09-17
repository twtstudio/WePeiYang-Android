package com.twt.service.announcement.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.twt.service.announcement.service.Tag

class AnnoViewModel : ViewModel() {
    private val tagLiveData = MutableLiveData<List<Tag>>()

    val tagTree
        get() = tagLiveData

    fun setTagTree(data : List<Tag>){
        tagLiveData.postValue(data)
    }
}