package com.twt.service.announcement.model

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.twt.service.announcement.service.Tag

class AnnoViewModel : ViewModel() {
    /**
     * [tagTreeLiveData]: 以动态数组的方式存储 tagTree
     * [searchQuesLiveData]: 存储 button 对应的 tagId
     */
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
