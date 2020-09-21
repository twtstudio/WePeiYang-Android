package com.twt.service.announcement.model

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.twt.service.announcement.service.Tag

class AnnoViewModel : ViewModel() {
    private val tagTreeLiveData = MutableLiveData<List<Tag>>()
    private val searchQuesLiveData = MutableLiveData<Int>()

    val quesId get() = searchQuesLiveData

    val tagTree get() = tagTreeLiveData

    fun searchQuestion(id: Int) {
        searchQuesLiveData.postValue(id)
    }

    fun setTagTree(data: List<Tag>) {
        tagTreeLiveData.postValue(data)
    }
}
