package com.twt.wepeiyang.commons.experimental

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import java.lang.ref.WeakReference

/**
 * Created by rickygao on 2018/3/9.
 */

fun initCommons(block: Commons.() -> Unit) = Commons.block()

object Commons {

    private var applicationReference: WeakReference<Application>? = null

    fun registerApplication(application: Application) {
        if (applicationReference == null || applicationReference?.get() == null) {
            applicationReference = WeakReference(application)
        } else throw IllegalStateException("Application should be registered no more than once.")
    }

    val application: Application
        get() = applicationReference?.get()
                ?: throw IllegalStateException("Application should be registered.")

    val applicationContext: Context
        get() = application

    private var loginActivityClass: Class<out Activity>? = null

    fun registerLoginActivity(clazz: Class<out Activity>) {
        loginActivityClass = clazz
    }

    fun startLoginActivity() = loginActivityClass?.let {
        val intent = Intent(applicationContext, it)
        applicationContext.startActivity(intent)
    } ?: throw IllegalStateException("Login activity should be registered.")

}