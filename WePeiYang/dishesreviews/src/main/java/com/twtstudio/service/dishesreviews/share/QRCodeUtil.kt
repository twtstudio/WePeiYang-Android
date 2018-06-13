package com.twtstudio.service.dishesreviews.share

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.*


/**
 * Created by SGXM on 2018/5/31.
 */
class QRCodeUtil {
    fun createQRCode(content: String, width: Int, height: Int): Bitmap? {
        val qrCodeWriter = QRCodeWriter()
        val hints = HashMap<EncodeHintType, Any>().apply {
            put(EncodeHintType.CHARACTER_SET, "utf-8")
            put(EncodeHintType.MARGIN, 1)
            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H)//为了logo
        }
        try {
            val encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints)
            val pixels = IntArray(width * height)
            for (i in 0 until height) {
                for (j in 0 until width) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x000000//黑色
                    } else {
                        pixels[i * width + j] = 0xffffff//白色
                    }
                }
            }

            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565).copy(Bitmap.Config.RGB_565, true)
        } catch (e: WriterException) {
            e.printStackTrace()
        }

        return null
    }

    fun createQRCodeWithLogo(content: String, width: Int, height: Int, logo: Bitmap): Bitmap? {
        try {
            var res = createQRCode(content, width, height)
            val canvas = Canvas(res)
            canvas.save()
            val ratio = width / (logo.width.toFloat() * 5)
            canvas.scale(ratio, ratio)//0.5表示将后面的绘制都缩小为0.5比如绘制400*400=》200*200 距离绘制点位置也会缩小
            canvas.drawBitmap(logo, (canvas.width / ratio - logo.width) / 2.toFloat(), (canvas.height / ratio - logo.height) / 2.toFloat(), Paint())
            canvas.restore()
            return res
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}