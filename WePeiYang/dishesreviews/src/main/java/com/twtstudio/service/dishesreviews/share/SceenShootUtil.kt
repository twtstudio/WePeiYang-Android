package com.twtstudio.service.dishesreviews.share

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by SGXM on 2018/5/28.
 */
class SceenShootUtil {
    private fun takeScreenShot(activity: Activity): Bitmap {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val b1 = view.drawingCache//取出图源
        //获取状态栏高度
        val frame = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(frame)
        val statusBarHeight = frame.top

        //获取屏幕长和高
        var size = Point()
        activity.windowManager.defaultDisplay.getSize(size)
        val width = size.x
        val height = size.y

        val res = Bitmap.createBitmap(b1, 0, statusBarHeight, width, (height - statusBarHeight))//返回源图指定子集的位图
        view.destroyDrawingCache()
        return res
    }

    private fun save(pic: Bitmap, strFile: String) {

        try {
            var fot = FileOutputStream(strFile)
            pic.compress(Bitmap.CompressFormat.PNG, 70, fot)//压缩率70%
            fot.flush()
            fot.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun shoot(activity: Activity): String {
        val strFile = "sdcard/" + System.currentTimeMillis().toString() + ".png"
        save(takeScreenShot(activity), strFile)
        return strFile
    }
}