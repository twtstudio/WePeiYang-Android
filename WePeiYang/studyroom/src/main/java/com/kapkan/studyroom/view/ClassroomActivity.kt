package com.kapkan.studyroom.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.kapkan.studyroom.service.ViewModel
import kotlinx.android.synthetic.main.window_classroom.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class ClassroomActivity: AppCompatActivity() {
    var select:BooleanArray = BooleanArray(12){false}
    val viewModel = ViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        select = intent.getBooleanArrayExtra("class")

    }


    fun init(){



    }


    fun star(){

    }

    fun unstar(){

    }
}