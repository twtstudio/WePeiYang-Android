package com.twt.service.settings


import agency.tango.materialintroscreen.MaterialIntroActivity
import android.os.Bundle

/**
 * Created by retrox on 01/03/2017.
 */

class BindActivity : MaterialIntroActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableLastSlideAlphaExitTransition(true)
        addSlide(TjuBindFragment())
        addSlide(LibBindFragment())
    }
}


