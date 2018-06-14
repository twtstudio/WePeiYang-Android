package com.twtstudio.service.dishesreviews.share

import android.Manifest
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by SGXM on 2018/5/28.
 */
class ScreenShootUtil {
    private fun takeScreenShot(activity: Activity): Bitmap {
        val view = activity.window.decorView
//        view.isDrawingCacheEnabled = true
//        view.buildDrawingCache()
//        val b1 = view.drawingCache//取出图源
        //获取状态栏高度
        val frame = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(frame)
        val statusBarHeight = frame.top

        //获取屏幕长和高
        val size = Point()
        activity.windowManager.defaultDisplay.getSize(size)
        val mWidth = size.x
        val mHeight = size.y
        val res = Bitmap.createBitmap(mWidth, (mHeight - statusBarHeight), Bitmap.Config.RGB_565)
        val canvas = Canvas(res)
        view.draw(canvas)

        return res
    }

    fun save(pic: Bitmap, strFile: String, context: Context) {
        val filePath = strFile + "/" + System.currentTimeMillis() + ".jpg"
        val fileDic = File(strFile)
        val file = File(filePath)
        EasyPermissions.requestPermissions(context, "微北洋需要外部存储来提供必要的缓存\n 需要位置信息来获取校园网连接状态", 0, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

        try {
            if (!fileDic.exists()) {
                fileDic.mkdir()
            }
            if (!file.exists()) {
                file.createNewFile()
            }
            val fot = FileOutputStream(filePath)
            pic.compress(Bitmap.CompressFormat.PNG, 70, fot)//压缩率70%
            fot.write(pic.rowBytes)
            fot.flush()
            fot.close()

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
//
//    fun shoot(activity: Activity): String {
//        val strFile = "sdcard/" + System.currentTimeMillis().toString() + ".png"
//        save(takeScreenShot(activity), strFile)
//        return strFile
//    }

    fun shoot(activity: Activity): Bitmap {
        return takeScreenShot(activity)
    }
}