package xyz.rickygao.gpa2.ext

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowManager

/**
 * Created by rickygao on 2017/11/14.
 */
fun Activity.setLightStatusBarMode(enable: Boolean) {


    try {
        var darkModeFlag = 0
        val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
        val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
        darkModeFlag = field.getInt(layoutParams)
        val extraFlagField = window::class.java.getMethod("setExtraFlags", Int::class.java, Int::class.java)
        if (enable) {
            extraFlagField.invoke(window, darkModeFlag, darkModeFlag)
        } else {
            extraFlagField.invoke(window, 0, darkModeFlag)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (enable) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        }
        return
    } catch (e: Exception) {

    }


    try {
        val lp = window.getAttributes();
        val darkFlag = WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
        val meizuFlags = WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
        darkFlag.isAccessible = true;
        meizuFlags.isAccessible = true;
        val bit = darkFlag.getInt(null);
        var value = meizuFlags.getInt(lp);
        if (enable) {
            value = value or bit;
        } else {
            value = value and bit.inv();
        }
        meizuFlags.setInt(lp, value);
        window.attributes = lp;
        return
    } catch (e: Exception) {

    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (enable) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }
}