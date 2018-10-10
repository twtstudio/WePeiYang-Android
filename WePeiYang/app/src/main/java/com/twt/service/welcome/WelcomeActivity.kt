package com.twt.service.welcome

import agency.tango.materialintroscreen.MaterialIntroActivity
import agency.tango.materialintroscreen.SlideFragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twt.service.R
import com.twt.service.home.HomeNewActivity
import com.twt.service.settings.LibBindFragment
import com.twt.service.settings.TjuBindFragment
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences


class WelcomeActivity : MaterialIntroActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        addSlide(GpaWelcomeFragment())
//        addSlide(ScheduleWelcomeFragment())
//        addSlide(BikeWelcomeFragment())
//        addSlide(BriefWelcomeFragment())
//        if (!CommonPreferences.isBindTju) addSlide(TjuBindFragment())
//        if (!CommonPreferences.isBindLibrary) addSlide(LibBindFragment())
        onFinish()
    }

    override fun onFinish() {
        super.onFinish()
        val intent = Intent(this, HomeNewActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}

abstract class SimpleSlideFragment : SlideFragment() {

    abstract val resource: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(resource, container, false)

    override fun backgroundColor() = R.color.white_color

    override fun buttonsColor() = R.color.colorAccent

    override fun canMoveFurther() = true

}

class GpaWelcomeFragment : SimpleSlideFragment() {

    override val resource: Int
        get() = R.layout.fragment_gpa_welcome_slide

}

class ScheduleWelcomeFragment : SimpleSlideFragment() {

    override val resource: Int
        get() = R.layout.fragment_schedule_welcome_slide

}

class BikeWelcomeFragment : SimpleSlideFragment() {

    override val resource: Int
        get() = R.layout.fragment_bike_welcome_slide

}

class BriefWelcomeFragment : SimpleSlideFragment() {

    override val resource: Int
        get() = R.layout.fragment_brief_welcome_slide

}