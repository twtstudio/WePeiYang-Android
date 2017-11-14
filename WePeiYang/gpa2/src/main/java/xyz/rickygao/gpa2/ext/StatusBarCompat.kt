package xyz.rickygao.gpa2.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowManager

/**
 * Created by rickygao on 2017/11/14.
 */
@SuppressLint("PrivateApi")
fun Activity.setLightStatusBarMode(enable: Boolean): String {

    // for MIUI
    try {
        val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
        val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
        val darkModeFlag = field.getInt(layoutParams)
        val extraFlagField = window::class.java.getMethod("setExtraFlags", Int::class.java, Int::class.java)
        extraFlagField.invoke(window, if (enable) darkModeFlag else 0, darkModeFlag)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                    if (enable) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    else View.SYSTEM_UI_FLAG_VISIBLE
        }
        return "MIUI"
    } catch (e: Exception) {

    }

    // for Flyme
    try {
        val layoutParams = window.attributes
        val darkFlag = WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
        val meizuFlags = WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
        darkFlag.isAccessible = true
        meizuFlags.isAccessible = true
        val bit = darkFlag.getInt(null)
        var value = meizuFlags.getInt(layoutParams)
        value = if (enable) value or bit else value and bit.inv()
        meizuFlags.setInt(layoutParams, value)
        window.attributes = layoutParams
        return "Flyme"
    } catch (e: Exception) {

    }

    // for Android M
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.decorView.systemUiVisibility =
                if (enable) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                else View.SYSTEM_UI_FLAG_VISIBLE
        return "Android M"
    }

    return "None"
}