package com.twt.wepeiyang.commons.experimental

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import java.lang.ref.WeakReference

/**
 * A common context, should be initialized at the beginning of the application.
 */
object CommonContext {

    private var applicationReference: WeakReference<Application>? = null

    fun registerApplication(application: Application) {
        applicationReference = WeakReference(application)
    }

    val application: Application
        get() = applicationReference?.get()
                ?: throw IllegalStateException("Application should be registered.")

    val applicationContext: Context
        get() = application

    val applicationVersion: String by lazy {
        applicationContext.packageManager
                .getPackageInfo(applicationContext.packageName, 0).versionName
    }

    private var activityClasses: MutableMap<String, Class<out Activity>> = mutableMapOf()

    fun registerActivity(name: String, clazz: Class<out Activity>) {
        activityClasses[name] = clazz
    }

    fun startActivity(context: Context = applicationContext, name: String, block: Intent.() -> Unit = {}) =
            activityClasses[name]?.let {
                context.startActivity(Intent(context, it).apply(block))
            } ?: throw IllegalStateException("Activity class $name should be registered.")

}

fun Context.startActivity(name: String, block: Intent.() -> Unit = {}) = CommonContext.startActivity(this, name, block)